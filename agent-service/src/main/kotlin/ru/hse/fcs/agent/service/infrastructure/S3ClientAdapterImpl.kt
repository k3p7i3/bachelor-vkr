package ru.hse.fcs.agent.service.infrastructure

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.hse.fcs.agent.service.config.properties.S3Properties
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration


@Service
class S3ClientAdapterImpl(
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    private val s3Properties: S3Properties
): S3ClientAdapter {

    override fun getPresignedUrl(
        bucketName: String,
        path: String
    ): S3ClientAdapter.Response<String> {
        s3Presigner
            .use { presigner ->
            val getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build()

            val presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(s3Properties.presignedUrlDuration))
                .getObjectRequest(getObjectRequest)
                .build()

            try {
                val presignedRequest = presigner.presignGetObject(presignRequest)

                return S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.SUCCESS,
                    presignedRequest.url().toString()
                )
            } catch (exc: Exception) {
                logger.error(
                    "Failed to generate presigned url for $path in bucket $bucketName",
                    exc
                )
                return S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.FAIL,
                    path
                )
            }
        }
    }

    override fun createDirectory(
        bucketName: String,
        dirName: String
    ) : S3ClientAdapter.Response<String> {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key("$dirName/")
            .build()

        try {
            s3Client.putObject(putObjectRequest, RequestBody.empty())
            logger.trace("Created new directory $dirName in bucket $bucketName")

            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.SUCCESS,
                dirName
            )
        } catch (exc: Exception) {
            logger.error(
                "Failed to create new directory $dirName in bucket $bucketName",
                exc
            )
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.FAIL,
                dirName
            )
        }
    }

    override fun deleteObject(
        bucketName: String,
        path: String
    ): S3ClientAdapter.Response<List<String>> {
        return when (isDirectory(bucketName, path)) {
            true -> deleteDirectory(bucketName, path)
            false -> deleteFiles(bucketName, listOf(path))
        }
    }

    override fun deleteDirectory(
        bucketName: String,
        dirName: String
    ) : S3ClientAdapter.Response<List<String>> {
        try {
            if (!checkObjectExist(bucketName, "$dirName/")) {
                logger.warn(
                    "Directory $dirName/ does not exist in bucket $bucketName, " +
                        "delete will be skipped "
                )
                return S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.DIRECTORY_NOT_FOUND,
                    emptyList()
                )
            }

            val listNestedRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("$dirName/")
                .build()

            val nestedObjects = s3Client.listObjectsV2(listNestedRequest)
                .contents()
                .map { it.key() }

            val deleteNested = deleteFiles(bucketName, fileNames = nestedObjects)

            if (deleteNested.responseType != S3ClientAdapter.ResponseType.SUCCESS) {
                logger.error(
                    "Couldn't delete all objects for directory $dirName/, " +
                        "deleting returned ${deleteNested.responseType}. " +
                        "Directory delete will be skipped"
                )

                return S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.FAIL,
                    deleteNested.response
                )
            }

            logger.trace("Successfully deleted directory $dirName in bucket $bucketName")
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.SUCCESS,
                deleteNested.response
            )
        } catch (exc: Exception) {
            logger.error("Unexpected error during directory delete!", exc)
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.FAIL,
                emptyList()
            )
        }
    }

    override fun deleteFiles(
        bucketName: String,
        fileNames: List<String>
    ) : S3ClientAdapter.Response<List<String>> {
        val deleteObjectsRequest = DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete(
                Delete.builder()
                    .objects(
                        fileNames.map {
                            ObjectIdentifier.builder()
                                .key(it)
                                .build()
                        }
                    )
                    .build()
            )
            .build()

        try {
            val deleteResult = s3Client.deleteObjects(deleteObjectsRequest)
            val deletedFiles = deleteResult.deleted()
                .map(DeletedObject::key)
            val deletedQuantity = deleteResult.deleted().size

            val responseType = when (deletedQuantity == fileNames.size) {
                true -> {
                    logger.trace("Successfully deleted $deletedQuantity files")
                    S3ClientAdapter.ResponseType.SUCCESS
                }
                else -> {
                    logger.warn(
                        "Deleted $deletedQuantity files of ${fileNames.size}, " +
                            "couldn't delete files: ${fileNames.filter { it !in deletedFiles }}"
                    )
                    S3ClientAdapter.ResponseType.PARTIAL_DELETE
                }
            }

            return S3ClientAdapter.Response(
                responseType,
                deletedFiles
            )
        } catch (exc: Exception) {
            logger.error("Unexpected error during file delete!", exc)
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.FAIL,
                emptyList()
            )
        }
    }

    override fun uploadFileAsBytes(
        bucketName: String,
        filePath: String,
        file: ByteArray
    ) : S3ClientAdapter.Response<String> {
        try {
            if (checkObjectExist(bucketName, filePath)) {
                logger.warn(
                    "File $filePath already exists in bucket $bucketName, " +
                        "file uploading will be skipped"
                )
                return S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.ALREADY_EXIST,
                    filePath
                )
            }

            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build()
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file))

            logger.trace(
                "Successfully uploaded file ($filePath) to bucket $bucketName"
            )
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.SUCCESS,
                filePath
            )
        } catch (exc: Exception) {
            logger.error(
                "Couldn't upload file $filePath to " +
                    "bucket $bucketName due to unexpected error!",
                exc
            )
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.FAIL,
                filePath
            )
        }
    }

    override fun getFileAsBytes(
        bucketName: String,
        filePath: String
    ) : S3ClientAdapter.Response<ByteArray> {
        try {
            val byteContent: ByteArray

            val getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build()
            s3Client.getObject(getObjectRequest).use {
                byteContent = it.readBytes()
            }

            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.SUCCESS,
                byteContent
            )
        } catch (exc: Exception) {
            logger.error(
                "Couldn't get byte content of file " +
                    "$filePath from bucket $bucketName due to unexpected exception!",
                exc
            )

            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.FAIL,
                byteArrayOf()
            )
        }
    }

    override fun getDirectoryContents(
        bucketName: String,
        dirPath: String
    ): S3ClientAdapter.Response<List<String>> {
        try {
            val request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("$dirPath/")
                .delimiter("/")
                .build()
            val objectsList = s3Client.listObjectsV2(request)

            val files = objectsList.contents().map { it.key() }.filter { it != "$dirPath/" }

            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.SUCCESS,
                files
            )
        } catch (exc: Exception) {
            logger.error(
                "Couldn't get directory $dirPath contents from bucket $bucketName.",
                exc
            )
            return S3ClientAdapter.Response(
                S3ClientAdapter.ResponseType.FAIL,
                emptyList()
            )
        }
    }

    private fun checkObjectExist(bucketName: String, path: String): Boolean {
        try {
            val headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build()
            s3Client.headObject(headObjectRequest)
            return true
        } catch (e: S3Exception) {
            if (e.statusCode() == 404) {
                return false
            } else {
                throw e
            }
        }
    }

    private fun isDirectory(
        bucket: String,
        objectName: String
    ): Boolean = checkObjectExist(bucket, "$objectName/")

    companion object {

        private val logger = LoggerFactory.getLogger(S3ClientAdapterImpl::class.java)
    }
}
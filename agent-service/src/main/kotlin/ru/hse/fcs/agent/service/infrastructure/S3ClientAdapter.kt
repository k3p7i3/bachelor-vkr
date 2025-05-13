package ru.hse.fcs.agent.service.infrastructure

interface S3ClientAdapter {
    enum class ResponseType {
        SUCCESS,
        FAIL,
        DIRECTORY_NOT_FOUND,
        ALREADY_EXIST,
        PARTIAL_DELETE
    }

    data class Response<T>(
        val responseType: ResponseType,
        val response: T? = null
    )

    fun getPresignedUrl(
        bucketName: String,
        path: String
    ): Response<String>

    fun createDirectory(
        bucketName: String,
        dirName: String
    ) : Response<String>

    fun deleteDirectory(
        bucketName: String,
        dirName: String
    ) : Response<List<String>>

    fun deleteFiles(
        bucketName: String,
        fileNames: List<String>
    ) : Response<List<String>>

    fun deleteObject(
        bucketName: String,
        path: String
    ): Response<List<String>>

    fun uploadFileAsBytes(
        bucketName: String,
        filePath: String,
        file: ByteArray
    ) : Response<String>

    fun getFileAsBytes(
        bucketName: String,
        filePath: String
    ) : Response<ByteArray>

    fun getDirectoryContents(
        bucketName: String,
        dirPath: String
    ): Response<List<String>>
}
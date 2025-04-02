package ru.hse.fcs.agent.service.infrastructure

interface S3Client {

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

    fun createDirectory(
        dirName: String
    ) : Response<String>

    fun deleteDirectory(
        dirName: String
    ) : Response<List<String>>

    fun deleteFiles(
        fileNames: List<String>
    ) : Response<List<String>>

    fun deleteObject(
        path: String
    ): Response<List<String>>

    fun uploadFile(
        path: String,
        fileName: String,
        file: File
    ) : Response<String>

    fun renameObject(
        oldPath: String,
        newPath: String,
    ) : Response<List<FileRename>>

    fun getFileAsBytes(
        filePath: String
    ) : Response<ByteArray>

    fun getDirectoryContents(
        dirPath: String
    ): Response<GetDirectoryContentsResult>
}
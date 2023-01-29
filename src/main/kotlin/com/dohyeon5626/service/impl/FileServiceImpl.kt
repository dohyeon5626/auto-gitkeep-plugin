package com.dohyeon5626.service.impl

import com.dohyeon5626.service.FileService
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

class FileServiceImpl: FileService {

    private val fileSystem = LocalFileSystem.getInstance()

    override fun generateGitKeep(path: String) {
        with(File(path).listFiles()!!) {
            if (isEmpty()) {
                createFile("$path/.gitkeep")
            } else if (any { it.name != ".gitkeep" }) {
                filter { it.name == ".gitkeep" }
                    .forEach { deleteFile(it) }

                filter { it.isDirectory }
                    .forEach { generateGitKeep(it.path) }
            }
        }
    }

    private fun createFile(path: String) {
        File(path).apply { createNewFile() }
            .also { refreshFilePath(it) }
    }

    private fun deleteFile(file: File) {
        file.delete()
        refreshFilePath(file)
    }

    private fun refreshFilePath(file: File) {
        fileSystem.refreshAndFindFileByIoFile(file)
            ?.refresh(true, false)
    }

}
package com.dohyeon5626.service.impl

import com.dohyeon5626.service.FileService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File
import java.nio.file.FileSystems
import kotlin.io.path.Path

class FileServiceImpl: FileService {

    private val fileSystem = LocalFileSystem.getInstance()

    override fun generateGitKeep(path: String) {
        path.takeIf { !isGitIgnorePath("$path/.gitkeep") }
            ?.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    createFile("$path/.gitkeep")
                } else if (any { it.name != ".gitkeep" }) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                }
            }
    }

    override fun generateGitKeepInAllSubfolder(path: String) {
        path.takeIf { !isGitIgnorePath("$path/.gitkeep") }
            ?.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    createFile("$path/.gitkeep")
                } else if (any { it.name != ".gitkeep" }) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                    filter { it.isDirectory }.forEach { generateGitKeepInAllSubfolder(it.path) }
                }
            }
    }

    private fun createFile(path: String) {
        File(path).apply { createNewFile() }.also { refreshFilePath(it) }
    }

    private fun deleteFile(file: File) {
        file.apply { delete() }.also { refreshFilePath(it) }
    }

    private fun refreshFilePath(file: File) {
        fileSystem.refreshAndFindFileByIoFile(file)?.refresh(true, false)
    }

    private fun isGitIgnorePath(path: String): Boolean {
        getProject(path)?.basePath?.let {
            return File("${it}/.gitignore").readLines()
                .filter { line -> line.isNotEmpty() && !line.startsWith("#") }
                .any { ignorePattern -> isGitIgnorePatternMatch(ignorePattern, path.replace("$it/", "")) }
        }
        return false
    }

    private fun isGitIgnorePatternMatch(ignorePattern: String, path: String) =
        FileSystems.getDefault().getPathMatcher("glob:$ignorePattern").matches(Path(path))

    private fun getProject(path: String): Project? {
        ProjectManager.getInstance().openProjects.toList()
            .forEach { if(it.basePath != null && path.contains(it.basePath!!)) return it }
        return null
    }

}
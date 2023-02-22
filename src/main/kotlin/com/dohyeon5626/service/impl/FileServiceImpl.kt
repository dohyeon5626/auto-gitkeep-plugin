package com.dohyeon5626.service.impl

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.VisibleSettingComponent
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.components.service
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

class FileServiceImpl: FileService {

    private val fileSystem = LocalFileSystem.getInstance()
    private val visibleSettingComponent = service<VisibleSettingComponent>()

    override fun generateGitKeep(path: String) {
        path.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    createFile("$path/.gitkeep")
                } else if (any { it.name != ".gitkeep" }) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                }
            }
    }

    override fun generateGitKeepInAllSubfolder(path: String) {
        path.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    createFile("$path/.gitkeep")
                } else if (any { it.name != ".gitkeep" }) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                    filter { it.isDirectory }.forEach { generateGitKeepInAllSubfolder(it.path) }
                }
            }
    }

    override fun refreshVirtualFileList() {
        ProjectManager.getInstance().openProjects.forEach {
            ProjectView.getInstance(it).refresh()
        }
    }

    private fun createFile(path: String) {
        File(path).apply { createNewFile() }.also { refreshFilePath(it) }
    }

    private fun deleteFile(file: File) {
        file.apply { delete() }.also { refreshFilePath(it) }
    }

    private fun refreshFilePath(file: File) {
        if (visibleSettingComponent.state)
            fileSystem.refreshAndFindFileByIoFile(file)?.refresh(false, false)
    }

}
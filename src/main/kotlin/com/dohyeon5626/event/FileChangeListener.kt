package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File

class FileChangeListener: BulkFileListener {

    private val fileService = service<FileService>()

    override fun after(events: MutableList<out VFileEvent>) = with(fileService) {
        events.forEach { event -> when {
            event.path.endsWith(".gitignore") -> {
                getProject(event.path)?.also { project ->
                    refreshGitIgnorePath(project)
                    project.basePath?.also { refreshGitKeepInAllSubfolder(project, it) }
                }
            }
            !event.isFromRefresh && !event.isFromSave -> {
                getProject(event.path)?.also { project ->
                    if (File(event.path).isDirectory) {
                        refreshGitKeep(project, event.path)
                        refreshGitKeep(project, getParentPath(event.path))
                    } else refreshGitKeep(project, getParentPath(event.path))
                }
            }
        }}
    }

    private fun getParentPath(path: String) = path.substring(0, path.lastIndexOf("/"))

}
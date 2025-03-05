package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingStateComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File

class FileChangeListener: BulkFileListener {

    private val fileService = service<FileService>()
    private val settingStateComponent = service<SettingStateComponent>()

    override fun after(events: MutableList<out VFileEvent>) = with(fileService) {

        events.forEach { event -> if (event.path.endsWith(".gitignore")) {
            getProject(event.path)?.also { project ->
                refreshGitIgnorePath(project)
                if (settingStateComponent.state.autoCreateStatus)
                    project.basePath?.also { refreshGitKeepInAllSubfolder(project, it) }
            }
        } else if (!event.isFromRefresh && !event.isFromSave && settingStateComponent.state.autoCreateStatus) {
            getProject(event.path)?.also { project ->
                if (File(event.path).isDirectory) {
                    refreshGitKeep(project, event.path)
                    refreshGitKeep(project, getParentPath(event.path))
                } else refreshGitKeep(project, getParentPath(event.path))
            }
        }}
    }

    private fun getParentPath(path: String) = path.substring(0, path.lastIndexOf("/"))

}
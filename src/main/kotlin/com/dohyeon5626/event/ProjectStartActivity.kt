package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingStateComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class ProjectStartActivity : ProjectActivity {

    private val fileService = service<FileService>()
    private val settingStateComponent = service<SettingStateComponent>()

    override suspend fun execute(project: Project) {
        with(fileService) {
            refreshGitIgnorePath(project)
            if (settingStateComponent.state.autoCreateStatus)
                project.basePath?.also { refreshGitKeepInAllSubfolder(project, it) }
        }
    }

}

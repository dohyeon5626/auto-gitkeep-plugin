package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class ProjectStartActivity : StartupActivity {

    private val fileService = service<FileService>()

    override fun runActivity(project: Project) {
        project.also { fileService.refreshGitIgnorePath(project) }
            .basePath?.also { fileService.refreshGitKeepInAllSubfolder(project, it) }
    }

}

package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingStateComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service

class DeleteGitkeepAction: AnAction() {

    private val fileService = service<FileService>()
    private val settingStateComponent = service<SettingStateComponent>()

    override fun actionPerformed(event: AnActionEvent) {
        event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.forEach { fileService.deleteGitKeepInAllSubfolder(it.path) }
    }

    override fun update(event: AnActionEvent) {
        event.getData(CommonDataKeys.VIRTUAL_FILE)?.run {
            event.presentation.isEnabledAndVisible = (isDirectory && !settingStateComponent.state.autoCreateStatus)
        }
    }

}
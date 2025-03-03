package com.dohyeon5626.setting

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class SettingConfigurable: Configurable {

    private val fileService = service<FileService>()
    private val settingComponent = service<SettingComponent>()
    private val application = ApplicationManager.getApplication()
    private val panel = JPanel(BorderLayout())

    private val visibleCheckBox = JCheckBox("show .gitkeep")
    private val gitIgnoreUseStatusCheckBox = JCheckBox("create .gitkeep in .gitignore path")

    init {
        panel.apply {
            layout = GridLayout(25, 1)
            add(visibleCheckBox)
            add(gitIgnoreUseStatusCheckBox)
        }
    }

    override fun createComponent(): JComponent {
        settingComponent.apply {
            visibleCheckBox.isSelected = getVisible()
            gitIgnoreUseStatusCheckBox.isSelected = getGitIgnoreUseStatus()
        }
        return panel
    }

    override fun isModified() = with(settingComponent) {
        visibleCheckBox.isSelected != getVisible() || gitIgnoreUseStatusCheckBox.isSelected != getGitIgnoreUseStatus()
    }

    override fun apply() {
        application.runWriteAction {
            visibleCheckBox.apply {
                settingComponent.updateVisible(isSelected)
                fileService.run {
                    if (isSelected) refreshGitKeepVirtualFile()
                    else deleteGitKeepVirtualFile()
                }
            }
            gitIgnoreUseStatusCheckBox.apply {
                settingComponent.updateGitIgnoreUseStatus(isSelected)
                fileService.run {
                    getAllProjects().forEach {
                        project -> project.basePath?.also { refreshGitKeepInAllSubfolder(project, it) }
                    }
                }
            }
            fileService.refreshProjectTree()
        }
    }

    override fun getDisplayName() = "Auto Gitkeep"

}
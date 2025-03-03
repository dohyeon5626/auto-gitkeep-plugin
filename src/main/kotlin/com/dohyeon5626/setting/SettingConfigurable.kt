package com.dohyeon5626.setting

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingStateComponent
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
    private val settingStateComponent = service<SettingStateComponent>()
    private val application = ApplicationManager.getApplication()
    private val panel = JPanel(BorderLayout())

    private val autoCreateStatusCheckBox = JCheckBox("Automatically creates a .gitkeep file in all empty folders of the project.")
    private val visibleCheckBox = JCheckBox("Show .gitkeep")
    private val gitIgnoreUseStatusCheckBox = JCheckBox("Create .gitkeep in .gitignore path")

    init {
        panel.apply {
            layout = GridLayout(25, 1)
            add(autoCreateStatusCheckBox)
            add(visibleCheckBox)
            add(gitIgnoreUseStatusCheckBox)
        }
    }

    override fun createComponent(): JComponent {
        settingStateComponent.apply {
            autoCreateStatusCheckBox.isSelected = state.autoCreateStatus
            visibleCheckBox.isSelected = state.visible
            gitIgnoreUseStatusCheckBox.isSelected = state.gitIgnoreUseStatus
        }
        return panel
    }

    override fun isModified() = with(settingStateComponent) {
        autoCreateStatusCheckBox.isSelected != state.autoCreateStatus ||
                visibleCheckBox.isSelected != state.visible ||
                gitIgnoreUseStatusCheckBox.isSelected != state.gitIgnoreUseStatus
    }

    override fun apply() {
        application.runWriteAction {
            settingStateComponent.state.visible = visibleCheckBox.isSelected
            settingStateComponent.state.gitIgnoreUseStatus = gitIgnoreUseStatusCheckBox.isSelected
            settingStateComponent.state.autoCreateStatus = autoCreateStatusCheckBox.isSelected

            visibleCheckBox.apply {
                fileService.run {
                    if (isSelected) refreshGitKeepVirtualFile()
                    else deleteGitKeepVirtualFile()
                }
            }
            autoCreateStatusCheckBox.apply {
                if (isSelected) {
                    fileService.run { getAllProjects().forEach {
                            project -> project.basePath?.also { refreshGitKeepInAllSubfolder(project, it) }
                    }}
                }
            }
            fileService.refreshProjectTree()
        }
    }

    override fun getDisplayName() = "Auto Gitkeep"

}
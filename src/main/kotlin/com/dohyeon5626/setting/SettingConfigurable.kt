package com.dohyeon5626.setting

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class SettingConfigurable: Configurable {

    private val fileService = service<FileService>()
    private val settingComponent = service<SettingComponent>()
    private val application = ApplicationManager.getApplication()
    private val panel = JPanel(BorderLayout())
    private val checkBox = JCheckBox("show .gitkeep")

    init {
        panel.add(checkBox, BorderLayout.NORTH)
    }

    override fun createComponent(): JComponent {
        checkBox.isSelected = settingComponent.getVisible()
        return panel
    }

    override fun isModified() = checkBox.isSelected != settingComponent.getVisible()

    override fun apply() {
        application.runWriteAction {
            checkBox.apply {
                settingComponent.updateVisible(isSelected)
                if (isSelected)
                    fileService.refreshGitKeepVirtualFile()
                else fileService.deleteGitKeepVirtualFile()
            }
            fileService.refreshProjectTree()
        }
    }

    override fun getDisplayName() = "Auto Gitkeep"

}
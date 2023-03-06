package com.dohyeon5626.setting

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.VisibleSettingComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class VisibleSettingConfigurable: Configurable {

    private val fileService = service<FileService>()
    private val visibleSettingComponent = service<VisibleSettingComponent>()
    private val application = ApplicationManager.getApplication()
    private val panel = JPanel(BorderLayout())
    private val checkBox = JCheckBox("show .gitkeep")

    init {
        panel.add(checkBox, BorderLayout.NORTH)
    }

    override fun createComponent(): JComponent {
        checkBox.isSelected = visibleSettingComponent.getVisible()
        return panel
    }

    override fun isModified() = checkBox.isSelected != visibleSettingComponent.getVisible()

    override fun apply() {
        application.runWriteAction {
            checkBox.apply {
                visibleSettingComponent.updateVisible(isSelected)
                if (isSelected)
                    fileService.refreshGitKeepVirtualFile()
                else fileService.deleteGitKeepVirtualFile()
            }
            fileService.refreshProjectTree()
        }
    }

    override fun getDisplayName() = "Auto Gitkeep"

}
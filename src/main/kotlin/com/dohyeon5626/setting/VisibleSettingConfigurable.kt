package com.dohyeon5626.setting

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.VisibleSettingComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class VisibleSettingConfigurable: Configurable {

    private val fileService = service<FileService>()
    private val visibleSettingComponent = service<VisibleSettingComponent>()
    private val panel = JPanel(BorderLayout())
    private val checkBox = JCheckBox("show .gitkeep")

    init {
        panel.add(checkBox, BorderLayout.NORTH)
    }

    override fun createComponent(): JComponent {
        checkBox.isSelected = visibleSettingComponent.state
        return panel
    }

    override fun isModified() = checkBox.isSelected != visibleSettingComponent.state

    override fun apply() {
        visibleSettingComponent.updateState(checkBox.isSelected)
        fileService.refreshVirtualFileList()
    }

    override fun getDisplayName() = "Auto Gitkeep"

}
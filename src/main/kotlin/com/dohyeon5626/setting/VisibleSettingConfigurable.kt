package com.dohyeon5626.setting

import com.dohyeon5626.service.VisibleSettingService
import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class VisibleSettingConfigurable: Configurable {

    private val visibleSettingService = service<VisibleSettingService>()
    private val panel = JPanel(BorderLayout())
    private val checkBox = JCheckBox("show .gitkeep")

    init {
        panel.add(checkBox, BorderLayout.NORTH)
    }

    override fun createComponent(): JComponent {
        checkBox.isSelected = visibleSettingService.state
        return panel
    }

    override fun isModified(): Boolean {
        return checkBox.isSelected != visibleSettingService.state
    }

    override fun apply() {
        visibleSettingService.updateState(checkBox.isSelected)
    }

    override fun getDisplayName() = "Auto Gitkeep"

}
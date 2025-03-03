package com.dohyeon5626.service

import com.intellij.ide.util.PropertiesComponent

class SettingComponent {

    private val propertiesComponent = PropertiesComponent.getInstance()

    fun getVisible() = propertiesComponent.getBoolean("visible")

    fun updateVisible(visible: Boolean) {
        propertiesComponent.setValue("visible", visible)
    }

    fun getGitIgnoreUseStatus() = propertiesComponent.getBoolean("gitIgnoreUseStatus")

    fun updateGitIgnoreUseStatus(gitIgnoreUseStatus: Boolean) {
        propertiesComponent.setValue("gitIgnoreUseStatus", gitIgnoreUseStatus)
    }

}
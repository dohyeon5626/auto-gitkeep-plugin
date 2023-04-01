package com.dohyeon5626.service.impl

import com.dohyeon5626.service.SettingComponent
import com.intellij.ide.util.PropertiesComponent

class SettingComponentImpl: SettingComponent {

    private val propertiesComponent = PropertiesComponent.getInstance()

    override fun getVisible(): Boolean = propertiesComponent.getBoolean("visible")

    override fun updateVisible(visible: Boolean) {
        propertiesComponent.setValue("visible", visible)
    }

}
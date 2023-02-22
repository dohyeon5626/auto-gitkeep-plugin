package com.dohyeon5626.service.impl

import com.dohyeon5626.service.VisibleSettingComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "gitkeepVisible", storages = [Storage(StoragePathMacros.CACHE_FILE)])
class VisibleSettingComponentImpl: VisibleSettingComponent {

    private var visible = false

    override fun getState() = visible

    override fun loadState(state: Boolean) {
        XmlSerializerUtil.copyBean(state, visible)
        visible = state
    }

    override fun updateState(state: Boolean) {
        visible = state
    }

}
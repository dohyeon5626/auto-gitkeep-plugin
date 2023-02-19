package com.dohyeon5626.service.impl

import com.dohyeon5626.service.VisibleSettingService
import com.intellij.util.xmlb.XmlSerializerUtil

class VisibleSettingServiceImpl: VisibleSettingService {

    private var visible = false

    override fun getState(): Boolean = visible

    override fun loadState(state: Boolean) {
        XmlSerializerUtil.copyBean(state, visible)
        visible = state
    }

    override fun updateState(state: Boolean) {
        visible = state
    }

}
package com.dohyeon5626.service

import com.intellij.openapi.components.PersistentStateComponent

interface VisibleSettingComponent: PersistentStateComponent<Boolean> {

    fun updateState(state: Boolean)
    override fun getState(): Boolean

}
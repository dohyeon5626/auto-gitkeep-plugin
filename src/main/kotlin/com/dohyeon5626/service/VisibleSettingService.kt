package com.dohyeon5626.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros

@State(name = "gitkeepVisible", storages = [Storage(StoragePathMacros.CACHE_FILE)])
interface VisibleSettingService: PersistentStateComponent<Boolean> {

    fun updateState(state: Boolean)
    override fun getState(): Boolean

}
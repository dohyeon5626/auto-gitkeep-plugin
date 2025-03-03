package com.dohyeon5626.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "SettingStateComponent", storages = [Storage("autoGitKeepPlugin.xml")])
class SettingStateComponent : PersistentStateComponent<SettingStateComponent.State> {

    data class State(
        var autoCreateStatus: Boolean = false,
        var visible: Boolean = true,
        var gitIgnoreUseStatus: Boolean = false
    )

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

}
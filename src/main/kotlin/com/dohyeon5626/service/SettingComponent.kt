package com.dohyeon5626.service

interface SettingComponent {

    fun getVisible(): Boolean
    fun updateVisible(visible: Boolean)
    fun getGitIgnoreUseStatus(): Boolean
    fun updateGitIgnoreUseStatus(gitIgnoreUseStatus: Boolean)

}
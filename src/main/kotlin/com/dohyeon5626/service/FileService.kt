package com.dohyeon5626.service

interface FileService {

    fun generateGitKeep(path: String)
    fun generateGitKeepInAllSubfolder(path: String)
    fun refreshVirtualFileList()

}
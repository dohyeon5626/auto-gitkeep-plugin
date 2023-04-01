package com.dohyeon5626.service

import com.intellij.openapi.project.Project

interface FileService {

    fun refreshGitKeep(project: Project, path: String)
    fun refreshGitKeepInAllSubfolder(project: Project, path: String)
    fun deleteGitKeepVirtualFile()
    fun refreshGitKeepVirtualFile()
    fun refreshProjectTree()
    fun refreshGitIgnorePath(project: Project)
    fun getProject(path: String): Project?
    fun getAllProjects(): List<Project>

}
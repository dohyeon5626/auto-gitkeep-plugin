package com.dohyeon5626.service

import com.github.arturopala.gitignore.GitIgnore
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import scala.collection.JavaConverters
import java.io.File

class FileService {

    private val fileSystem = LocalFileSystem.getInstance()
    private val settingStateComponent = service<SettingStateComponent>()
    private val projectManager = ProjectManager.getInstance()
    private var gitignoreMap = mutableMapOf<Project, GitIgnore>()

    fun refreshGitKeep(project: Project, path: String) {
        path.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    if (settingStateComponent.state.gitIgnoreUseStatus || (!isPathGitIgnore(project, path) && !isPathGitIgnore(project, "$path/.gitkeep")))
                        createFile("$path/.gitkeep")
                    else filter { it.isGitKeep() }.forEach { deleteFile(it) }
                } else if (any { !it.isGitKeep()}) {
                    filter { it.isGitKeep() }.forEach { deleteFile(it) }
                } else if (!settingStateComponent.state.gitIgnoreUseStatus && (isPathGitIgnore(project, path) || isPathGitIgnore(project, "$path/.gitkeep"))) {
                    filter { it.isGitKeep() }.forEach { deleteFile(it) }
                }
            }
    }

    fun refreshGitKeepInAllSubfolder(project: Project, path: String) {
        path.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    if (settingStateComponent.state.gitIgnoreUseStatus || (!isPathGitIgnore(project, path) && !isPathGitIgnore(project, "$path/.gitkeep")))
                        createFile("$path/.gitkeep")
                    else
                        filter { it.isGitKeep() }.forEach { deleteFile(it) }
                } else if (any { !it.isGitKeep()}) {
                    filter { it.isGitKeep() }.forEach { deleteFile(it) }
                } else if (!settingStateComponent.state.gitIgnoreUseStatus && (isPathGitIgnore(project, path) || isPathGitIgnore(project, "$path/.gitkeep"))) {
                    filter { it.isGitKeep() }.forEach { deleteFile(it) }
                }
                filter { file -> file.isDirectory }.forEach { file -> refreshGitKeepInAllSubfolder(project, file.path) }
            }
    }

    fun deleteGitKeepInAllSubfolder(path: String) {
        findGitKeep(path).forEach { deleteFile(it) }
    }

    fun refreshProjectTree() {
        projectManager.openProjects.forEach { ProjectView.getInstance(it).refresh() }
    }

    fun refreshGitIgnorePath(project: Project) {
        project.basePath.let { "$it/.gitignore" }
            .let { File(it) }.takeIf { file -> file.exists() }?.readLines()
            ?.filter { line -> !line.startsWith("#") && line.replace(" ", "") != "" }
            ?.let { JavaConverters.asScalaIteratorConverter(it.iterator()).asScala().toSeq() }
            ?.also { gitignoreMap[project] = GitIgnore(it) }
    }

    fun getProject(path: String) = projectManager.openProjects
        .filter { project -> project.basePath ?.let { path.contains(it) } ?: false }
        .getOrNull(0)

    fun getAllProjects() = projectManager.openProjects.asList()

    private fun findGitKeep(path: String): List<File> {
        val gitKeepFileList = mutableListOf<File>()
        path.let { File(it).listFiles() }
            ?.apply {
                gitKeepFileList.addAll(filter { it.isGitKeep() })
                filter { file -> file.isDirectory }
                    .map { findGitKeep(it.path) }
                    .forEach { gitKeepFileList.addAll(it) }
            }
        return gitKeepFileList
    }

    private fun createFile(path: String) {
        File(path).apply { createNewFile() }.also { refreshFilePath(it) }
    }

    private fun deleteFile(file: File) {
        file.apply { delete() }.also { refreshFilePath(it) }
    }

    private fun refreshFilePath(file: File) {
        if (settingStateComponent.state.visible)
            fileSystem.refreshAndFindFileByIoFile(file)?.refresh(false, false)
    }

    private fun isPathGitIgnore(project: Project, path: String): Boolean = gitignoreMap[project]?.isIgnored(path) ?: false

    private fun File.isGitKeep() = name == ".gitkeep"

}
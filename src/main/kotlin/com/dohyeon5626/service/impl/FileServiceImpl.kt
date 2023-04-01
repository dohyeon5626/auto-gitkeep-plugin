package com.dohyeon5626.service.impl

import com.dohyeon5626.service.FileService
import com.dohyeon5626.service.SettingComponent
import com.github.arturopala.gitignore.GitIgnore
import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.LocalFileSystem
import scala.collection.JavaConverters
import java.io.File

class FileServiceImpl: FileService {

    private val fileSystem = LocalFileSystem.getInstance()
    private val settingComponent = service<SettingComponent>()
    private val projectManager = ProjectManager.getInstance()
    private var gitignoreMap = mutableMapOf<Project, GitIgnore>()

    override fun refreshGitKeep(project: Project, path: String) {
        path.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    if (!isPathGitIgnore(project, path) && !isPathGitIgnore(project, "$path/.gitkeep"))
                        createFile("$path/.gitkeep")
                    else
                        filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                } else if (any {it.name != ".gitkeep"}) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                } else if (isPathGitIgnore(project, path) || isPathGitIgnore(project, "$path/.gitkeep")) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                }
            }
    }

    override fun refreshGitKeepInAllSubfolder(project: Project, path: String) {
        path.let { File(it).listFiles() }
            ?.apply {
                if (isEmpty()) {
                    if (!isPathGitIgnore(project, path) && !isPathGitIgnore(project, "$path/.gitkeep"))
                        createFile("$path/.gitkeep")
                    else
                        filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                } else if (any {it.name != ".gitkeep"}) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                } else if (isPathGitIgnore(project, path) || isPathGitIgnore(project, "$path/.gitkeep")) {
                    filter { it.name == ".gitkeep" }.forEach { deleteFile(it) }
                }
                filter { it.isDirectory }.forEach { refreshGitKeepInAllSubfolder(project, it.path) }
            }
    }

    override fun deleteGitKeepVirtualFile() {
        projectManager.openProjects.forEach {
            it.basePath?.also {
                findGitKeep(it).forEach {
                    fileSystem.refreshAndFindFileByIoFile(it)?.delete(this)
                }
            }
        }
    }

    override fun refreshGitKeepVirtualFile() {
        projectManager.openProjects.forEach {
            it.basePath?.also {
                findGitKeep(it).forEach {
                    fileSystem.refreshAndFindFileByIoFile(it)?.refresh(false, false)
                }
            }
        }
    }

    override fun refreshProjectTree() {
        projectManager.openProjects.forEach {
            ProjectView.getInstance(it).refresh()
        }
    }

    override fun refreshGitIgnorePath(project: Project) {
        project.basePath.let { "$it/.gitignore" }
            .let { File(it) }.takeIf { it.exists() }?.readLines()
            ?.filter { !it.startsWith("#") && it.replace(" ", "") != "" }
            ?.let { JavaConverters.asScalaIteratorConverter(it.iterator()).asScala().toSeq() }
            ?.also { gitignoreMap[project] = GitIgnore(it) }
    }

    override fun getProject(path: String): Project? =
        projectManager.openProjects
            .filter { it.basePath ?.let { path.contains(it) } ?: false }
            .getOrNull(0)

    private fun findGitKeep(path: String): List<File> {
        val gitKeepFileList = mutableListOf<File>()
        path.let { File(it).listFiles() }
            ?.apply {
                gitKeepFileList.addAll(filter { it.name == ".gitkeep" })
                filter { it.isDirectory }.map { findGitKeep(it.path) }
                    .forEach {
                        gitKeepFileList.addAll(it)
                    }
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
        if (settingComponent.getVisible())
            fileSystem.refreshAndFindFileByIoFile(file)?.refresh(false, false)
    }

    private fun isPathGitIgnore(project: Project, path: String): Boolean = gitignoreMap[project]?.isIgnored(path) ?: false

}
package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider.Companion.contains
import java.io.File

class FileChangeListener: BulkFileListener {

    private val fileService = service<FileService>()

    override fun after(events: MutableList<out VFileEvent>) {
        events.run {
            filter { it.path.endsWith(".gitignore") }
                .forEach {
                    fileService.run {
                        getProject(it.path).also {
                            refreshGitIgnorePath(it)
                            it.basePath?.apply { refreshGitKeepInAllSubfolder(it, this) }
                        }
                    }
                }

            filter { !it.isFromRefresh && !it.isFromSave }
                .forEach {
                    it.apply {
                        fileService.apply {
                            if (File(path).isDirectory) {
                                getProject(path).also {
                                    refreshGitKeep(it, path)
                                    refreshGitKeep(it, getParentPath(path))
                                }
                            } else refreshGitKeep(getProject(path), getParentPath(path))
                        }
                    }
                }
        }
    }

    private fun getParentPath(path: String) =
        path.substring(0, path.lastIndexOf("/"))

}
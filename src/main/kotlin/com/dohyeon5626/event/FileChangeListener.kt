package com.dohyeon5626.event

import com.dohyeon5626.service.FileService
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import java.io.File

class FileChangeListener: BulkFileListener {

    private val fileService = service<FileService>()

    override fun after(events: MutableList<out VFileEvent>) {
        events.filter { !it.isFromRefresh && !it.isFromSave }
            .forEach {
                it.apply {
                    if(File(path).isDirectory) {
                        fileService.apply {
                            generateGitKeep(path)
                            generateGitKeep(getParentPath(path))
                        }
                    } else fileService.generateGitKeep(getParentPath(path))
                }
            }
    }

    private fun getParentPath(path: String) =
        path.substring(0, path.lastIndexOf("/"))

}
package com.dohyeon5626.listener

import com.dohyeon5626.service.FileService
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

class FileListener: BulkFileListener {

    private val fileService = service<FileService>()

    override fun after(events: MutableList<out VFileEvent>) {
        events.forEach {
                event -> if (!event.isFromRefresh && !event.isFromSave)
                    fileService.generateGitKeep(getParentPath(event.path))
        }
        super.after(events)
    }

    private fun getParentPath(path: String) =
        path.substring(0, path.lastIndexOf("/"))

}
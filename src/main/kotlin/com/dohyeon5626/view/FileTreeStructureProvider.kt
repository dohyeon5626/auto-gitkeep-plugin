package com.dohyeon5626.view

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import kotlin.streams.toList

class FileTreeStructureProvider: TreeStructureProvider {

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        settings: ViewSettings?
    ): List<AbstractTreeNode<*>> {
        return children.stream()
            .filter {
                !(it is PsiFileNode && with(it.virtualFile) { this != null && name == ".gitkeep" })
            }.toList()
    }

}
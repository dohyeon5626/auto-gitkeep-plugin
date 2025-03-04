package com.dohyeon5626.view

import com.dohyeon5626.service.SettingStateComponent
import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.components.service

class FileTreeStructureProvider: TreeStructureProvider {

    private val settingStateComponent = service<SettingStateComponent>()

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        settings: ViewSettings?
    ): Collection<AbstractTreeNode<*>> =
        if(!settingStateComponent.state.visible) children.filter { !(it is PsiFileNode && with(it.virtualFile) { this != null && name == ".gitkeep" }) }
        else children

}
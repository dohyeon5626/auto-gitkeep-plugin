package com.dohyeon5626.view

import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.ide.projectView.PresentationData
import com.intellij.openapi.util.IconLoader
import com.intellij.packageDependencies.ui.PackageDependenciesNode
import com.intellij.psi.PsiFile
import com.intellij.ui.ColoredTreeCellRenderer

class FileProjectViewNodeDecorator : ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        val psiElement = node.value
        if (psiElement is PsiFile && psiElement.name == ".gitkeep") {
            data.setIcon(IconLoader.getIcon("/icon/gitkeepFileIcon.svg", javaClass))
        }
    }

    override fun decorate(p0: PackageDependenciesNode?, p1: ColoredTreeCellRenderer?) {}

}
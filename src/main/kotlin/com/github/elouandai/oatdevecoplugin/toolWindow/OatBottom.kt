package com.github.elouandai.oatdevecoplugin.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.Font
import javax.swing.JScrollPane
import javax.swing.JTextArea

class OatBottom : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val textArea = JTextArea()
        textArea.isEditable = false
        val font = Font("JetBrains Mono", Font.ITALIC, 15)
        textArea.font = font
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        val scrollPane: JScrollPane = JBScrollPane(textArea)

        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(scrollPane, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
package com.github.elouandai.oatdevecoplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.ToolWindowManager
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import javax.swing.JScrollPane
import javax.swing.JTextArea

class OatCheck : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)

        val actionId = e.actionManager.getId(this)
        var nameOfRepo = ""
        if ("PluginOAT.SelfAction" == actionId) {
            nameOfRepo = "nameOfRepo"
        } else if ("PluginOAT.ThirdPartyAction" == actionId) {
            nameOfRepo = "third_party"
        }

        if (file != null) {
            val path = file.path

            val jarPath = "D:\\Elouan\\Research\\OpenSourceCompliance\\OatPackage\\ohos_ossaudittool-2.0.0.jar"
            val command = "java -jar $jarPath -mode s -s $path -r $path -n $nameOfRepo"

            try {
                val process = Runtime.getRuntime().exec(command)
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var output = StringBuilder()

                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    output.append(line).append("\n")
                }

                process.waitFor()
                reader.close()

                val toolWindow = ToolWindowManager.getInstance(e.project!!).getToolWindow("OATCheckLog")
                val content = toolWindow!!.contentManager.getContent(0)

                if (content != null) {
                    val scrollPane = content.component as JScrollPane
                    val textArea = scrollPane.viewport.view as JTextArea
                    textArea.text = output.toString()
                }

                output = StringBuilder()
                val txtFilePath = "$path\\single\\PlainReport_$nameOfRepo.txt"
                appendTextFromFile(output, txtFilePath)

                val toolWindow2 = ToolWindowManager.getInstance(e.project!!).getToolWindow("OATCheckResult")
                val content2 = toolWindow2!!.contentManager.getContent(0)

                if (content2 != null) {
                    val scrollPane = content2.component as JScrollPane
                    val textArea = scrollPane.viewport.view as JTextArea
                    textArea.text = output.toString()
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        }
    }

    private fun appendTextFromFile(output: StringBuilder, filePath: String) {
        try {
            BufferedReader(FileReader(filePath)).use { reader ->
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    output.append(line).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            output.append("Failed to read file: ").append(filePath)
        }
    }
}
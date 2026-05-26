package com.github.mikrzo.cucumbergo

import com.goide.psi.GoCallExpr
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import kotlin.collections.joinToString

class StepUtils {
    companion object {
        fun checkIdentifierName(name: String): Boolean {
            return name == "Step" || name == "Given" || name == "When" || name == "Then"
        }
    }

}

/** Returns the step pattern text, or null if [argument] is not a recognised
 *  step pattern (string literal or regexp.MustCompile/Compile call). */
fun extractStepPattern(argument: PsiElement?): String? {
    if (argument == null) return null
    if (argument is GoCallExpr) {
        val funcText = argument.children.getOrNull(0)?.text
        if (funcText == "regexp.MustCompile" || funcText == "regexp.Compile") {
            return extractStepPattern(argument.argumentList.expressionList.getOrNull(0))
        }
        return null
    }
    return when {
        argument.text.startsWith("`")  -> argument.text.removeSurrounding("`").replace("\\\\", "\\")
        argument.text.startsWith("\"") -> argument.text.removeSurrounding("\"").replace("\\\\", "\\")
        else -> argument.text
    }
}

fun <T> inReadAction(body: () -> T): T {
    return ApplicationManager.getApplication().run {
        if (isReadAccessAllowed) {
            body()
        } else runReadAction<T>(body)
    }
}

fun toPascalCase(s: String): String {
    return s
        .split(" ")
        .joinToString("") { s ->
            s.replaceFirstChar { ch ->
                ch.uppercase()
            }
        }
}
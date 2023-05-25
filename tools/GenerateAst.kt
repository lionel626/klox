import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

@Throws(IOException::class)
private fun defineAst(
    outputDir: String, baseName: String, types: List<String>
) {
    val path = "$outputDir/$baseName.kt"
    val writer = PrintWriter(File(path))
    writer.println("package com.craftinginterpreters.lox")
    writer.println()
    writer.println("import java.util.*")
    writer.println()
    writer.println("abstract class $baseName {")

    defineVisitor(writer, baseName, types)

    // The AST classes.
    for (type in types) {
        val className = type.split("->")[0].trim()
        val fields = type.split("->")[1].trim()
        defineType(writer, baseName, className, fields)
    }

    // The base accept() method.
    writer.println()
    writer.println("    abstract fun <R> accept(visitor: Visitor<R>): R")

    writer.println("}")
    writer.close()
}

private fun defineVisitor(
    writer: PrintWriter, baseName: String, types: List<String>
) {
    writer.println("    interface Visitor<R> {")
    for (type: String in types) {
        val typeName = type.split("->")[0].trim()
        writer.println(
            "        fun visit" + typeName + baseName + "(" +
                    baseName.lowercase(Locale.getDefault()) + ": " + typeName + "): R"
        )
    }
    writer.println("    }")
}

private fun defineType(
    writer: PrintWriter, baseName: String,
    className: String, fieldList: String
) {
    writer.println(
        "    data class " + className + "(" +
                fieldList + ") : " + baseName + " {"
    )

    // Visitor pattern.
    writer.println()
    writer.println("        override fun <R> accept(visitor: Visitor<R>): R {")
    writer.println("            return visitor.visit" +
            className + baseName + "(this)")
    writer.println("        }")

    writer.println("    }")
}

fun main(args: Array<String>) {

    val path = Paths.get("").toAbsolutePath().toString()
    val outputDir = path
    defineAst(outputDir, "Expr", Arrays.asList(
        "Binary   -> left : Expr, operator : Token, right : Expr",
        "Grouping -> expression : Expr",
        "Literal  -> value : Object",
        "Unary    -> operator : Token, right : Expr"
    ));
}

package com.craftinginterpreters.lox

import com.craftinginterpreters.lox.lox.Expr


internal class AstPrinter : Expr.Visitor<String?> {
    fun print(expr: Expr): String? {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String? {
        return parenthesize(
            expr.operator.lexeme,
            expr.left, expr.right
        )
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String? {
        return parenthesize("group", expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String? {
        return if (expr.value == null) "nil" else expr.value.toString()
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String? {
        return parenthesize(expr.operator.lexeme, expr.right)
    }

    override fun visitTernaryExpr(ternary: Expr.Ternary): String? {
        return ("ternary_if(" + ternary.left.accept(this)
            + ", "
            + ternary.middle.accept(this)
            + " ," + ternary.right.accept(this) + " )")
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String? {
        val builder = StringBuilder()
        builder.append("(").append(name)
        for (expr in exprs) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }
        builder.append(")")
        return builder.toString()
    }

}

internal class RpnAstPrinter : Expr.Visitor<String?> {
    fun print(expr: Expr): String? {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): String? {
        return parenthesize(
            expr.operator.lexeme,
            expr.left, expr.right
        )
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): String? {
        return parenthesize("group", expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): String? {
        return if (expr.value == null) "nil" else expr.value.toString()
    }

    override fun visitUnaryExpr(expr: Expr.Unary): String? {
        return parenthesize(expr.operator.lexeme, expr.right)
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String? {
        val builder = StringBuilder()
        builder.append("(").append(name)
        for (expr in exprs) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }
        builder.append(")")
        return builder.toString()
    }
    override fun visitTernaryExpr(ternary: Expr.Ternary): String? {
        return ("ternary_if(" + ternary.left.accept(this)
                + ", "
                + ternary.middle.accept(this)
                + " ," + ternary.right.accept(this) + " )")
    }

}

fun main(args: Array<String>) {
    val expression: Expr = Expr.Binary(
        Expr.Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Expr.Literal(123)
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(
            Expr.Literal(45.67)
        )
    )

    println(AstPrinter().print(expression))
}
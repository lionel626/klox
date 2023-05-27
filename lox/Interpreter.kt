package com.craftinginterpreters.lox.lox

import com.craftinginterpreters.lox.Token
import com.craftinginterpreters.lox.TokenType
import kotlin.math.exp


class RuntimeError(val token: Token, message: String?) : RuntimeException(message)
internal class Interpreter : Expr.Visitor<Any?> {

    fun interpret(expression: Expr?) {
        try {
            val value = evaluate(expression!!)
            println(stringify(value))
        } catch (error: RuntimeError) {
            com.craftinginterpreters.lox.runtimeError(error)
        }
    }

    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }
    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        when (expr.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperands(expr.operator, left, right);
                return left as Double - right as Double
            }
            TokenType.PLUS -> {
                if (left is Double && right is Double) {
                    return left + right
                }

                if (left is String && right is String) {
                    return left + right
                }
                throw RuntimeError(
                    expr.operator,
                    "Operands must be two numbers or two strings."
                )
            }
            TokenType.SLASH -> {
                checkNumberOperands(expr.operator, left, right); return left as Double / right as Double
            }
            TokenType.STAR -> {
                checkNumberOperands(expr.operator, left, right); return left as Double * right as Double
            }
            TokenType.GREATER -> {
                checkNumberOperands(expr.operator, left, right);
                return left as Double > right as Double
            }
            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right); return left as Double >= right as Double
            }
            TokenType.LESS -> {
                checkNumberOperands(expr.operator, left, right); return (left as Double) < (right as Double)
            }
            TokenType.LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right); return left as Double <= right as Double
            }
            TokenType.BANG_EQUAL -> return !isEqual(left,right)
            TokenType.EQUAL_EQUAL -> return isEqual(left,right)
            else -> null
        }

        // Unreachable.
        return null
    }

    private fun checkNumberOperands(
        operator: Token,
        left: Any?, right: Any?
    ) {
        if (left is Double && right is Double) return
        throw RuntimeError(operator, "Operands must be numbers.")
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        return if (a == null) false else a == b
    }

    private fun stringify(`object`: Any?): String? {
        if (`object` == null) return "nil"
        if (`object` is Double) {
            var text = `object`.toString()
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            return text
        }
        return `object`.toString()
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? {
        return expr.expression.accept(this)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? {
        return expr.value
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)

        when (expr.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperand(expr.operator, right);
                return -(right as Double)
            }
            TokenType.BANG -> !isTruthy(right)
            else -> null
        }

        // Unreachable.

        // Unreachable.
        return null
    }

    private fun checkNumberOperand(operator: Token, operand: Any?) {
        if (operand is Double) return
        throw RuntimeError(operator, "Operand must be a number.")
    }

    private fun isTruthy(`object`: Any?): Boolean {
        if (`object` == null) return false
        return if (`object` is Boolean) `object` else true
    }

    override fun visitTernaryExpr(ternary: Expr.Ternary): Any? {
        TODO("Not yet implemented")
    }

}
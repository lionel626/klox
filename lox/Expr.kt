package com.craftinginterpreters.lox.lox

import com.craftinginterpreters.lox.Token


abstract class Expr {
    interface Visitor<R> {
        fun visitBinaryExpr(expr: Binary): R
        fun visitGroupingExpr(expr: Grouping): R
        fun visitLiteralExpr(expr: Literal): R
        fun visitUnaryExpr(expr: Unary): R
        fun visitTernaryExpr(ternary: Ternary): R
    }
    data class Binary(val left : Expr, val  operator : Token, val  right : Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }
    }
    data class Grouping(val expression : Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitGroupingExpr(this)
        }
    }
    data class Literal(val value : Any?) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }
    }
    data class Unary(val operator : Token, val  right : Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }
    }

    data class Ternary(val left : Expr, val  operator : Token, val  middle : Expr, val operator1: Token, val  right : Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitTernaryExpr(this)
        }
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}

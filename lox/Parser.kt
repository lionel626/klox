package com.craftinginterpreters.lox

import com.craftinginterpreters.lox.lox.Expr


class Parser(tokens:List<Token>) {
    final val tokens = tokens;
    private var current : Int = 0;

    private class ParseError : RuntimeException()


    private fun expression() : Expr {
        return equality();
    }

    private fun equality() : Expr {
        var expr = comparaison()

        while (match(TokenType.BANG_EQUAL,TokenType.EQUAL_EQUAL)) {
            val operator = previous();
            val right = comparaison();
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun comparison(): Expr {
        var expr: Expr = term()
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right: Expr = term()
            expr = Expr.Binary(expr, operator!!, right)
        }
        return expr
    }

    private fun term(): Expr {
        var expr: Expr = factor()
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right: Expr = factor()
            expr = Expr.Binary(expr, operator!!, right)
        }
        return expr
    }

    private fun factor(): Expr {
        var expr: Expr = unary()
        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator!!, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator!!, right)
        }
        return primary()
    }

    private fun primary(): Expr? {
        if (match(TokenType.FALSE)) return Expr.Literal(false)
        if (match(TokenType.TRUE)) return Expr.Literal(true)
        if (match(TokenType.NIL)) return Expr.Literal(null)
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return Expr.Literal(previous()!!.literal!!)
        }
        if (match(TokenType.LEFT_PAREN)) {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }
        return Expr.Literal(0);
    }

    private fun consume(type: TokenType, message: String): Token? {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun error(token: Token, message: String): ParseError? {
        error(token, message)
        return ParseError()
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean {
        return if (isAtEnd()) false else peek().type === type
    }

    private fun advance(): Token? {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type === EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token? {
        return tokens[current - 1]
    }


}
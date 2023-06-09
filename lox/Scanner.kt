package com.craftinginterpreters.lox



class Scanner(source: String) {
    final val source: String = source
    final val tokens: MutableList<Token> = mutableListOf()
    final var start = 0
    final var current = 0
    final var line = 0

    

    companion object {
        private final val keywords : Map<String, TokenType> = mapOf(
            "and" to TokenType.AND,
            "class" to TokenType.CLASS,
            "else" to TokenType.ELSE,
            "false" to TokenType.FALSE,
            "for" to TokenType.FOR,
            "fun" to TokenType.FUN,
            "if" to     TokenType.IF,
            "nil" to    TokenType.NIL,
            "or" to     TokenType.OR,
            "print" to  TokenType.PRINT,
            "return" to TokenType.RETURN,
            "super" to  TokenType.SUPER,
            "this" to   TokenType.THIS,
            "true" to   TokenType.TRUE,
            "var" to    TokenType.VAR,
            "while" to  TokenType.WHILE,
        );
        
    }

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))

        return tokens
    }

    private fun isAtEnd(): Boolean {
        return (current >= source.length)
    }

    private fun scanToken() {
        val c: Char = advance()

        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)
            '?' -> addToken(TokenType.QUESTION_MARK)
            ':' -> addToken(TokenType.DOT_DOT)
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '/' -> {

                if (match('*')) {

                    while (!isAtEnd()) {
                        if (peek()=='*' && peekNext()=='/') {
                            advance()
                            advance()
                            break
                        }
                        advance()
                    }
                } else if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(TokenType.SLASH)
                }
            }
            '"' -> string()
            ' ' -> null
            else -> {
                if (isDigit(c)) {
                    number()
                } else if (isAlpha(c)) {
                    identifier()
                } else {
                    error(line, "Unexpected character.")
                }
            }
        }
    }

    private fun isAlpha(c:Char) : Boolean {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
      }
    
      private fun isAlphaNumeric( c:Char) : Boolean {
        return isAlpha(c) || isDigit(c);
      }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance();
        val text = source.substring(start, current);
        var type = keywords[text];
        if (type == null) type = TokenType.IDENTIFIER;
        addToken(type);
      }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun number() {
        while (isDigit(peek())) advance()

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance()

            while (isDigit(peek())) advance()
        }
        addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
    }

    private fun peekNext() : Char {
        if (current + 1 >= source.length) return Char.MIN_VALUE;
        return source[current + 1];
      }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            error(line, "Unterminated string.")
            return
        }

        // The closing ".
        advance()

        // Trim the surrounding quotes.
        val value: String = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }

    private fun peek(): Char {
        if (isAtEnd()) return Char.MIN_VALUE

        return source[current]
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }
}

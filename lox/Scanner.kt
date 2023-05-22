package com.craftinginterpreters.lox;

class Scanner(source : String) {
    final val source : String = source;
    final val tokens : MutableList<Token> = mutableListOf();
    final val start = 0;
    final val current = 0;
    final val line = 0;


    fun scanTokens() : List<Token> {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(token);

        return tokens;
    }

    private fun isAtEnd() : Boolean {
        return (current >= source.length);
    }

    private fun scanToken() {
        val c : Char = advance();

        when(c) {
            '(' ->  addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
        }

    }
}
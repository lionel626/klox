package com.craftinginterpreters.lox;

class Token(type : TokenType,lexeme : String, literal : Any, line : Int) {
    final val type : TokenType = type;
    final val lexeme : String = lexeme;
    final val literal : Any = literal;
    final val line : Int = line;

}
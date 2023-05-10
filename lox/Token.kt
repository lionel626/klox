package com.craftinginterpreters.lox;

class Token {
    final type : TokenType;
    final lexeme : String;
    final literal : Object;
    final line : Int;

    constructor(type : TokenType,lexeme : String, literal : Object, line : Int) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
}
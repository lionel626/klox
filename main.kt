package com.craftinginterpreters.lox;

fun main(args: Array<String>) {
    if(args.size > 1) {
        println("Usage: jlox [script]");
        System.exit(64);
    } else if (args.size==1) {
        runFile(args[0]);
    } else {
        runPrompt();
    }
}
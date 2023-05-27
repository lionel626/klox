
package com.craftinginterpreters.lox;

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


//
var hadError : Boolean= false;

fun report(line:Int,where:String,message:String) {
    error("[line $line] Error$where:$message");
    hadError = true;
}

fun error(line:Int,message:String) {
    report(line,"",message);
}

fun error(token: Token, message: String?) {
    if (token.type === TokenType.EOF) {
        report(token.line, " at end", message!!)
    } else {
        report(token.line, " at '" + token.lexeme + "'", message!!)
    }
}

fun run(source:String) {
    val scanner : Scanner  = Scanner(source);
    val tokens : List<Token> = scanner.scanTokens();
    val parser = Parser(tokens)
    val expression = parser.parse()

    // Stop if there was a syntax error.

    // Stop if there was a syntax error.
    if (hadError) return

    println(AstPrinter().print(expression!!))
    //for (token in tokens) {
    //    println(token);
    //}
}

fun runFile(path:String) {
    val bytes = Files.readAllBytes(Paths.get(path));
    run(String(bytes,Charsets.UTF_8));
    if (hadError) exitProcess(65);
}

fun runPrompt() {
    val input : InputStreamReader = InputStreamReader(System.`in`);
    val reader : BufferedReader = BufferedReader(input);

    while (true) {
        print("> ");
        val line = reader.readLine();
        if(line==null) break;
        run(line);
        hadError = false;
    }

}
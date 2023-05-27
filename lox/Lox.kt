
package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.lox.Interpreter
import com.craftinginterpreters.lox.lox.RuntimeError
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


//
var hadError : Boolean= false;
var hadRuntimeError : Boolean = false;

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

fun runtimeError(error: RuntimeError) {
    System.err.println(
        """
            ${error.message}
            [line ${error.token.line}]
            """.trimIndent()
    )
    hadRuntimeError = true
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

    val interpreter = Interpreter()
    interpreter.interpret(expression);
}

fun runFile(path:String) {
    val bytes = Files.readAllBytes(Paths.get(path));
    run(String(bytes,Charsets.UTF_8));
    if (hadError) exitProcess(65);
    if (hadRuntimeError) exitProcess(70);
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
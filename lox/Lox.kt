
package com.craftinginterpreters.lox;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStreamReader;
import java.io.BufferedReader;
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

fun run(source:String) {
    val scanner : Scanner  = Scanner(source);
    val tokens : List<Token> = scanner.scanTokens();
    for (token in tokens) {
        println(token);
    }
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
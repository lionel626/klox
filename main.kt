

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.craftinginterpreters.lox.Token

var hadError : Boolean= false;

fun report(line:Int,where:String,message:String) {
    error("[line "+line+"] Error"+ where+":"+message);
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
    if (hadError) System.exit(65);
}

fun runPrompt() {
    val input : InputStreamReader = InputStreamReader(System.in);
    val reader : BufferedReader = BufferedReader(input);

    for (;;) {
        print("> ");
        val line = reader.readLine();
        if(line==null) break;
        run(line);
        hadError = false;
    }

}

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
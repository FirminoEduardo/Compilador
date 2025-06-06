package main;

import lexer.Lexer;
import lexer.Token;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code = """
            program exemplo
            declarations
            varType integer: x, y;
            varType real: z
            endDeclararions
            functions
            funcType integer: soma(x, y) {
                return x + y;
            } endFunction;
            endFunctions
            endProgram
        """;

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}

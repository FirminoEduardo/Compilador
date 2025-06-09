package main;

import lexer.Lexer;
import lexer.Token;
import parser.ParserImpl;

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
            }
            endFunction;
            endFunctions
            endProgram
        """;

        try {
            Lexer lexer = new Lexer(code);
            ParserImpl parser = new ParserImpl(lexer);
            parser.parseFileProgram();
            System.out.println("✔ Código sintaticamente correto!");
        } catch (RuntimeException e) {
            System.err.println("✖ Erro: " + e.getMessage());
        }
    }
}

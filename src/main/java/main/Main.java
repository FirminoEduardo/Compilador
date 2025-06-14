package main;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;
import parser.ParserImpl;
import symboltable.SymbolTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java -jar compilador.jar <arquivo>.251");
            System.exit(1);
        }

        String sourcePath = args[0];
        String lexPath = sourcePath.replaceAll("\\.251$", ".lex");
        String tabPath = sourcePath.replaceAll("\\.251$", ".tab");

        try {
            Lexer lexer = new Lexer(new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(sourcePath))));
            SymbolTable symbolTable = new SymbolTable();

            try (PrintWriter lexOut = new PrintWriter(new File(lexPath))) {
                Token token;
                // Gera arquivo .lex
                while ((token = lexer.nextToken()).getType() != TokenType.EOF) {
                    lexOut.println(token);
                    // para tabela de símbolos, coletamos tokens via parser
                }
            }

            // para análise sintática e tabela
            lexer = new Lexer(new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(sourcePath))));
            ParserImpl parser = new ParserImpl(lexer, symbolTable);
            parser.parseFileProgram();

            try (PrintWriter tabOut = new PrintWriter(new File(tabPath))) {
                symbolTable.printTable(tabOut);
            }

            System.out.println("Arquivos gerados: " + lexPath + ", " + tabPath);

        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + sourcePath);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.exit(1);
        }
    }
}

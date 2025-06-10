package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;
import symboltable.SymbolTable;
import symboltable.SymbolTable.SymbolType;

public class ParserImpl {

    private final Lexer lexer;
    private Token currentToken;
    private final SymbolTable symbolTable;

    public ParserImpl(Lexer lexer) {
        this.lexer = lexer;
        this.symbolTable = new SymbolTable();
        this.currentToken = lexer.nextToken();
    }

    private void eat(TokenType expectedType) {
        if (currentToken.getType() == expectedType) {
            currentToken = lexer.nextToken();
        } else {
            throw new RuntimeException("Esperado token " + expectedType + " mas encontrado " + currentToken.getType());
        }
    }

    public void parseFileProgram() {
        eat(TokenType.PROGRAM);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.DECLARATIONS);
        parseDeclarationList();
        eat(TokenType.END_DECLARATIONS);

        if (currentToken.getType() == TokenType.FUNCTIONS) {
            eat(TokenType.FUNCTIONS);
            parseFunctionList();
            eat(TokenType.END_FUNCTIONS);
        }

        eat(TokenType.END_PROGRAM);
        System.out.println("Programa sintaticamente correto!");
        symbolTable.printTable();
    }

    private void parseDeclarationList() {
        parseDeclarationVar();
        while (currentToken.getType() == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            parseDeclarationVar();
        }
    }

    private void parseDeclarationVar() {
        eat(TokenType.VARTYPE);
        TokenType type = currentToken.getType();
        parseTypeSpecification();
        eat(TokenType.COLON);
        parseVariableList(type);
    }

    private void parseTypeSpecification() {
        switch (currentToken.getType()) {
            case REAL, INTEGER, STRING, BOOLEAN, CHARACTER, VOID -> eat(currentToken.getType());
            default -> throw new RuntimeException("Tipo inválido: " + currentToken.getLexeme());
        }
    }

    private void parseVariableList(TokenType type) {
        declareVariable(type);
        while (currentToken.getType() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            declareVariable(type);
        }
    }

    private void declareVariable(TokenType type) {
        String name = currentToken.getLexeme();
        eat(TokenType.IDENTIFIER);
        if (symbolTable.exists(name)) {
            throw new RuntimeException("Identificador já declarado: " + name);
        }
        symbolTable.declare(name, type.name(), SymbolType.VARIABLE);
    }

    private void parseFunctionList() {
        parseFunctionDeclaration();
        while (currentToken.getType() == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            if (currentToken.getType() == TokenType.FUNCTYPE) {
                parseFunctionDeclaration();
            } else {
                break;
            }
        }
    }

    private void parseFunctionDeclaration() {
        eat(TokenType.FUNCTYPE);
        TokenType returnType = currentToken.getType();
        parseTypeSpecification();
        eat(TokenType.COLON);
        String functionName = currentToken.getLexeme();
        eat(TokenType.IDENTIFIER);
        if (symbolTable.exists(functionName)) {
            throw new RuntimeException("Função já declarada: " + functionName);
        }
        symbolTable.declare(functionName, returnType.name(), SymbolType.FUNCTION);

        eat(TokenType.LPAREN);

        if (currentToken.getType() != TokenType.RPAREN) {
            parseParameterList();
        }

        eat(TokenType.RPAREN);
        parseCommandBlock();
        eat(TokenType.IDENTIFIER);
    }

    private void parseParameterList() {
        TokenType type = currentToken.getType();
        parseTypeSpecification();
        eat(TokenType.COLON);
        String name = currentToken.getLexeme();
        eat(TokenType.IDENTIFIER);
        if (symbolTable.exists(name)) {
            throw new RuntimeException("Parâmetro já declarado: " + name);
        }
        symbolTable.declare(name, type.name(), SymbolType.PARAMETER);

        while (currentToken.getType() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            type = currentToken.getType();
            parseTypeSpecification();
            eat(TokenType.COLON);
            name = currentToken.getLexeme();
            eat(TokenType.IDENTIFIER);
            if (symbolTable.exists(name)) {
                throw new RuntimeException("Parâmetro já declarado: " + name);
            }
            symbolTable.declare(name, type.name(), SymbolType.PARAMETER);
        }
    }

    private void parseCommandBlock() {
        eat(TokenType.LBRACE);
        while (currentToken.getType() != TokenType.RBRACE) {
            currentToken = lexer.nextToken();
        }
        eat(TokenType.RBRACE);
    }

    private void parseCommand() {
        switch (currentToken.getType()) {
            case RETURN -> {
                eat(TokenType.RETURN);
                parseExpression();
                eat(TokenType.SEMICOLON);
            }
            case BREAK -> {
                eat(TokenType.BREAK);
                eat(TokenType.SEMICOLON);
            }
            case PRINT -> {
                eat(TokenType.PRINT);
                parseExpression();
                eat(TokenType.SEMICOLON);
            }
            case IF -> parseIfBlock();
            case WHILE -> parseWhileBlock();
            default -> throw new RuntimeException("Comando inválido: " + currentToken.getLexeme());
        }
    }

    private void parseIfBlock() {
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        parseExpression();
        eat(TokenType.RPAREN);
        parseCommandBlock();

        if (currentToken.getType() == TokenType.ELSE) {
            eat(TokenType.ELSE);
            parseCommandBlock();
        }

        eat(TokenType.ENDIF);
    }

    private void parseWhileBlock() {
        eat(TokenType.WHILE);
        eat(TokenType.LPAREN);
        parseExpression();
        eat(TokenType.RPAREN);
        parseCommandBlock();
        eat(TokenType.ENDWHILE);
    }

    private void parseExpression() {
        parseAddExp();
    }

    private void parseAddExp() {
        parseTerm();
        while (currentToken.getType() == TokenType.ADD || currentToken.getType() == TokenType.SUB) {
            eat(currentToken.getType());
            parseTerm();
        }
    }

    private void parseTerm() {
        parseUnaryExp();
        while (currentToken.getType() == TokenType.MUL || currentToken.getType() == TokenType.DIV || currentToken.getType() == TokenType.MOD) {
            eat(currentToken.getType());
            parseUnaryExp();
        }
    }

    private void parseUnaryExp() {
        switch (currentToken.getType()) {
            case IDENTIFIER, INT_CONST, REAL_CONST, STRING_CONST, CHAR_CONST, TRUE, FALSE -> {
                String name = currentToken.getLexeme();
                if (currentToken.getType() == TokenType.IDENTIFIER && !symbolTable.exists(name)) {
                    throw new RuntimeException("Identificador não declarado: " + name);
                }
                eat(currentToken.getType());
            }
            case LPAREN -> {
                eat(TokenType.LPAREN);
                parseExpression();
                eat(TokenType.RPAREN);
            }
            default -> throw new RuntimeException("Expressão inválida em: " + currentToken.getLexeme());
        }
    }
}

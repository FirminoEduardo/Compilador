package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

public class ParserImpl {

    private final Lexer lexer;
    private Token currentToken;

    public ParserImpl(Lexer lexer) {
        this.lexer = lexer;
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
        eat(TokenType.PROGRAM);                   // "program"
        eat(TokenType.IDENTIFIER);                // programName
        eat(TokenType.DECLARATIONS);              // "declarations"
        parseDeclarationList();                   // DeclarationList
        eat(TokenType.END_DECLARATIONS);          // "endDeclararions"

        if (currentToken.getType() == TokenType.FUNCTIONS) {
            eat(TokenType.FUNCTIONS);             // "functions"
            parseFunctionList();                  // FunctionList
            eat(TokenType.END_FUNCTIONS);         // "endFunctions"
        }

        eat(TokenType.END_PROGRAM);               // "endProgram"
        System.out.println("Programa sintaticamente correto!");
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
        parseTypeSpecification();
        eat(TokenType.COLON);
        parseVariableList();
    }

    private void parseTypeSpecification() {
        switch (currentToken.getType()) {
            case REAL, INTEGER, STRING, BOOLEAN, CHARACTER, VOID -> eat(currentToken.getType());
            default -> throw new RuntimeException("Tipo inválido: " + currentToken.getLexeme());
        }
    }

    private void parseVariableList() {
        eat(TokenType.IDENTIFIER);
        while (currentToken.getType() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            eat(TokenType.IDENTIFIER);
        }
    }

    private void parseFunctionList() {
        parseFunctionDeclaration();
        while (currentToken.getType() == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);

            if (currentToken.getType() == TokenType.FUNCTYPE) {
                parseFunctionDeclaration();
            } else {
                // Permite finalizar a lista após o último ponto e vírgula
                break;
            }
        }
    }

    private void parseFunctionDeclaration() {
        eat(TokenType.FUNCTYPE);
        parseTypeSpecification();
        eat(TokenType.COLON);
        eat(TokenType.IDENTIFIER); // functionName
        eat(TokenType.LPAREN);

        if (currentToken.getType() != TokenType.RPAREN) {
            parseParameterList();
        }

        eat(TokenType.RPAREN);
        parseCommandBlock();
        eat(TokenType.IDENTIFIER); // endFunction
    }

    private void parseParameterList() {
        eat(TokenType.IDENTIFIER);
        while (currentToken.getType() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            eat(TokenType.IDENTIFIER);
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
                parseExpression(); // simplificação por enquanto
                eat(TokenType.SEMICOLON);
            }
            case BREAK -> {
                eat(TokenType.BREAK);
                eat(TokenType.SEMICOLON);
            }
            case PRINT -> {
                eat(TokenType.PRINT);
                parseExpression(); // simplificação por enquanto
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
        parseExpression(); // condição
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
        parseExpression(); // condição
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
            eat(currentToken.getType()); // Consome + ou -
            parseTerm();
        }
    }

    private void parseTerm() {
        parseUnaryExp();
        while (currentToken.getType() == TokenType.MUL || currentToken.getType() == TokenType.DIV || currentToken.getType() == TokenType.MOD) {
            eat(currentToken.getType()); // Consome *, / ou %
            parseUnaryExp();
        }
    }

    private void parseUnaryExp() {
        switch (currentToken.getType()) {
            case IDENTIFIER, INT_CONST, REAL_CONST, STRING_CONST, CHAR_CONST, TRUE, FALSE ->
                    eat(currentToken.getType());
            case LPAREN -> { // expressão entre parênteses
                eat(TokenType.LPAREN);
                parseExpression();
                eat(TokenType.RPAREN);
            }
            default -> throw new RuntimeException("Expressão inválida em: " + currentToken.getLexeme());
        }
    }

    private void parseIfCommand() {
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        parseExpression(); // condição
        eat(TokenType.RPAREN);
        parseCommandBlock();
        if (currentToken.getType() == TokenType.ELSE) {
            eat(TokenType.ELSE);
            parseCommandBlock();
        }
        eat(TokenType.ENDIF);
    }

    private void parseWhileCommand() {
        eat(TokenType.WHILE);
        eat(TokenType.LPAREN);
        parseExpression();
        eat(TokenType.RPAREN);
        parseCommandBlock();
        eat(TokenType.ENDWHILE);
    }

    private void parseReturnCommand() {
        eat(TokenType.RETURN);
        parseExpression();
        eat(TokenType.SEMICOLON);
    }

    private void parsePrintCommand() {
        eat(TokenType.PRINT);
        eat(TokenType.LPAREN);
        parseExpression(); // ou apenas IDENTIFIER, dependendo da gramática
        eat(TokenType.RPAREN);
        eat(TokenType.SEMICOLON);
    }

    private void parseAssignmentOrCall() {
        eat(TokenType.IDENTIFIER);
        if (currentToken.getType() == TokenType.ASSIGN) {
            eat(TokenType.ASSIGN);
            parseExpression();
            eat(TokenType.SEMICOLON);
        } else if (currentToken.getType() == TokenType.LPAREN) {
            // chamada de função
            eat(TokenType.LPAREN);
            if (currentToken.getType() != TokenType.RPAREN) {
                parseExpression(); // Simples, pode ser expandido com múltiplos args
                while (currentToken.getType() == TokenType.COMMA) {
                    eat(TokenType.COMMA);
                    parseExpression();
                }
            }
            eat(TokenType.RPAREN);
            eat(TokenType.SEMICOLON);
        } else {
            throw new RuntimeException("Esperado ':=' ou '(' após identificador.");
        }
    }


}

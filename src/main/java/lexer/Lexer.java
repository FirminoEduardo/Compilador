package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (position < input.length()) {
            char current = peek();

            if (Character.isWhitespace(current)) {
                advance();
                continue;
            }

            if (Character.isLetter(current)) {
                tokens.add(lexIdentifierOrKeyword());
                continue;
            }

            if (Character.isDigit(current)) {
                tokens.add(lexNumber());
                continue;
            }

            switch (current) {
                case ':':
                    if (peekNext() == '=') {
                        tokens.add(new Token(TokenType.ASSIGN, ":="));
                        advance(2);
                    } else {
                        tokens.add(new Token(TokenType.COLON, ":"));
                        advance();
                    }
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, ","));
                    advance();
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";"));
                    advance();
                    break;
                case '+':
                    tokens.add(new Token(TokenType.ADD, "+"));
                    advance();
                    break;
                case '-':
                    tokens.add(new Token(TokenType.SUB, "-"));
                    advance();
                    break;
                case '*':
                    tokens.add(new Token(TokenType.MUL, "*"));
                    advance();
                    break;
                case '/':
                    tokens.add(new Token(TokenType.DIV, "/"));
                    advance();
                    break;
                case '%':
                    tokens.add(new Token(TokenType.MOD, "%"));
                    advance();
                    break;
                case '=':
                    if (peekNext() == '=') {
                        tokens.add(new Token(TokenType.EQ, "=="));
                        advance(2);
                    }
                    break;
                case '!':
                    if (peekNext() == '=') {
                        tokens.add(new Token(TokenType.NEQ, "!="));
                        advance(2);
                    }
                    break;
                case '<':
                    if (peekNext() == '=') {
                        tokens.add(new Token(TokenType.LE, "<="));
                        advance(2);
                    } else {
                        tokens.add(new Token(TokenType.LT, "<"));
                        advance();
                    }
                    break;
                case '>':
                    if (peekNext() == '=') {
                        tokens.add(new Token(TokenType.GE, ">="));
                        advance(2);
                    } else {
                        tokens.add(new Token(TokenType.GT, ">"));
                        advance();
                    }
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "("));
                    advance();
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")"));
                    advance();
                    break;
                case '{':
                    tokens.add(new Token(TokenType.LBRACE, "{"));
                    advance();
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RBRACE, "}"));
                    advance();
                    break;
                case '[':
                    tokens.add(new Token(TokenType.LBRACKET, "["));
                    advance();
                    break;
                case ']':
                    tokens.add(new Token(TokenType.RBRACKET, "]"));
                    advance();
                    break;
                case '"':
                    tokens.add(lexString());
                    break;
                case '\'':
                    tokens.add(lexChar());
                    break;
                default:
                    throw new RuntimeException("Caractere inesperado: " + current);
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private Token lexIdentifierOrKeyword() {
        StringBuilder sb = new StringBuilder();

        while (Character.isLetterOrDigit(peek())) {
            sb.append(peek());
            advance();
        }

        String lexeme = sb.toString();

        switch (lexeme) {
            case "program": return new Token(TokenType.PROGRAM, lexeme);
            case "declarations": return new Token(TokenType.DECLARATIONS, lexeme);
            case "endDeclararions": return new Token(TokenType.END_DECLARATIONS, lexeme);
            case "functions": return new Token(TokenType.FUNCTIONS, lexeme);
            case "endFunctions": return new Token(TokenType.END_FUNCTIONS, lexeme);
            case "endProgram": return new Token(TokenType.END_PROGRAM, lexeme);
            case "varType": return new Token(TokenType.VARTYPE, lexeme);
            case "funcType": return new Token(TokenType.FUNCTYPE, lexeme);
            case "paramType": return new Token(TokenType.PARAMTYPE, lexeme);
            case "if": return new Token(TokenType.IF, lexeme);
            case "else": return new Token(TokenType.ELSE, lexeme);
            case "endIf": return new Token(TokenType.ENDIF, lexeme);
            case "while": return new Token(TokenType.WHILE, lexeme);
            case "endWhile": return new Token(TokenType.ENDWHILE, lexeme);
            case "return": return new Token(TokenType.RETURN, lexeme);
            case "break": return new Token(TokenType.BREAK, lexeme);
            case "print": return new Token(TokenType.PRINT, lexeme);
            case "real": return new Token(TokenType.REAL, lexeme);
            case "integer": return new Token(TokenType.INTEGER, lexeme);
            case "string": return new Token(TokenType.STRING, lexeme);
            case "boolean": return new Token(TokenType.BOOLEAN, lexeme);
            case "character": return new Token(TokenType.CHARACTER, lexeme);
            case "void": return new Token(TokenType.VOID, lexeme);
            case "true": return new Token(TokenType.TRUE, lexeme);
            case "false": return new Token(TokenType.FALSE, lexeme);
            default: return new Token(TokenType.IDENTIFIER, lexeme);
        }
    }

    private Token lexNumber() {
        StringBuilder sb = new StringBuilder();

        while (Character.isDigit(peek())) {
            sb.append(peek());
            advance();
        }

        if (peek() == '.') {
            sb.append('.');
            advance();
            while (Character.isDigit(peek())) {
                sb.append(peek());
                advance();
            }
            return new Token(TokenType.REAL_CONST, sb.toString());
        }

        return new Token(TokenType.INT_CONST, sb.toString());
    }

    private Token lexString() {
        advance(); // pula o "
        StringBuilder sb = new StringBuilder();
        while (peek() != '"') {
            sb.append(peek());
            advance();
        }
        advance(); // pula o "
        return new Token(TokenType.STRING_CONST, sb.toString());
    }

    private Token lexChar() {
        advance(); // pula o '
        char c = peek();
        advance();
        advance(); // pula o '
        return new Token(TokenType.CHAR_CONST, String.valueOf(c));
    }

    private char peek() {
        return input.charAt(position);
    }

    private char peekNext() {
        return position + 1 < input.length() ? input.charAt(position + 1) : '\0';
    }

    private void advance() {
        position++;
    }

    private void advance(int steps) {
        position += steps;
    }
}
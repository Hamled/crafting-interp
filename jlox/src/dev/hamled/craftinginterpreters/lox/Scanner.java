package dev.hamled.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("break", TokenType.BREAK);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch(c) {
            // Single-character tokens
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;
            case '?': addToken(TokenType.QUESTION); break;
            case ':': addToken(TokenType.COLON); break;

            // One or two character tokens
            case '!': addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER); break;

            // Comments and slashes
            case '/':
                if(peek() == '/' || peek() == '*') {
                    comment(match('/'));
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            // Whitespace
            case '\n':
                line++;
            case ' ':
            case '\t':
            case '\r':
                break;

            case '"': string(); break;

            default:
                if(isDigit(c)) {
                    number();
                } else if(isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected character.");
                }
        }
    }

    private void identifier() {
        while(isAlphaNumeric(peek())) advance();

        // See if the identifier is a reserved word.
        String text = source.substring(start, current);

        TokenType type = keywords.get(text);
        if(type == null) type = TokenType.IDENTIFIER;
        addToken(type);
    }

    private void number() {
        while(isDigit(peek())) advance();

        // Look for a fractional part.
        if(peek() == '.' && isDigit(peekNext())) {
            advance();

            while(isDigit(peek())) advance();
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while(peek() != '"' && !isAtEnd()) {
            if(peek() == '\n') line++;
            advance();
        }

        // Unterminated string.
        if(isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private void comment(boolean inline) {
        if(inline) {
            // An "inline" comment goes until the end of the line
            while(peek() != '\n' && !isAtEnd()) advance();
        } else {
            // A "block" comment goes until the closing '*/'
            boolean inComment = true;
            while(inComment) {
                switch(peek()) {
                    case '\n': line++; break;
                    case '*':
                        if(peekNext() == '/') {
                            advance(); // Need to consume two chars for terminator
                            inComment = false;
                        }
                        break;
                    case '/':
                        if(peekNext() == '*') { // Nested comment
                            advance(); advance();
                            comment(false);
                        }
                        break;
                    case '\0':
                        Lox.error(line, "Unterminated block comment.");
                        return;
                }
                advance();
            }
        }
    }

    private boolean match(char expected) {
        if(isAtEnd()) return false;
        if(peek() != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if(current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}

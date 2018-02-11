/*
 * Copyright 2018 "JackWhite20"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jackwhite20.slug.lexer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class Lexer {

    private static final char EOF = '|';

    private static Map<String, Token> reservedKeywords = new HashMap<>();

    private String input;

    private int pos;

    private char currentChar;

    static {
        reservedKeywords.put("class", new Token(TokenType.CLASS, "class"));
        reservedKeywords.put("func", new Token(TokenType.FUNC, "func"));
        reservedKeywords.put("call", new Token(TokenType.CALL, "call"));
        reservedKeywords.put("bool", new Token(TokenType.BOOL, "bool"));
        reservedKeywords.put("int", new Token(TokenType.INTEGER, "int"));
        reservedKeywords.put("string", new Token(TokenType.STRING, "string"));
        reservedKeywords.put("if", new Token(TokenType.IF, "if"));
        reservedKeywords.put("for", new Token(TokenType.FOR, "for"));
        reservedKeywords.put("while", new Token(TokenType.WHILE, "while"));
        reservedKeywords.put("else", new Token(TokenType.ELSE, "else"));
        reservedKeywords.put("return", new Token(TokenType.RETURN, "return"));
        reservedKeywords.put("new", new Token(TokenType.NEW, "new"));
    }

    public Lexer(String input) {
        this.input = input;
        this.currentChar = input.charAt(pos);
    }

    private void advance() {
        pos++;
        if (pos > input.length() - 1) {
            currentChar = EOF;
        } else {
            currentChar = input.charAt(pos);
        }
    }

    private char peek() {
        int peekPos = pos + 1;
        if (peekPos > input.length() - 1) {
            return EOF;
        } else {
            return input.charAt(peekPos);
        }
    }

    private void skipComments() {
        while (currentChar != EOF && (currentChar != '#' && peek() != '#')) {
            advance();
        }

        // Skip possible whitespaces in the comment
        skipWhitespace();

        if (currentChar == '#') {
            advance();

            if (currentChar == '#') {
                advance();
            } else {
                throw new IllegalStateException("missing / to close comment");
            }
        } else {
            throw new IllegalStateException("comments should end with //");
        }
    }

    private void skipWhitespace() {
        while (currentChar != EOF && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private int integer() {
        StringBuilder res = new StringBuilder();
        while (currentChar != EOF && Character.isDigit(currentChar)) {
            res.append(currentChar);
            advance();
        }

        return Integer.parseInt(res.toString());
    }

    private Token handleReserved() {
        StringBuilder result = new StringBuilder();
        while (currentChar != EOF && Character.isLetterOrDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }

        // TODO: 11.02.2018 Should SLUG be case sensitive?
        String possibleKeywordString = result.toString();//.toLowerCase();

        Token possibleKeyword = reservedKeywords.get(possibleKeywordString);
        if (possibleKeyword == null) {
            // Check if we got a function call (also used for function declaration)
            if (currentChar == '(') {
                return new Token(TokenType.CALL, possibleKeywordString);
            } else {
                // TODO: 11.02.2018 Better way of checking this?
                // Check if boolean false/true or normal name token
                if (possibleKeywordString.equalsIgnoreCase("true") || possibleKeywordString.equalsIgnoreCase("false")) {
                    return new Token(TokenType.BOOL, possibleKeywordString);
                } else {
                    return new Token(TokenType.NAME, possibleKeywordString);
                }
            }
        }

        return possibleKeyword;
    }

    public Token nextToken() {
        while (currentChar != EOF) {
            // Handle comments
            if (currentChar == '#') {
                char peek = peek();
                if (peek != EOF && peek == '#') {
                    advance();
                    advance();
                    skipComments();
                    continue;
                } else {
                    throw new IllegalStateException("EOF reached while lexing comments");
                }
            }

            // Whitespace
            if (Character.isWhitespace(currentChar)) {
                skipWhitespace();
                continue;
            }

            // Handle the reserved keywords or names (eg. variable name, function name)
            if (Character.isAlphabetic(currentChar)) {
                return handleReserved();
            }

            if (Character.isDigit(currentChar)) {
                return new Token(TokenType.INTEGER, String.valueOf(integer()));
            }

            // Equal and assign
            if (currentChar == '=') {
                advance();
                if (currentChar != '=') {
                    return new Token(TokenType.ASSIGN, "=");
                } else {
                    advance();
                    return new Token(TokenType.EQUAL, "==");
                }
            }

            if (currentChar == ';') {
                advance();
                return new Token(TokenType.SEMICOLON, ";");
            }
          
            if (currentChar == ',') {
                advance();
                return new Token(TokenType.COMMA, ",");
            }

            // Not equal
            if (currentChar == '!' && peek() == '=') {
                advance();
                advance();
                return new Token(TokenType.NOT_EQUAL, "!=");
            }

            // Greater
            if (currentChar == '>') {
                advance();
                if (currentChar == '=') {
                    advance();
                    return new Token(TokenType.GREATER_EQUAL, ">=");
                } else {
                    return new Token(TokenType.GREATER, ">");
                }
            }

            // Less
            if (currentChar == '<') {
                advance();
                if (currentChar == '=') {
                    advance();
                    return new Token(TokenType.LESS_EQUAL, "<=");
                } else {
                    return new Token(TokenType.LESS, "<");
                }
            }

            if (currentChar == '+') {
                advance();
                return new Token(TokenType.PLUS, "+");
            }
            if (currentChar == '-') {
                advance();
                return new Token(TokenType.MINUS, "-");
            }
            if (currentChar == '*') {
                advance();
                return new Token(TokenType.MULTIPLY, "*");
            }
            if (currentChar == '/') {
                advance();
                return new Token(TokenType.DIVIDE, "/");
            }
            if (currentChar == '(') {
                advance();
                return new Token(TokenType.LEFT_PARAN, "(");
            }
            if (currentChar == ')') {
                advance();
                return new Token(TokenType.RIGHT_PARAN, ")");
            }
            if (currentChar == '{') {
                advance();
                return new Token(TokenType.CURLY_LEFT_PARAN, "{");
            }
            if (currentChar == '}') {
                advance();
                return new Token(TokenType.CURLY_RIGHT_PARAN, "}");
            }

            // String
            if (currentChar == '"') {
                advance();
                StringBuilder string = new StringBuilder();
                // Allow all chars in a string variable
                while (currentChar != EOF && currentChar != '"') {
                    string.append(currentChar);
                    advance();
                }
                // Strings needs to end with '"'
                if (currentChar != '"') {
                    throw new IllegalStateException("strings needs to end with '\"'");
                }

                // Remove the last '"' from '"some string"'
                advance();

                return new Token(TokenType.STRING, string.toString());
            }
        }

        return new Token(TokenType.NONE, "NONE");
    }
}

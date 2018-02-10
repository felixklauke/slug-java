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

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class Lexer {

    private static final char EOF = '#';

    private String input;

    private int pos;

    private char currentChar;

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
        while (currentChar != EOF && (currentChar != '/' && peek() != '/')) {
            advance();
        }

        // Skip possible whitespaces in the comment
        skipWhitespace();

        if (currentChar == '/') {
            advance();

            if (currentChar == '/') {
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

    private Token handleReserved() {
        throw new UnsupportedOperationException("not implemented");
    }

    public Token nextToken() {
        while (currentChar != EOF) {
            // Handle comments
            if (currentChar == '/') {
                if (peek() != EOF) {
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

            if (currentChar == ',') {
                advance();
                return new Token(TokenType.COMMA, ",");
            }

            if (currentChar == '+') {
                advance();
                return new Token(TokenType.PLUS, "+");
            }
        }

        return new Token(TokenType.NONE, "NONE");
    }
}

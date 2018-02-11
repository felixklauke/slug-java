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

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class LexerTest {

    @Test
    public void testClass() {
        Lexer lexer = new Lexer("class");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.CLASS, token.getTokenType());
        Assert.assertEquals("class", token.getValue());
    }

    @Test
    public void testFunc() {
        Lexer lexer = new Lexer("func");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.FUNC, token.getTokenType());
        Assert.assertEquals("func", token.getValue());
    }

    @Test
    public void testCurlyLeft() {
        Lexer lexer = new Lexer("{");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.CURLY_LEFT_PARAN, token.getTokenType());
        Assert.assertEquals("{", token.getValue());
    }

    @Test
    public void testCurlyRight() {
        Lexer lexer = new Lexer("}");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.CURLY_RIGHT_PARAN, token.getTokenType());
        Assert.assertEquals("}", token.getValue());
    }

    @Test
    public void testLeftParan() {
        Lexer lexer = new Lexer("(");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.LEFT_PARAN, token.getTokenType());
        Assert.assertEquals("(", token.getValue());
    }

    @Test
    public void testRightParan() {
        Lexer lexer = new Lexer(")");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.RIGHT_PARAN, token.getTokenType());
        Assert.assertEquals(")", token.getValue());
    }

    @Test
    public void testSemicolon() {
        Lexer lexer = new Lexer(";");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.SEMICOLON, token.getTokenType());
        Assert.assertEquals(";", token.getValue());
    }

    @Test
    public void testComma() {
        Lexer lexer = new Lexer(",");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.COMMA, token.getTokenType());
        Assert.assertEquals(",", token.getValue());
    }

    @Test
    public void testName() {
        Lexer lexer = new Lexer("Test");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.NAME, token.getTokenType());
        Assert.assertEquals("Test", token.getValue());
    }

    @Test
    public void testCall() {
        Lexer lexer = new Lexer("WriteLine()");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.CALL, token.getTokenType());
        Assert.assertEquals("WriteLine", token.getValue());
    }

    @Test
    public void testReturn() {
        Lexer lexer = new Lexer("return");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.RETURN, token.getTokenType());
        Assert.assertEquals("return", token.getValue());
    }

    @Test
    public void testBoolType() {
        Lexer lexer = new Lexer("bool");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.BOOL, token.getTokenType());
        Assert.assertEquals("bool", token.getValue());
    }

    @Test
    public void testBoolValueFalse() {
        Lexer lexer = new Lexer("false");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.BOOL, token.getTokenType());
        Assert.assertEquals("false", token.getValue());
    }

    @Test
    public void testBoolValueTrue() {
        Lexer lexer = new Lexer("true");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.BOOL, token.getTokenType());
        Assert.assertEquals("true", token.getValue());
    }

    @Test
    public void testIntegerType() {
        Lexer lexer = new Lexer("int");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.INTEGER, token.getTokenType());
        Assert.assertEquals("int", token.getValue());
    }

    @Test
    public void testIntegerValue() {
        Lexer lexer = new Lexer("42");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.INTEGER, token.getTokenType());
        Assert.assertEquals("42", token.getValue());
    }

    @Test
    public void testStringType() {
        Lexer lexer = new Lexer("string");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.STRING, token.getTokenType());
        Assert.assertEquals("string", token.getValue());
    }

    @Test
    public void testStringValue() {
        Lexer lexer = new Lexer("\"hello\"");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.STRING, token.getTokenType());
        Assert.assertEquals("hello", token.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testStringValueMissingClosingQuotationMark() {
        Lexer lexer = new Lexer("\"hello");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.STRING, token.getTokenType());
        Assert.assertEquals("hello", token.getValue());
    }

    @Test
    public void testIf() {
        Lexer lexer = new Lexer("if");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.IF, token.getTokenType());
        Assert.assertEquals("if", token.getValue());
    }

    @Test
    public void testElse() {
        Lexer lexer = new Lexer("else");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.ELSE, token.getTokenType());
        Assert.assertEquals("else", token.getValue());
    }

    @Test
    public void testFor() {
        Lexer lexer = new Lexer("for");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.FOR, token.getTokenType());
        Assert.assertEquals("for", token.getValue());
    }

    @Test
    public void testWhile() {
        Lexer lexer = new Lexer("while");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.WHILE, token.getTokenType());
        Assert.assertEquals("while", token.getValue());
    }

    @Test
    public void testBinaryTokens() {
        Lexer lexer = new Lexer("+-*/");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.PLUS, token.getTokenType());
        Assert.assertEquals("+", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.MINUS, token.getTokenType());
        Assert.assertEquals("-", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.MULTIPLY, token.getTokenType());
        Assert.assertEquals("*", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.DIVIDE, token.getTokenType());
        Assert.assertEquals("/", token.getValue());
    }

    @Test
    public void testAssign() {
        Lexer lexer = new Lexer("=");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.ASSIGN, token.getTokenType());
        Assert.assertEquals("=", token.getValue());
    }

    @Test
    public void testBooleanOperator() {
        Lexer lexer = new Lexer("== != > < >= <=");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.EQUAL, token.getTokenType());
        Assert.assertEquals("==", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.NOT_EQUAL, token.getTokenType());
        Assert.assertEquals("!=", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.GREATER, token.getTokenType());
        Assert.assertEquals(">", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.LESS, token.getTokenType());
        Assert.assertEquals("<", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.GREATER_EQUAL, token.getTokenType());
        Assert.assertEquals(">=", token.getValue());

        token = lexer.nextToken();
        Assert.assertEquals(TokenType.LESS_EQUAL, token.getTokenType());
        Assert.assertEquals("<=", token.getValue());
    }

    @Test
    public void testNew() {
        Lexer lexer = new Lexer("new");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.NEW, token.getTokenType());
        Assert.assertEquals("new", token.getValue());
    }

    @Test
    public void testNone() {
        Lexer lexer = new Lexer(" ");

        Token token = lexer.nextToken();
        Assert.assertEquals(TokenType.NONE, token.getTokenType());
        Assert.assertEquals("NONE", token.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeakEndOfFile() {
        Lexer lexer = new Lexer("#");

        lexer.nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testCommentMissingSecondSlash() {
        Lexer lexer = new Lexer("## #");

        lexer.nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testCommentMissingClosingSlashes() {
        Lexer lexer = new Lexer("## ");

        lexer.nextToken();
    }

    @Test
    public void testCommentCorrect() {
        Lexer lexer = new Lexer("## hello ##");

        lexer.nextToken();
    }
}
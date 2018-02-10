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
    public void nextToken() throws Exception {
        Lexer lexer = new Lexer(", + // test //");

        Token comma = lexer.nextToken();
        Assert.assertEquals(TokenType.COMMA, comma.getTokenType());
        Assert.assertEquals(",", comma.getValue());

        Token plus = lexer.nextToken();
        Assert.assertEquals(TokenType.PLUS, plus.getTokenType());
        Assert.assertEquals("+", plus.getValue());

        Token none = lexer.nextToken();
        Assert.assertEquals(TokenType.NONE, none.getTokenType());
        Assert.assertEquals("NONE", none.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeakEndOfFile() {
        Lexer lexer = new Lexer("/");

        lexer.nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testCommentMissingSecondSlash() {
        Lexer lexer = new Lexer("// /");

        lexer.nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testCommentMissingClosingSlashes() {
        Lexer lexer = new Lexer("// ");

        lexer.nextToken();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReservedNotSupported() {
        Lexer lexer = new Lexer("class");

        lexer.nextToken();
    }
}
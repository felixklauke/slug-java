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

package net.jackwhite20.slug.interpreter;

import net.jackwhite20.slug.ast.*;
import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.lexer.Token;
import net.jackwhite20.slug.lexer.TokenType;
import org.junit.Test;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class TestNodeVisitor {

    @Test(expected = SlugRuntimeException.class)
    public void testVisitUnhandledNode() {
        NodeVisitor nodeVisitor = new NodeVisitor();
        nodeVisitor.visit(new UnhandledNode());
    }

    @Test(expected = SlugRuntimeException.class)
    public void testInvalidBinaryOperator() {
        NodeVisitor nodeVisitor = new NodeVisitor();
        nodeVisitor.visit(new BinaryNode(new NumberNode("2"), TokenType.COMMA, new NumberNode("2")));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testUnaryInvalidOperator() {
        NodeVisitor nodeVisitor = new NodeVisitor();
        nodeVisitor.visit(new UnaryNode(new Token(TokenType.MULTIPLY, "*"), new NumberNode("6")));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testBooleanInvalidOperator() {
        NodeVisitor nodeVisitor = new NodeVisitor();
        nodeVisitor.visit(new BooleanNode(new NumberNode("2"), TokenType.PLUS, new NumberNode("2")));
    }

    private class UnhandledNode extends Node {

    }
}

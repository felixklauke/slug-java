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

package net.jackwhite20.slug.ast;

import net.jackwhite20.slug.lexer.Token;
import net.jackwhite20.slug.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ForNodeTest {

    private static final Node DECLARATION =
            new VariableDeclarationAssignNode("a", TokenType.INTEGER, new NumberNode("5"));

    private static final Node CONDITION =
            new BooleanNode(new NumberNode("10"), TokenType.EQUAL, new NumberNode("10"));

    private static final Node EXPRESSION =
            new VariableAssignNode("a",
                    new BinaryNode(
                            new VariableUsageNode(
                                    new Token(TokenType.NAME, "a")), TokenType.PLUS,
                            new NumberNode("1")));

    private static final BlockNode BLOCK = new BlockNode(null, Collections.singletonList(EXPRESSION));

    private ForNode forNode;

    @Before
    public void setUp() {
        forNode = new ForNode(DECLARATION, CONDITION, EXPRESSION, BLOCK);
    }

    @Test
    public void testGetDeclaration() {
        assertEquals(DECLARATION, forNode.getDeclaration());
    }

    @Test
    public void testGetCondition() {
        assertEquals(CONDITION, forNode.getCondition());
    }

    @Test
    public void testGetExpression() {
        assertEquals(EXPRESSION, forNode.getExpression());
    }

    @Test
    public void testGetBlock() {
        assertEquals(BLOCK, forNode.getBlock());
    }
}

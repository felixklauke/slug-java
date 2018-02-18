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

import net.jackwhite20.slug.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VariableDeclarationAssignNodeTest {

    private static final String VARIABLE_NAME = "a";

    private static final TokenType VARIABLE_TYPE = TokenType.STRING;

    private static final Node RIGHT = new StringNode("slug");

    private VariableDeclarationAssignNode varDecAssign;

    @Before
    public void setUp() throws Exception {
        varDecAssign = new VariableDeclarationAssignNode(VARIABLE_NAME, VARIABLE_TYPE, RIGHT);
    }

    @Test
    public void getVariableName() {
        assertEquals(VARIABLE_NAME, varDecAssign.getVariableName());
    }

    @Test
    public void getVariableType() {
        assertEquals(VARIABLE_TYPE, varDecAssign.getVariableType());
    }

    @Test
    public void getRight() {
        assertEquals(RIGHT, varDecAssign.getRight());
    }
}
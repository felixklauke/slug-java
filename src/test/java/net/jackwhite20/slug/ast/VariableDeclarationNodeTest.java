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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VariableDeclarationNodeTest {

    private static final String VARIABLE_NAME = "b";

    private static final TokenType VARIABLE_TYPE = TokenType.STRING;

    @Test
    public void testVariableDeclarationNode() {
        VariableDeclarationNode variableDeclarationNode = new VariableDeclarationNode(0, VARIABLE_NAME, VARIABLE_TYPE);

        assertEquals(VARIABLE_NAME, variableDeclarationNode.getVariableName());
        assertEquals(VARIABLE_TYPE, variableDeclarationNode.getVariableType());
    }
}
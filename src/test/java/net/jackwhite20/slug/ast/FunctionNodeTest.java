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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FunctionNodeTest {

    private static final String NAME = "MyOtherFunction";

    private static final List<Node> CHILDREN = Collections.singletonList(new VariableUsageNode(new Token(TokenType.NAME, "a")));

    private static final List<Node> PARAMETERS = Arrays.asList(new NumberNode("65"), new BoolNode("true"));

    private FunctionNode functionNode;

    @Before
    public void setUp() throws Exception {
        functionNode = new FunctionNode(NAME, CHILDREN, PARAMETERS);
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, functionNode.getName());
    }

    @Test
    public void testGetChildren() {
        assertEquals(CHILDREN, functionNode.getChildren());
    }

    @Test
    public void testGetParameter() {
        assertEquals(PARAMETERS, functionNode.getParameter());
    }
}
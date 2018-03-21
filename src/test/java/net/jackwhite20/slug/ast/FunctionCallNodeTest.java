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

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FunctionCallNodeTest {

    private static final String NAME = "MyFunction";

    private static final List<Node> PARAMETERS = Arrays.asList(new NumberNode("43"), new BoolNode("false"));

    private static final FunctionNode FUNCTION_NODE = new FunctionNode("MyFunction", new BlockNode(null, Collections.singletonList(new NumberNode("1"))), PARAMETERS);

    private FunctionCallNode functionCallNode;

    @Before
    public void setUp() throws Exception {
        functionCallNode = new FunctionCallNode(NAME, FUNCTION_NODE, PARAMETERS);
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, functionCallNode.getName());
    }

    @Test
    public void testGetParameter() {
        assertEquals(PARAMETERS, functionCallNode.getParameter());
    }

    @Test
    public void testGetFunctionNode() {
        assertEquals(FUNCTION_NODE, functionCallNode.getFunctionNode());
    }
}
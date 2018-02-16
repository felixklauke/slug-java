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

import net.jackwhite20.slug.ast.FunctionNode;
import net.jackwhite20.slug.ast.NumberNode;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class FunctionRegistryTest {

    private static final FunctionNode FUNCTION_NODE;

    static {
        FUNCTION_NODE = new FunctionNode("Test",
                Collections.singletonList(new NumberNode("5")),
                Collections.singletonList(new NumberNode("6")));
    }

    @Before
    public void setUp() throws Exception {
        FunctionRegistry.register(FUNCTION_NODE);
    }

    @Test
    public void testLookup() throws Exception {
        FunctionNode functionNode = FunctionRegistry.lookup(FUNCTION_NODE.getName());

        assertNotNull(functionNode);

        assertNotNull(functionNode.getChildren());
        assertEquals(1, functionNode.getChildren().size());
        assertTrue(functionNode.getChildren().get(0) instanceof NumberNode);

        assertNotNull(functionNode.getParameter());
        assertEquals(1, functionNode.getParameter().size());
        assertTrue(functionNode.getParameter().get(0) instanceof NumberNode);
    }
}
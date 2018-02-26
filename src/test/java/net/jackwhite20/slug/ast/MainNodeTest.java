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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MainNodeTest {

    private static final List<Node> FUNCTIONS = Collections.singletonList(
            new FunctionNode("Add",
                    Collections.singletonList(new NumberNode("45")),
                    Collections.singletonList(new StringNode("hello"))));

    private MainNode mainNode;

    @Before
    public void setUp() throws Exception {
        mainNode = new MainNode(FUNCTIONS);
    }

    @Test
    public void testGetFunctions() {
        assertEquals(FUNCTIONS, mainNode.getFunctions());
    }
}
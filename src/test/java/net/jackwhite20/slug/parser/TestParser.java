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

package net.jackwhite20.slug.parser;

import net.jackwhite20.slug.ast.FunctionNode;
import net.jackwhite20.slug.ast.MainNode;
import net.jackwhite20.slug.ast.Node;
import net.jackwhite20.slug.lexer.Lexer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class TestParser {

    private Parser parser;

    @Before
    public void setUp() {
        Lexer lexer = new Lexer("func Main() { }");
        parser = new Parser(lexer);
    }

    @Test
    public void testParser() {
        Node ast = parser.parse();

        assertTrue(ast instanceof MainNode);

        MainNode mainNode = (MainNode) ast;

        List<Node> functions = mainNode.getFunctions();

        assertEquals(1, functions.size());

        Node function = functions.get(0);

        assertTrue(function instanceof FunctionNode);

        FunctionNode functionNode = (FunctionNode) function;

        assertEquals("Main", functionNode.getName());
        assertEquals(0, functionNode.getChildren().size());
        assertEquals(0, functionNode.getParameter().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongExpectedToken() {
        Lexer lexer = new Lexer("func () { }");
        Parser parser = new Parser(lexer);

        parser.parse();
    }
}

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

import net.jackwhite20.slug.ast.*;
import net.jackwhite20.slug.lexer.Lexer;
import net.jackwhite20.slug.lexer.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class TestParser {

    private Parser parser;

    @Before
    public void setUp() {
        Lexer lexer = new Lexer("func TestFunctionCall() { } func Main() { int a = 2 a = 10 + 10 * 5 / 10 + (-10 + 5) TestFunctionCall() bool b = true b = false}");
        parser = new Parser(lexer);
    }

    @Test
    public void testParser() {
        Node ast = parser.parse();

        assertTrue(ast instanceof MainNode);

        MainNode mainNode = (MainNode) ast;

        List<Node> functions = mainNode.getFunctions();

        assertEquals(2, functions.size());

        Node mainFunction = functions.get(1);

        assertTrue(mainFunction instanceof FunctionNode);

        FunctionNode functionNode = (FunctionNode) mainFunction;

        assertEquals("Main", functionNode.getName());
        assertEquals(5, functionNode.getChildren().size());
        assertEquals(0, functionNode.getParameter().size());

        assertTrue(functionNode.getChildren().get(0) instanceof VariableAssignNode);

        VariableAssignNode variableAssignNode = (VariableAssignNode) functionNode.getChildren().get(0);
        assertEquals("a", variableAssignNode.getVariableName());
        assertTrue(variableAssignNode.getRight() instanceof NumberNode);
        assertEquals(2, ((NumberNode) variableAssignNode.getRight()).getValue());


        VariableAssignNode calculation = (VariableAssignNode) functionNode.getChildren().get(1);
        assertEquals("a", calculation.getVariableName());
        assertTrue(calculation.getRight() instanceof BinaryNode);
        BinaryNode right = (BinaryNode) calculation.getRight();
        assertTrue(right.getLeft() instanceof BinaryNode);
        assertTrue(right.getOperator().getTokenType() == TokenType.PLUS);
        assertTrue(right.getRight() instanceof BinaryNode);
        // TODO: Test further?

        FunctionCallNode functionCallNode = (FunctionCallNode) functionNode.getChildren().get(2);
        assertEquals("TestFunctionCall", functionCallNode.getName());
        assertEquals(0, functionCallNode.getParameter().size());
        assertNotNull(functionCallNode.getFunctionNode());
        assertEquals("TestFunctionCall", functionCallNode.getFunctionNode().getName());
        assertEquals(0, functionCallNode.getFunctionNode().getParameter().size());
        assertEquals(1, functionCallNode.getFunctionNode().getChildren().size());
        assertTrue(functionCallNode.getFunctionNode().getChildren().get(0) instanceof NoOpNode);

        VariableAssignNode bool = (VariableAssignNode) functionNode.getChildren().get(3);
        assertEquals("b", bool.getVariableName());
        assertTrue(bool.getRight() instanceof BoolNode);
        assertEquals(true, ((BoolNode) bool.getRight()).getValue());

        VariableAssignNode boolSecond = (VariableAssignNode) functionNode.getChildren().get(4);
        assertEquals("b", boolSecond.getVariableName());
        assertTrue(boolSecond.getRight() instanceof BoolNode);
        assertEquals(false, ((BoolNode) boolSecond.getRight()).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongExpectedToken() {
        Lexer lexer = new Lexer("func () { }");
        Parser parser = new Parser(lexer);

        parser.parse();
    }
}

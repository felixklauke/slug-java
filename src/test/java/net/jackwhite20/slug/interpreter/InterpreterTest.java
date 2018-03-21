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

import net.jackwhite20.slug.ast.MainBlockNode;
import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.lexer.Lexer;
import net.jackwhite20.slug.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InterpreterTest {

    @Test(expected = SlugRuntimeException.class)
    public void testNoFunctions() {
        Lexer lexer = new Lexer("random");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test(expected = SlugRuntimeException.class)
    public void testMainNotLastFunction() {
        Lexer lexer = new Lexer("func Main() { } func OtherFunc() { }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testVariableDeclarationAndAssign() {
        Lexer lexer = new Lexer("int a = 42 string b = \"hello\" bool c = true");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(42, (int) MainBlockNode.getInstance().lookupVariable("a"));
        assertEquals("hello", MainBlockNode.getInstance().lookupVariable("b"));
        assertEquals(true, MainBlockNode.getInstance().lookupVariable("c"));
    }

    @Test
    public void testVariableDeclarationAndAssignAndOnlyAssign() {
        Lexer lexer = new Lexer("int a = 42 string b = \"hello\" bool c = true func Main() { a = 35 b = \"changed\" c = false }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(35, (int) MainBlockNode.getInstance().lookupVariable("a"));
        assertEquals("changed", MainBlockNode.getInstance().lookupVariable("b"));
        assertEquals(false, MainBlockNode.getInstance().lookupVariable("c"));
    }

    @Test
    public void testVariableDeclarationOnly() {
        // This also tests default values
        Lexer lexer = new Lexer("int i string s bool b");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(0, (int) MainBlockNode.getInstance().lookupVariable("i"));
        assertEquals("", MainBlockNode.getInstance().lookupVariable("s"));
        assertEquals(false, MainBlockNode.getInstance().lookupVariable("b"));
    }

    @Test
    public void testBooleanNode() {
        Lexer lexer = new Lexer("bool b = 4 > 2");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertTrue(MainBlockNode.getInstance().lookupVariable("b"));
    }

    @Test
    public void testIfNumberEqual() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a == 42) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testIfNumberNotEqual() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a != 40) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testIfNumberGreaterEqual() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a >= 40) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testIfNumberLessEqual() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a <= 42) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testIfNumberGreater() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a > 40) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testIfNumberLess() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a < 45) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testIfElse() {
        Lexer lexer = new Lexer("int success = 0 func Main() { int a = 42 if (a == 40) { success = 0 } else { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testWhile() {
        Lexer lexer = new Lexer("int i = 0 func Main() { while (i < 6) { i = i + 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(6, (int) MainBlockNode.getInstance().lookupVariable("i"));
    }

    @Test
    public void testFor() {
        Lexer lexer = new Lexer("int counter = 0 func Main() { for (int i = 0; i < 2; i = i + 1) { counter = i } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) MainBlockNode.getInstance().lookupVariable("counter"));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testWhileInvalidExpression() {
        Lexer lexer = new Lexer("int success = 0 func Main() { while (5) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test(expected = SlugRuntimeException.class)
    public void testIfInvalidExpression() {
        Lexer lexer = new Lexer("int success = 0 func Main() { if (5) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testFunctionCall() {
        Lexer lexer = new Lexer("bool success = false func Test() { success = true } func Main() { Test() }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertTrue(MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testVariableAlreadyExistsMainBlockNode() {
        Lexer lexer = new Lexer("int i int i");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test(expected = SlugRuntimeException.class)
    public void testVariableAlreadyExistsFunctionBlock() {
        Lexer lexer = new Lexer("func Main() { int i int i }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test(expected = SlugRuntimeException.class)
    public void testVariableUpdateNotFoundMainBlockNode() {
        Lexer lexer = new Lexer("i = 5");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test(expected = SlugRuntimeException.class)
    public void testVariableUpdateNotFoundFunctionBlock() {
        Lexer lexer = new Lexer("func Main() { i = 42}");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }
}
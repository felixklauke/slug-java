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
        Lexer lexer = new Lexer("func Main() { int a = 42 string b = \"hello\" bool c = true }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(42, (int) interpreter.getGlobalVariableRegistry().lookup("a"));
        assertEquals("hello", interpreter.getGlobalVariableRegistry().lookup("b"));
        assertEquals(true, interpreter.getGlobalVariableRegistry().lookup("c"));
    }

    @Test
    public void testVariableDeclarationAndAssignAndOnlyAssign() {
        Lexer lexer = new Lexer("func Main() { int a = 42 string b = \"hello\" bool c = true a = 35 b = \"changed\" c = false}");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(35, (int) interpreter.getGlobalVariableRegistry().lookup("a"));
        assertEquals("changed", interpreter.getGlobalVariableRegistry().lookup("b"));
        assertEquals(false, interpreter.getGlobalVariableRegistry().lookup("c"));
    }

    @Test
    public void testBooleanNode() {
        Lexer lexer = new Lexer("func Main() { bool b = 4 > 2 }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertTrue(interpreter.getGlobalVariableRegistry().lookup("b"));
    }

    @Test
    public void testIfNumberEqual() {
        Lexer lexer = new Lexer("func Main() { int a = 42 if (a == 42) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) interpreter.getGlobalVariableRegistry().lookup("success"));
    }

    @Test
    public void testIfNumberNotEqual() {
        Lexer lexer = new Lexer("func Main() { int a = 42 if (a != 40) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) interpreter.getGlobalVariableRegistry().lookup("success"));
    }

    @Test
    public void testIfNumberGreaterEqual() {
        Lexer lexer = new Lexer("func Main() { int a = 42 if (a >= 40) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) interpreter.getGlobalVariableRegistry().lookup("success"));
    }

    @Test
    public void testIfNumberLessEqual() {
        Lexer lexer = new Lexer("func Main() { int a = 42 if (a <= 42) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();


        assertEquals(1, (int) interpreter.getGlobalVariableRegistry().lookup("success"));
    }

    @Test
    public void testIfNumberGreater() {
        Lexer lexer = new Lexer("func Main() { int a = 42 if (a > 40) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) interpreter.getGlobalVariableRegistry().lookup("success"));
    }

    @Test
    public void testIfNumberLess() {
        Lexer lexer = new Lexer("func Main() { int a = 42 if (a < 45) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(1, (int) interpreter.getGlobalVariableRegistry().lookup("success"));
    }

    @Test
    public void testWhile() {
        Lexer lexer = new Lexer("func Main() { int i = 0 while (i < 6) { i = i + 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(6, (int) interpreter.getGlobalVariableRegistry().lookup("i"));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testWhileInvalidExpression() {
        Lexer lexer = new Lexer("func Main() { while (5) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test(expected = SlugRuntimeException.class)
    public void testIfInvalidExpression() {
        Lexer lexer = new Lexer("func Main() { if (5) { int success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }
}
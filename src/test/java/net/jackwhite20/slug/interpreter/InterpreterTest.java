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
import net.jackwhite20.slug.exception.VariableNotFoundException;
import net.jackwhite20.slug.lexer.Lexer;
import net.jackwhite20.slug.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

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

    @Test(expected = SlugRuntimeException.class)
    public void testWhileInvalidExpression() {
        Lexer lexer = new Lexer("int success = 0 func Main() { while (5) { success = 1 } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
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
    public void testForInvalidCondition() {
        Lexer lexer = new Lexer("func Main() { for (int i = 0; 5; i = i + 1) { } }");
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
        Lexer lexer = new Lexer("func Main() { i = 42 }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testUnaryMinus() {
        Lexer lexer = new Lexer("int i = -512");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(-512, (int) MainBlockNode.getInstance().lookupVariable("i"));
    }

    @Test
    public void testUnaryPlus() {
        Lexer lexer = new Lexer("int i = +512");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(512, (int) MainBlockNode.getInstance().lookupVariable("i"));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testUnaryInvalid() {
        Lexer lexer = new Lexer("int i = -false");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testOneParameter() {
        Lexer lexer = new Lexer("bool success = false func Test(bool param) { success = param } func Main() { Test(true) }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(true, MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test
    public void testMultipleParameter() {
        Lexer lexer = new Lexer("int success = 0 func Test(int one, int two) { success = one + two } func Main() { Test(2, 2) }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(4, (int) MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test(expected = SlugRuntimeException.class)
    public void testParameterInvalidAmount() {
        Lexer lexer = new Lexer("func Test(int one, int two) { } func Main() { Test(2) }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testCodeBlockScopes() {
        Lexer lexer = new Lexer("bool success = false func Main() { int i = 0 if (1 == 1) { i = 5 } if (i == 5) { success = true } }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        assertEquals(true, MainBlockNode.getInstance().lookupVariable("success"));
    }

    @Test(expected = VariableNotFoundException.class)
    public void testCodeBlockScopesInvalidLowerBlockAccess() {
        Lexer lexer = new Lexer("func Main() { if (1 == 1) { int i = 5 } i = 6  }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testCodeBlockScopesWithFunctionCall() {
        PrintStream savedOut = System.out;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(out)) {
            System.setOut(ps);

            Lexer lexer = new Lexer("func Test() { } func Main() { int i = 3 Test() WriteLine(i) }");
            Parser parser = new Parser(lexer);

            Interpreter interpreter = new Interpreter(parser);
            interpreter.interpret();

            System.setOut(savedOut);

            String output = out.toString("UTF-8");
            String[] lines = output.split("\n");
            String line = lines[lines.length - 2];

            assertEquals("3", line.substring(0, line.length() - 1));
        } catch (Exception e) {
            fail();
        } finally {
            System.setOut(savedOut);
        }
    }

    @Test
    public void testInternalFunctionWriteLine() {
        PrintStream savedOut = System.out;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(out)) {
            System.setOut(ps);

            Lexer lexer = new Lexer("func Main() { WriteLine(\"Hello, World!\") }");
            Parser parser = new Parser(lexer);

            Interpreter interpreter = new Interpreter(parser);
            interpreter.interpret();

            System.setOut(savedOut);

            String output = out.toString("UTF-8");
            String[] lines = output.split("\n");
            String line = lines[lines.length - 2];

            assertEquals("Hello, World!", line.substring(0, line.length() - 1));
        } catch (Exception e) {
            fail();
        } finally {
            System.setOut(savedOut);
        }
    }

    @Test(expected = SlugRuntimeException.class)
    public void testInternalFunctionWriteLineInvalidArgs() {
        Lexer lexer = new Lexer("func Main() { WriteLine() }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();
    }

    @Test
    public void testInternalFunctionRandomOneArg() {
        Lexer lexer = new Lexer("int random func Main() { random = Random(6) }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        int random = MainBlockNode.getInstance().lookupVariable("random");

        assertTrue(random != -1);
        assertTrue(random >= 0);
        assertTrue(random < 6);
    }

    @Test
    public void testInternalFunctionRandomTwoArgs() {
        Lexer lexer = new Lexer("int random func Main() { random = Random(4, 8) }");
        Parser parser = new Parser(lexer);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        int random = MainBlockNode.getInstance().lookupVariable("random");

        assertTrue(random != -1);
        assertTrue(random >= 4);
        assertTrue(random < 8);
    }

    @Test
    public void testInternalFunctionReadLineString() throws Exception {
        String testValue = UUID.randomUUID().toString();

        Lexer lexer = new Lexer("string read func Main() { read = ReadLine() }");
        Parser parser = new Parser(lexer);

        InputStream savedIn = System.in;

        InputStream testInput = new ByteArrayInputStream(testValue.getBytes("UTF-8"));
        System.setIn(testInput);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        System.setIn(savedIn);

        assertEquals(testValue, MainBlockNode.getInstance().lookupVariable("read"));
    }

    @Test
    public void testInternalFunctionReadLineInt() throws Exception {
        int testValue = ThreadLocalRandom.current().nextInt(0, 101);

        Lexer lexer = new Lexer("int read func Main() { read = ReadLine() }");
        Parser parser = new Parser(lexer);

        InputStream savedIn = System.in;

        InputStream testInput = new ByteArrayInputStream(String.valueOf(testValue).getBytes("UTF-8"));
        System.setIn(testInput);

        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        System.setIn(savedIn);

        assertEquals(testValue, (int) MainBlockNode.getInstance().lookupVariable("read"));
    }

    @Test
    public void testInlineVariablesSingle() {
        PrintStream savedOut = System.out;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(out)) {
            System.setOut(ps);

            Lexer lexer = new Lexer("func Main() { int i = 50 WriteLine(\"i = $i other text\") }");
            Parser parser = new Parser(lexer);

            Interpreter interpreter = new Interpreter(parser);
            interpreter.interpret();

            System.setOut(savedOut);

            String output = out.toString("UTF-8");
            System.out.println(output);
            String[] lines = output.split("\n");
            String line = lines[lines.length - 2];

            assertEquals("i = 50 other text", line.substring(0, line.length() - 1));
        } catch (Exception e) {
            fail();
        } finally {
            System.setOut(savedOut);
        }
    }

    @Test
    public void testInlineVariablesMultiple() {
        PrintStream savedOut = System.out;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(out)) {
            System.setOut(ps);

            Lexer lexer = new Lexer("func Main() { int i = 50 WriteLine(\"i = $i\") i = 100 WriteLine(\"i = $i\") }");
            Parser parser = new Parser(lexer);

            Interpreter interpreter = new Interpreter(parser);
            interpreter.interpret();

            System.setOut(savedOut);

            String output = out.toString("UTF-8");
            System.out.println(output);
            String[] lines = output.split("\n");
            String firstActual = lines[lines.length - 4];
            String secondActual = lines[lines.length - 2];

            assertEquals("i = 50", firstActual.substring(0, firstActual.length() - 1));
            assertEquals("i = 100", secondActual.substring(0, secondActual.length() - 1));
        } catch (Exception e) {
            fail();
        } finally {
            System.setOut(savedOut);
        }
    }
}
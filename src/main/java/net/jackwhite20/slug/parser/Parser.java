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
import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.interpreter.FunctionRegistry;
import net.jackwhite20.slug.lexer.Lexer;
import net.jackwhite20.slug.lexer.Token;
import net.jackwhite20.slug.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class Parser {

    private Lexer lexer;

    private Token currentToken;

    private BlockNode currentBlock = new MainBlockNode();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    private void eat(TokenType tokenType) {
        if (currentToken.getTokenType() == tokenType) {
            currentToken = lexer.nextToken();
        } else {
            // TODO: 12.02.2018 Remove when basic structure exists
            System.out.println("Expected: " + currentToken.getTokenType() + " Got: " + tokenType);
            throw new IllegalStateException();
        }
    }

    private Node parseFunction() {
        eat(TokenType.FUNC);
        String functionName = currentToken.getValue();
        // Call because CALL is used when the next char is "(", which is when calling a function (possible parameters etc.)
        eat(TokenType.CALL);

        eat(TokenType.LEFT_PARAN);
        // TODO: 12.02.2018 Parameters
        List<Node> parameters = new ArrayList<>();
        while (currentToken.getTokenType() != TokenType.RIGHT_PARAN) {
            Node node = parseDeclareOrAndAssignStatement();
            if (!(node instanceof VariableDeclarationNode)) {
                throw new SlugRuntimeException("parameters needs to be a variable declaration");
            }

            parameters.add(node);
        }
        eat(TokenType.RIGHT_PARAN);

        BlockNode blockNode = parseBlock();
        //eat(TokenType.CURLY_LEFT_PARAN);
        //List<Node> functionStatements = parseFunctionStatements();
        //eat(TokenType.CURLY_RIGHT_PARAN);
        // We need to check if the parent is null because the main block node parent is null
        if (currentBlock.getParent() != null) {
            currentBlock = currentBlock.getParent();
        }

        FunctionNode functionNode = new FunctionNode(functionName, blockNode, parameters);

        // Register the global function
        FunctionRegistry.register(functionNode);

        return functionNode;
    }

    private List<Node> parseFunctionStatements() {
        List<Node> results = new ArrayList<>();

        results.add(statement());

        // Add statements until the closing curly bracket from the function end is found
        while (currentToken.getTokenType() != TokenType.CURLY_RIGHT_PARAN) {
            results.add(statement());
        }

        return results;
    }

    private Node parseFunctionCall() {
        // Get the function name from the CALL token (the name is there as the value)
        String name = currentToken.getValue();

        eat(TokenType.CALL);

        eat(TokenType.LEFT_PARAN);

        List<Node> parameter = new ArrayList<>();
        while (currentToken.getTokenType() != TokenType.RIGHT_PARAN) {
            parameter.add(factor());

            // Parameters should be separated with a comma
            if (currentToken.getTokenType() == TokenType.COMMA) {
                eat(TokenType.COMMA);
            }
        }

        eat(TokenType.RIGHT_PARAN);

        FunctionNode functionNodeToCall = FunctionRegistry.lookup(name);

        // Do not continue if the function to call does not exists
        if (functionNodeToCall == null) {
            throw new SlugRuntimeException("function " + name + " does not exists");
        }

        return new FunctionCallNode(name, functionNodeToCall, parameter);
    }

    private Node parseDeclareOrAndAssignStatement() {
        TokenType varType = currentToken.getTokenType();
        eat(varType);

        String varName = currentToken.getValue();
        eat(TokenType.NAME);

        if (currentToken.getTokenType() != TokenType.ASSIGN) {
            return new VariableDeclarationNode(varName, varType);
        } else {
            eat(TokenType.ASSIGN);
            return new VariableDeclarationAssignNode(varName, varType, expression());
        }
    }

    private Node parseVariableAssign() {
        String varName = currentToken.getValue();
        eat(TokenType.NAME);
        eat(TokenType.ASSIGN);

        Node right = expression();

        return new VariableAssignNode(varName, right);
    }

    private Node parseVariableUsage() {
        Token variableNameToken = currentToken;
        eat(TokenType.NAME);
        return new VariableUsageNode(variableNameToken);
    }

    private Node parseIf() {
        eat(TokenType.IF);

        eat(TokenType.LEFT_PARAN);
        Node expression = expression();
        eat(TokenType.RIGHT_PARAN);

        BlockNode trueBlock;
        BlockNode falseBlock = null;

        trueBlock = parseBlock();

        // Handle else block
        if (currentToken.getTokenType() == TokenType.ELSE) {
            eat(TokenType.ELSE);

            falseBlock = parseBlock();
        }

        return new IfNode(expression, trueBlock, falseBlock);
    }

    private Node parseWhile() {
        eat(TokenType.WHILE);

        eat(TokenType.LEFT_PARAN);
        Node expression = expression();
        eat(TokenType.RIGHT_PARAN);

        eat(TokenType.CURLY_LEFT_PARAN);

        List<Node> children = new ArrayList<>();

        while (currentToken.getTokenType() != TokenType.CURLY_RIGHT_PARAN) {
            // Add all true node statements
            children.add(statement());
        }

        eat(TokenType.CURLY_RIGHT_PARAN);

        return new WhileNode(expression, children);
    }

    private Node parseFor() {
        eat(TokenType.FOR);
        eat(TokenType.LEFT_PARAN);

        Node declaration = parseDeclareOrAndAssignStatement();
        eat(TokenType.SEMICOLON);
        Node condition = term();
        if (!(condition instanceof BooleanNode)) {
            throw new SlugRuntimeException("for condition needs to be a boolean node");
        }
        eat(TokenType.SEMICOLON);
        Node expression = parseVariableAssign();

        eat(TokenType.RIGHT_PARAN);

        BlockNode blockNode = parseBlock();

        return new ForNode(declaration, condition, expression, blockNode);
    }

    private BlockNode parseBlock() {
        currentBlock = new BlockNode(currentBlock);

        eat(TokenType.CURLY_LEFT_PARAN);
        List<Node> functionStatements = parseFunctionStatements();
        eat(TokenType.CURLY_RIGHT_PARAN);

        currentBlock.setStatements(functionStatements);

        BlockNode localBlockNode = currentBlock;
        currentBlock = currentBlock.getParent();

        return localBlockNode;
    }

    private Node statement() {
        Node node;

        if (currentToken.getTokenType() == TokenType.FUNC) {
            node = parseFunction();
        } else if (currentToken.getTokenType() == TokenType.INTEGER ||
                currentToken.getTokenType() == TokenType.STRING ||
                currentToken.getTokenType() == TokenType.BOOL) {
            // Here we need to difference between an assign and declare variable statement
            // Example:
            // int i
            // or
            // int i = 5
            node = parseDeclareOrAndAssignStatement();
        } else if (currentToken.getTokenType() == TokenType.NAME) {
            // Here we have a variable assign on a previous declared variable
            // Example:
            // i = 150
            node = parseVariableAssign();
        } else if (currentToken.getTokenType() == TokenType.CALL) {
            node = parseFunctionCall();
        } else if (currentToken.getTokenType() == TokenType.IF) {
            node = parseIf();
        } else if (currentToken.getTokenType() == TokenType.WHILE) {
            node = parseWhile();
        } else if (currentToken.getTokenType() == TokenType.FOR) {
            node = parseFor();
        } else {
            node = new NoOpNode();
        }

        return node;
    }

    private Node factor() {
        Token tmp = currentToken;
        if (tmp.getTokenType() == TokenType.PLUS) {
            eat(TokenType.PLUS);
            return new UnaryNode(tmp, factor());
        } else if (tmp.getTokenType() == TokenType.MINUS) {
            eat(TokenType.MINUS);
            return new UnaryNode(tmp, factor());
        } else if (tmp.getTokenType() == TokenType.INTEGER) {
            eat(TokenType.INTEGER);
            return new NumberNode(tmp.getValue());
        } else if (tmp.getTokenType() == TokenType.STRING) {
            eat(TokenType.STRING);
            return new StringNode(tmp.getValue());
        } else if (tmp.getTokenType() == TokenType.BOOL) {
            eat(TokenType.BOOL);
            return new BoolNode(tmp.getValue());
        } else if (tmp.getTokenType() == TokenType.LEFT_PARAN) {
            eat(TokenType.LEFT_PARAN);
            Node node = expression();
            eat(TokenType.RIGHT_PARAN);
            return node;
        } else if (tmp.getTokenType() == TokenType.CALL) {
            return parseFunctionCall();
        } else {
            return parseVariableUsage();
        }
    }

    private Node term() {
        Node res = factor();

        while (currentToken.getTokenType() == TokenType.MULTIPLY ||
                currentToken.getTokenType() == TokenType.DIVIDE ||
                currentToken.getTokenType() == TokenType.GREATER ||
                currentToken.getTokenType() == TokenType.LESS ||
                currentToken.getTokenType() == TokenType.GREATER_EQUAL ||
                currentToken.getTokenType() == TokenType.LESS_EQUAL ||
                currentToken.getTokenType() == TokenType.NOT_EQUAL ||
                currentToken.getTokenType() == TokenType.EQUAL) {
            Token tmp = currentToken;
            if (tmp.getTokenType() == TokenType.MULTIPLY) {
                eat(TokenType.MULTIPLY);
            } else if (tmp.getTokenType() == TokenType.DIVIDE) {
                eat(TokenType.DIVIDE);
            } else {
                // Handle boolean operators (>=, >, <, etc.)
                eat(tmp.getTokenType());

                // We got a boolean operation here
                res = new BooleanNode(res, tmp.getTokenType(), factor());
                continue;
            }

            res = new BinaryNode(res, tmp.getTokenType(), factor());
        }

        return res;
    }

    private Node expression() {
        Node result = term();

        while (currentToken.getTokenType() == TokenType.PLUS || currentToken.getTokenType() == TokenType.MINUS) {
            Token tmp = currentToken;
            if (tmp.getTokenType() == TokenType.PLUS) {
                eat(TokenType.PLUS);
            } else if (tmp.getTokenType() == TokenType.MINUS) {
                eat(TokenType.MINUS);
            } else if (tmp.getTokenType() == TokenType.MULTIPLY) {
                eat(TokenType.MULTIPLY);
            } else if (tmp.getTokenType() == TokenType.DIVIDE) {
                eat(TokenType.DIVIDE);
            }

            result = new BinaryNode(result, tmp.getTokenType(), term());
        }

        return result;
    }

    private Node parseSlugMainFile() {
        // A SLUG file can start with possible global variables
        List<Node> globalVariables = new ArrayList<>();
        while (Token.isVariable(currentToken.getTokenType())) {
            globalVariables.add(parseDeclareOrAndAssignStatement());
        }

        List<Node> functions = new ArrayList<>();

        // Parse function until we have all parsed
        while (currentToken.getTokenType() == TokenType.FUNC) {
            functions.add(parseFunction());
        }

        return new MainNode(globalVariables, functions);
    }

    /**
     * This starts the parsing process which will generate an AST (Abstract Syntax Tree).
     *
     * @return The interpretable AST.
     */
    public Node parse() {
        return parseSlugMainFile();
    }
}

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
        eat(TokenType.RIGHT_PARAN);

        eat(TokenType.CURLY_LEFT_PARAN);
        List<Node> functionStatements = parseFunctionStatements();
        eat(TokenType.CURLY_RIGHT_PARAN);

        FunctionNode functionNode = new FunctionNode(functionName, functionStatements, parameters);

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

        return new FunctionCallNode(name, FunctionRegistry.lookup(name), parameter);
    }

    private Node statement() {
        Node node;

        if (currentToken.getTokenType() == TokenType.FUNC) {
            node = parseFunction();
        } else if (currentToken.getTokenType() == TokenType.CALL) {
            node = parseFunctionCall();
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
            // TODO: 13.02.2018 Variable usage
            return null;
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

            res = new BinaryNode(res, tmp, factor());
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

            result = new BinaryNode(result, tmp, term());
        }

        return result;
    }

    private Node parseSlugMainFile() {
        List<Node> functions = new ArrayList<>();

        // Parse function until we have all parsed
        while (currentToken.getTokenType() == TokenType.FUNC) {
            functions.add(parseFunction());
        }

        return new MainNode(functions);
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

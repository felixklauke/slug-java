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
        // TODO: 12.02.2018 Function statements
        List<Node> functionStatements = new ArrayList<>();
        eat(TokenType.CURLY_RIGHT_PARAN);

        return new FunctionNode(functionName, functionStatements, parameters);
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

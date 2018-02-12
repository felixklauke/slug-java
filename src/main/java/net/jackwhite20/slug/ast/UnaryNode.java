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

import net.jackwhite20.slug.lexer.Token;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class UnaryNode extends Node {

    private Token operator;

    private Node expression;

    public UnaryNode(Token operator, Node expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public Token getOperator() {
        return operator;
    }

    public Node getExpression() {
        return expression;
    }
}

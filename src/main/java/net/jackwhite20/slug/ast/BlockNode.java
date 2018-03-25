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

import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.interpreter.variable.BlockVariableRegistry;
import net.jackwhite20.slug.lexer.TokenType;

import java.util.List;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class BlockNode extends Node {

    private BlockNode parent;

    private List<Node> statements;

    private BlockVariableRegistry variableRegistry;

    public BlockNode(BlockNode parent, List<Node> statements) {
        this.parent = parent;
        this.statements = statements;
        this.variableRegistry = new BlockVariableRegistry("Block");
    }

    public BlockNode(BlockNode parent) {
        this(parent, null);
    }

    public void registerVariable(String variableName, TokenType variableType, Object value) {
        // Only register if the variable isn't registered in an upper scope/block
        if (lookupVariable(variableName) != null) {
            throw new SlugRuntimeException("variable " + variableName + " already exists");
        }

        variableRegistry.register(variableName, variableType, value);
    }

    public <T> T lookupVariable(String name) {
        Object lookup = variableRegistry.lookup(name);
        if (lookup != null) {
            //noinspection unchecked
            return (T) lookup;
        }

        if (parent != null) {
            return parent.lookupVariable(name);
        }

        throw new IllegalStateException("no main block node");
    }


    public void updateVariable(String variableName, Object value) {
        if (variableRegistry.lookup(variableName) != null) {
            variableRegistry.update(variableName, value);
            return;
        }

        if (parent != null) {
            parent.updateVariable(variableName, value);
            return;
        }

        throw new IllegalStateException("no main block node");
    }

    public BlockNode getParent() {
        return parent;
    }

    public void setParent(BlockNode parent) {
        this.parent = parent;
    }

    public List<Node> getStatements() {
        return statements;
    }

    public void setStatements(List<Node> statements) {
        this.statements = statements;
    }
}

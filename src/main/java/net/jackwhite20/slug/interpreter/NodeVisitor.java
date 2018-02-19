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

import net.jackwhite20.slug.ast.*;
import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.interpreter.variable.GlobalVariableRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NodeVisitor {

    private static Logger logger = LoggerFactory.getLogger(NodeVisitor.class);

    private GlobalVariableRegistry globalVariableRegistry = new GlobalVariableRegistry();

    /**
     * Visits the NumberNode and returns it's value.
     *
     * @param numberNode The number node.
     * @return The value the number node holds.
     */
    private int visitNumber(NumberNode numberNode) {
        return numberNode.getValue();
    }

    private String visitString(StringNode stringNode) {
        return stringNode.getValue();
    }

    private boolean visitBool(BoolNode node) {
        return node.getValue();
    }

    private void visitMain(MainNode node) {
        if (node.getFunctions().size() == 0) {
            throw new SlugRuntimeException("no functions specified");
        }

        FunctionNode mainFunction = (FunctionNode) node.getFunctions().get(node.getFunctions().size() - 1);

        if (!mainFunction.getName().equals("Main")) {
            throw new SlugRuntimeException("the 'Main' function needs to be the last function");
        }

        // Visit the main function
        visitFunction(mainFunction);
    }

    private void visitFunction(FunctionNode functionNode) {
        for (Node node : functionNode.getChildren()) {
            visit(node);
        }
    }

    private void visitVariableDeclarationAssign(VariableDeclarationAssignNode node) {
        // Get the value from the right expression
        Object value = visit(node.getRight());

        globalVariableRegistry.register(node.getVariableName(), node.getVariableType(), value);
    }

    private void visitVariableAssign(VariableAssignNode node) {
        // Get the value from the right expression
        Object value = visit(node.getRight());

        globalVariableRegistry.update(node.getVariableName(), value);
    }

    Object visit(Node node) {
        if (node instanceof MainNode) {
            visitMain(((MainNode) node));
            return null;
        } else if (node instanceof NumberNode) {
            return visitNumber(((NumberNode) node));
        } else if (node instanceof StringNode) {
            return visitString((StringNode) node);
        } else if (node instanceof BoolNode) {
            return visitBool((BoolNode) node);
        } else if (node instanceof VariableDeclarationAssignNode) {
            visitVariableDeclarationAssign(((VariableDeclarationAssignNode) node));
            return null;
        } else if (node instanceof VariableAssignNode) {
            visitVariableAssign(((VariableAssignNode) node));
            return null;
        }

        logger.error("Error on visit, unhandled node {}", node.getClass().getName());

        return null;
    }

    public GlobalVariableRegistry getGlobalVariableRegistry() {
        return globalVariableRegistry;
    }
}

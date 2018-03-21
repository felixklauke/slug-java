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
import net.jackwhite20.slug.lexer.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NodeVisitor {

    private static Logger logger = LoggerFactory.getLogger(NodeVisitor.class);

    private BlockNode currentBlock = MainBlockNode.getInstance();

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
        // Register possible global variables
        for (Node globalVar : node.getGlobalVariables()) {
            visit(globalVar);
        }

        if (node.getFunctions().size() == 0 && node.getGlobalVariables().size() == 0) {
            throw new SlugRuntimeException("no functions and global variables");
        }

        // Only search main function and start interpreting if functions are available
        if (node.getFunctions().size() > 0) {
            FunctionNode mainFunction = (FunctionNode) node.getFunctions().get(node.getFunctions().size() - 1);

            if (!mainFunction.getName().equals("Main")) {
                throw new SlugRuntimeException("the 'Main' function needs to be the last function");
            }

            // Visit the main function
            visitFunction(mainFunction);
        }
    }

    private void visitFunction(FunctionNode functionNode) {
        visit(functionNode.getBlock());
        /*for (Node node : functionNode.getBlock()) {
            visit(node);
        }*/
    }

    private void visitVariableDeclarationAssign(VariableDeclarationAssignNode node) {
        // Get the value from the right expression
        Object value = visit(node.getRight());

        currentBlock.registerVariable(node.getVariableName(), node.getVariableType(), value);
    }

    private void visitVariableAssign(VariableAssignNode node) {
        // Get the value from the right expression
        Object value = visit(node.getRight());

        currentBlock.updateVariable(node.getVariableName(), value);
    }

    private Object visitVariableUsage(VariableUsageNode node) {
        String variableName = node.getVariableName();

        return currentBlock.lookupVariable(variableName);
    }

    private Object visitBinary(BinaryNode node) {
        if (node.getOperator() == TokenType.MINUS) {
            return (int) visit(node.getLeft()) - (int) visit(node.getRight());
        } else if (node.getOperator() == TokenType.PLUS) {
            return (int) visit(node.getLeft()) + (int) visit(node.getRight());
        } else if (node.getOperator() == TokenType.MULTIPLY) {
            return (int) visit(node.getLeft()) * (int) visit(node.getRight());
        } else if (node.getOperator() == TokenType.DIVIDE) {
            return (int) visit(node.getLeft()) / (int) visit(node.getRight());
        }

        throw new SlugRuntimeException("unhandled binary operator " + node.getOperator());
    }

    private boolean visitBoolean(BooleanNode node) {
        // TODO: Better handling and more supported types
        int a = (int) visit(node.getLeft());
        int b = (int) visit(node.getRight());

        if (node.getOperator() == TokenType.EQUAL) {
            return a == b;
        } else if (node.getOperator() == TokenType.GREATER) {
            return a > b;
        } else if (node.getOperator() == TokenType.LESS) {
            return a < b;
        } else if (node.getOperator() == TokenType.GREATER_EQUAL) {
            return a >= b;
        } else if (node.getOperator() == TokenType.LESS_EQUAL) {
            return a <= b;
        } else if (node.getOperator() == TokenType.NOT_EQUAL) {
            return a != b;
        }

        return false;
    }

    private void visitIf(IfNode node) {
        Node expression = node.getExpression();

        // The expression from the if node needs to be a boolean node
        if (!(expression instanceof BooleanNode)) {
            throw new SlugRuntimeException("the expression from an if node needs to be a boolean node");
        }

        boolean state = visitBoolean((BooleanNode) expression);

        if (state) {
            /*for (Node trueNode : node.getTrueBlock()) {
                visit(trueNode);
            }*/
            visitBlock(node.getTrueBlock());
        } else {
            for (Node falseNode : node.getFalseNodes()) {
                visit(falseNode);
            }
        }
    }

    private void visitWhile(WhileNode node) {
        if (!(node.getExpression() instanceof BooleanNode)) {
            throw new SlugRuntimeException("the while expression need to be a boolean node");
        }

        while (visitBoolean((BooleanNode) node.getExpression())) {
            for (Node children : node.getChildren()) {
                visit(children);
            }
        }
    }

    private void visitFor(ForNode node) {
        visit(node.getDeclaration());

        while (visitBoolean((BooleanNode) node.getCondition())) {
            visitBlock(node.getBlock());

            // At the end visit the expression to eg. increase the variable used in the declaration
            visit(node.getExpression());
        }
    }

    private void visitBlock(BlockNode node) {
        // Update the current block to the new one
        currentBlock = node;

        for (Node statement : node.getStatements()) {
            visit(statement);
        }

        // We left the block, so set the current block to the parent of this block
        currentBlock = currentBlock.getParent();
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
        } else if (node instanceof BinaryNode) {
            return visitBinary((BinaryNode) node);
        } else if (node instanceof VariableDeclarationAssignNode) {
            visitVariableDeclarationAssign(((VariableDeclarationAssignNode) node));
            return null;
        } else if (node instanceof VariableAssignNode) {
            visitVariableAssign(((VariableAssignNode) node));
            return null;
        } else if (node instanceof VariableUsageNode) {
            return visitVariableUsage(((VariableUsageNode) node));
        } else if (node instanceof BooleanNode) {
            return visitBoolean(((BooleanNode) node));
        } else if (node instanceof IfNode) {
            visitIf(((IfNode) node));
            return null;
        } else if (node instanceof WhileNode) {
            visitWhile(((WhileNode) node));
            return null;
        } else if (node instanceof ForNode) {
            visitFor(((ForNode) node));
            return null;
        } else if (node instanceof BlockNode) {
            visitBlock((BlockNode) node);
            return null;
        } else if (node instanceof NoOpNode) {
            // Do nothing (no op)
            return null;
        }

        logger.error("Error on visit, unhandled node {}", node.getClass().getName());

        return null;
    }
}

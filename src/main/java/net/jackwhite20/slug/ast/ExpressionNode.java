package net.jackwhite20.slug.ast;

import net.jackwhite20.slug.lexer.TokenType;

public abstract class ExpressionNode extends Node {

    private Node left;

    private TokenType operator;

    private Node right;

    ExpressionNode(Node left, TokenType operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public TokenType getOperator() {
        return operator;
    }

    public Node getRight() {
        return right;
    }
}

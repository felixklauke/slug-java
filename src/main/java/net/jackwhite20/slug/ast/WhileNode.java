package net.jackwhite20.slug.ast;

import java.util.List;

public class WhileNode extends Node {

    private Node expression;

    private List<Node> children;

    public WhileNode(Node expression, List<Node> children) {
        this.expression = expression;
        this.children = children;
    }

    public Node getExpression() {
        return expression;
    }

    public List<Node> getChildren() {
        return children;
    }
}

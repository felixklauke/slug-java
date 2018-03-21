package net.jackwhite20.slug.ast;

import java.util.List;

public class IfNode extends Node {

    private Node expression;

    private BlockNode trueNodes;

    private List<Node> falseNodes;

    public IfNode(Node expression, BlockNode trueNodes, List<Node> falseNodes) {
        this.expression = expression;
        this.trueNodes = trueNodes;
        this.falseNodes = falseNodes;
    }

    public Node getExpression() {
        return expression;
    }

    public BlockNode getTrueBlock() {
        return trueNodes;
    }

    public List<Node> getFalseNodes() {
        return falseNodes;
    }
}

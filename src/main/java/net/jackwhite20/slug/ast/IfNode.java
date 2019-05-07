package net.jackwhite20.slug.ast;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class IfNode extends Node {

    private Node expression;

    private BlockNode trueNodes;

    private BlockNode falseNodes;

    public IfNode(Node expression, BlockNode trueNodes, BlockNode falseNodes) {
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

    public BlockNode getFalseBlock() {
        return falseNodes;
    }
}

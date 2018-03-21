package net.jackwhite20.slug.ast;

public class ForNode extends Node {

    private Node declaration;

    private Node condition;

    private Node expression;

    private BlockNode block;

    public ForNode(Node declaration, Node condition, Node expression, BlockNode block) {
        this.declaration = declaration;
        this.condition = condition;
        this.expression = expression;
        this.block = block;
    }

    public Node getDeclaration() {
        return declaration;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getExpression() {
        return expression;
    }

    public BlockNode getBlock() {
        return block;
    }
}

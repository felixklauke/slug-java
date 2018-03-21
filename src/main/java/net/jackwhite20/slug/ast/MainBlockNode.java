package net.jackwhite20.slug.ast;

import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.interpreter.variable.GlobalVariableRegistry;
import net.jackwhite20.slug.lexer.TokenType;

public class MainBlockNode extends BlockNode {

    public MainBlockNode() {
        super(null);
    }

    @Override
    public void registerVariable(String variableName, TokenType variableType, Object value) {
        // Only register if the variable isn't registered as a global register
        if (GlobalVariableRegistry.getInstance().lookup(variableName) != null) {
            throw new SlugRuntimeException("variable " + variableName + " already exists");
        }

        GlobalVariableRegistry.getInstance().register(variableName, variableType, value);
    }

    @Override
    public Object lookupVariable(String name) {
        return GlobalVariableRegistry.getInstance().lookup(name);
    }

    @Override
    public void updateVariable(String variableName, Object value) {
        if (GlobalVariableRegistry.getInstance().lookup(variableName) != null) {
            GlobalVariableRegistry.getInstance().update(variableName, value);
            return;
        }

        throw new SlugRuntimeException("variable " + variableName + " not found");
    }
}

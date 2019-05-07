package net.jackwhite20.slug.ast;

import lombok.Getter;
import net.jackwhite20.slug.exception.SlugRuntimeException;
import net.jackwhite20.slug.exception.VariableNotFoundException;
import net.jackwhite20.slug.interpreter.variable.GlobalVariableRegistry;
import net.jackwhite20.slug.lexer.TokenType;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class MainBlockNode extends BlockNode {

    @Getter
    private static MainBlockNode instance;
    private GlobalVariableRegistry globalVariableRegistry;

    public MainBlockNode() {
        super(null);

        instance = this;

        this.globalVariableRegistry = new GlobalVariableRegistry();
    }

    @Override
    public void registerVariable(String variableName, TokenType variableType, Object value) {
        // Only register if the variable isn't registered as a global register
        if (globalVariableRegistry.lookup(variableName) != null) {
            throw new SlugRuntimeException("variable " + variableName + " already exists");
        }

        globalVariableRegistry.register(variableName, variableType, value);
    }

    @Override
    public <T> T lookupVariable(String name) {
        return globalVariableRegistry.lookup(name);
    }

    @Override
    public void updateVariable(String variableName, Object value) {
        if (globalVariableRegistry.lookup(variableName) != null) {
            globalVariableRegistry.update(variableName, value);
            return;
        }

        throw new VariableNotFoundException(variableName);
    }
}

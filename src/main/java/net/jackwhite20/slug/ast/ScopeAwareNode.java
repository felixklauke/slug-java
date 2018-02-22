package net.jackwhite20.slug.ast;

/**
 * A specific node that is aware of its current scope level stored in the {@link #currentScopeLevel}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class ScopeAwareNode extends Node {

    /**
     * The current scope level of the parsed node.
     */
    private final int currentScopeLevel;

    /**
     * Create a new block node by its current scope level.
     *
     * @param currentScopeLevel The scope level.
     */
    public ScopeAwareNode(int currentScopeLevel) {
        this.currentScopeLevel = currentScopeLevel;
    }

    /**
     * Get the current scope level of the node.
     *
     * @return The scope level.
     */
    public int getCurrentScopeLevel() {
        return currentScopeLevel;
    }
}

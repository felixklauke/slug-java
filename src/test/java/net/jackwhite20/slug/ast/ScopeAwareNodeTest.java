package net.jackwhite20.slug.ast;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class ScopeAwareNodeTest {

    private static final int TEST_SCOPE_LEVEL = 80000;
    private ScopeAwareNode blockNode;

    @Before
    public void setUp() {
        blockNode = new ScopeAwareNode(TEST_SCOPE_LEVEL);
    }

    @Test
    public void getCurrentScopeLevel() {
        assertEquals(TEST_SCOPE_LEVEL, blockNode.getCurrentScopeLevel());
    }
}
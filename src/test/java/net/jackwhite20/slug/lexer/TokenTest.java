package net.jackwhite20.slug.lexer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The test for the {@link Token}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class TokenTest {

    /**
     * The test value that represents the token in the code.
     */
    private static final String TEST_TOKEN_TYPE_VALUE = ",";

    /**
     * The test token type that represents the token in its formal context.
     */
    private static final TokenType TEST_TOKEN_TYPE = TokenType.COMMA;

    /**
     * The token build with {@link #TEST_TOKEN_TYPE_VALUE} and {@link #TEST_TOKEN_TYPE}.
     */
    private Token token;

    @Before
    public void setUp() {
        token = new Token(TEST_TOKEN_TYPE, TEST_TOKEN_TYPE_VALUE);
    }

    @Test
    public void getTokenType() {
        assertEquals(TEST_TOKEN_TYPE_VALUE, token.getValue());
    }

    @Test
    public void getValue() {
        assertEquals(TEST_TOKEN_TYPE, token.getTokenType());
    }
}
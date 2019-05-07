package net.jackwhite20.slug.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.jackwhite20.slug.lexer.TokenType;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 * @author Felix Klauke <info@felix-klauke.de>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ExpressionNode extends Node {

    private final Node left;
    private final TokenType operator;
    private final Node right;
}

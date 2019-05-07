package net.jackwhite20.slug.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 * @author Felix Klauke <info@felix-klauke.de>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IfNode extends Node {

    private final Node expression;
    private final BlockNode trueNodes;
    private final BlockNode falseNodes;
}

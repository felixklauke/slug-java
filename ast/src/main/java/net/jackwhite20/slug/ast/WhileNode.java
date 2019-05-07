package net.jackwhite20.slug.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 * @author Felix Klauke <info@felix-klauke.de>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WhileNode extends Node {

    private final Node expression;
    private final List<Node> children;
}

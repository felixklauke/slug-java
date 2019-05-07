/*
 * Copyright 2018 "JackWhite20"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jackwhite20.slug.interpreter;

import net.jackwhite20.slug.ast.Node;
import net.jackwhite20.slug.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class Interpreter extends NodeVisitor {

    private static Logger logger = LoggerFactory.getLogger(Interpreter.class);

    private Parser parser;

    public Interpreter(Parser parser) {
        this.parser = parser;
    }

    public void interpret() {
        long now = System.currentTimeMillis();
        Node tree = parser.parse();
        logger.debug("Parse time: " + (System.currentTimeMillis() - now) + "ms");

        now = System.nanoTime();

        // Start the visiting (interpreting process)
        visit(tree);

        long time = System.nanoTime() - now;

        logger.debug("Interpreted time: " + TimeUnit.NANOSECONDS.toMillis(time) + "ms (" + time + "ns)");
    }
}

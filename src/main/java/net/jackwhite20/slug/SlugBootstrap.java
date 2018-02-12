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

package net.jackwhite20.slug;

import net.jackwhite20.slug.ast.Node;
import net.jackwhite20.slug.lexer.Lexer;
import net.jackwhite20.slug.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class SlugBootstrap {

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.exists()) {
                try {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    String source = new String(bytes, Charset.forName("UTF-8"));
                    Lexer lexer = new Lexer(source);

                    Parser parser = new Parser(lexer);

                    Node ast = parser.parse();

                    // TODO: 13.02.2018 Interpreting
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Slug source file '" + args[0] + "' does not exist");
            }
        } else {
            System.err.println("Usage: java -jar slug.jar [Slug Source File]");
        }
    }
}

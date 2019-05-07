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

package net.jackwhite20.slug.ast;

import java.util.List;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class MainNode extends Node {

    private List<Node> globalVariables;
    private List<Node> functions;

    public MainNode(List<Node> globalVariables, List<Node> functions) {
        this.globalVariables = globalVariables;
        this.functions = functions;
    }

    public List<Node> getGlobalVariables() {
        return globalVariables;
    }

    public List<Node> getFunctions() {
        return functions;
    }
}

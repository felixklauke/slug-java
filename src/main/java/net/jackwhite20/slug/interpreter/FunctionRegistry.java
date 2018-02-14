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

import net.jackwhite20.slug.ast.FunctionNode;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistry {

    private static Map<String, FunctionNode> functions = new HashMap<>();

    public static void register(FunctionNode functionNode) {
        functions.put(functionNode.getName(), functionNode);
    }

    public static FunctionNode lookup(String functionName) {
        return functions.get(functionName);
    }
}

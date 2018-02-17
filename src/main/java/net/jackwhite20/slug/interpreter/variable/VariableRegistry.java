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

package net.jackwhite20.slug.interpreter.variable;

import net.jackwhite20.slug.interpreter.FunctionRegistry;
import net.jackwhite20.slug.lexer.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class VariableRegistry {

    private static Logger logger = LoggerFactory.getLogger(FunctionRegistry.class);

    private Map<String, Object> variables = new HashMap<>();

    private Map<String, TokenType> variableTypes = new HashMap<>();

    public void register(String name, TokenType variableType, Object value) {
        variables.put(name, value);
        variableTypes.put(name, variableType);

        logger.debug("Registered variable {} ({})", name, variableType);
    }

    public <T> T lookup(String name) {
        //noinspection unchecked
        return (T) variables.get(name);
    }

    public boolean checkType(String name, TokenType expectedVarType) {
        TokenType tokenType = variableTypes.get(name);

        return tokenType != null && tokenType == expectedVarType;
    }
}

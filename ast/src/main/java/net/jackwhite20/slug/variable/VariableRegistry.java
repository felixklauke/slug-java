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

package net.jackwhite20.slug.variable;

import lombok.Getter;
import net.jackwhite20.slug.lexer.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public abstract class VariableRegistry {

    private static Logger logger = LoggerFactory.getLogger(VariableRegistry.class);
    private static Map<TokenType, Object> DEFAULT_VALUES = new HashMap<>();

    static {
        DEFAULT_VALUES.put(TokenType.INTEGER, 0);
        DEFAULT_VALUES.put(TokenType.STRING, "");
        DEFAULT_VALUES.put(TokenType.BOOL, false);
    }

    @Getter
    private String name;

    private Map<String, Object> variables = new HashMap<>();

    private Map<String, TokenType> variableTypes = new HashMap<>();

    public VariableRegistry(String name) {
        this.name = name;
    }

    public void register(String variableName, TokenType variableType, Object value) {
        // Use default value if the value is null
        if (value == null) {
            value = DEFAULT_VALUES.get(variableType);
        }

        variables.put(variableName, value);
        variableTypes.put(variableName, variableType);

        logger.debug("Registered variable {} ({}) with value {}", variableName, variableType, value);
    }

    public void update(String variableName, Object newValue) {
        variables.put(variableName, newValue);

        logger.debug("Updated variable {} with the new value {}", variableName, newValue);
    }

    public <T> T lookup(String name) {
        //noinspection unchecked
        return (T) variables.get(name);
    }

    public boolean checkType(String variableName, TokenType expectedVarType) {
        TokenType tokenType = variableTypes.get(variableName);

        return tokenType != null && tokenType == expectedVarType;
    }
}

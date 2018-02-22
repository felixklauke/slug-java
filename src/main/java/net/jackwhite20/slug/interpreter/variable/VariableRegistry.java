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

import net.jackwhite20.slug.lexer.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public abstract class VariableRegistry {

    private static Logger logger = LoggerFactory.getLogger(VariableRegistry.class);

    private String name;

    private Map<String, Object> variables = new HashMap<>();

    private Map<String, TokenType> variableTypes = new HashMap<>();

    private Map<String, Integer> variableScopeLevels = new HashMap<>();

    public VariableRegistry(String name) {
        this.name = name;
    }

    public void register(int currentScopeLevel, String variableName, TokenType variableType, Object value) {
        variables.put(variableName, value);
        variableTypes.put(variableName, variableType);
        variableScopeLevels.put(variableName, currentScopeLevel);

        logger.debug("Registered variable {} ({})", variableName, variableType);
    }

    public void update(int currentScopeLevel, String variableName, Object newValue) {
        int scopeLevel = variableScopeLevels.get(variableName);

        if (currentScopeLevel < scopeLevel) {
            throw new IllegalStateException("Illegal variable access.");
        }

        variables.put(variableName, newValue);

        logger.debug("Updated variable {} with the new value {}", variableName, newValue);
    }

    public <T> T lookup(int currentScopeLevel, String name) {
        //noinspection unchecked
        T instance = (T) variables.get(name);

        if (instance != null) {
            int scopeLevel = variableScopeLevels.get(name);

            if (currentScopeLevel < scopeLevel) {
                throw new IllegalStateException("Illegal variable access.");
            }
        }

        return instance;
    }

    public boolean checkType(String variableName, TokenType expectedVarType) {
        TokenType tokenType = variableTypes.get(variableName);

        return tokenType != null && tokenType == expectedVarType;
    }

    public String getName() {
        return name;
    }
}

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

import net.jackwhite20.slug.lexer.TokenType;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class VariableDeclarationNode extends ScopeAwareNode {

    private String variableName;

    private TokenType variableType;

    public VariableDeclarationNode(int currentScopeLevel, String variableName, TokenType variableType) {
        super(currentScopeLevel);
        this.variableName = variableName;
        this.variableType = variableType;
    }

    public String getVariableName() {
        return variableName;
    }

    public TokenType getVariableType() {
        return variableType;
    }
}

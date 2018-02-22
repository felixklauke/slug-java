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

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class VariableAssignNode extends ScopeAwareNode {

    private String variableName;

    private Node right;

    public VariableAssignNode(int currentScopeLevel, String variableName, Node right) {
        super(currentScopeLevel);
        this.variableName = variableName;
        this.right = right;
    }

    public String getVariableName() {
        return variableName;
    }

    public Node getRight() {
        return right;
    }
}

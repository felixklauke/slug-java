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
import org.junit.Test;

import static org.junit.Assert.*;

public class GlobalVariableRegistryTest {

    private static final String VAR_NAME = "test";

    private static final TokenType VAR_TYPE = TokenType.INTEGER;

    private static final Object VAR_VALUE = 246;

    private static final Object VAR_VALUE_NEW = 128;

    private static final String GLOBAL_VARIABLE_REGISTRY_NAME = "Global";

    private static final GlobalVariableRegistry globalVariableRegistry = new GlobalVariableRegistry();

    @Test
    public void testRegister() {
        globalVariableRegistry.register(0, VAR_NAME, VAR_TYPE, VAR_VALUE);

        assertNotNull(globalVariableRegistry.lookup(0, VAR_NAME));
    }

    @Test
    public void testLookup() {
        Object varValue = globalVariableRegistry.lookup(0, VAR_NAME);

        assertNotNull(varValue);
        assertEquals(VAR_VALUE, varValue);
    }

    @Test
    public void testUpdate() {
        globalVariableRegistry.update(0, VAR_NAME, VAR_VALUE_NEW);

        Object varValue = globalVariableRegistry.lookup(0, VAR_NAME);

        assertNotNull(varValue);
        assertEquals(VAR_VALUE_NEW, varValue);
    }

    @Test
    public void testCheckType() {
        assertTrue(globalVariableRegistry.checkType(VAR_NAME, VAR_TYPE));
    }

    @Test
    public void testCheckTypeNonExistingVariable() {
        assertFalse(globalVariableRegistry.checkType("random", VAR_TYPE));
    }

    @Test
    public void testGetName() {
        assertNotNull(globalVariableRegistry.getName());
        assertEquals(GLOBAL_VARIABLE_REGISTRY_NAME, globalVariableRegistry.getName());
    }
}
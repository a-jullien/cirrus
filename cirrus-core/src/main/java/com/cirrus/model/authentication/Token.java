/*
 * *
 *  * Copyright (c) 2014 Antoine Jullien
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cirrus.model.authentication;



import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@SuppressWarnings("UnusedDeclaration")
@XmlRootElement
public class Token {

    //==================================================================================================================
    // Private
    //==================================================================================================================
    private final String tokenValue;

    //==================================================================================================================
    // Constructors
    //==================================================================================================================
    @JsonCreator
    public Token(@JsonProperty("tokenValue") final String tokenValue) {
        super();
        this.tokenValue = tokenValue;
    }

    public Token() {
        this(UUID.randomUUID().toString());
    }

    //==================================================================================================================
    // Getters
    //==================================================================================================================
    public String getTokenValue() {
        return tokenValue;
    }

    //==================================================================================================================
    // Override
    //==================================================================================================================

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Token token = (Token) o;

        if (tokenValue != null ? !tokenValue.equals(token.tokenValue) : token.tokenValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tokenValue != null ? tokenValue.hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.tokenValue;
    }
}

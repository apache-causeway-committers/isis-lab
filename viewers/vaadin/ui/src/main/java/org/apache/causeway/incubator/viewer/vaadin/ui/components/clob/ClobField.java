/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.incubator.viewer.vaadin.ui.components.clob;

import com.vaadin.flow.component.customfield.CustomField;

import org.jspecify.annotations.Nullable;

import org.apache.causeway.applib.value.Clob;

// TODO just a stub yet
//
public class ClobField extends CustomField<Clob> {

    private static final long serialVersionUID = 1L;

    private Clob clob;

    public ClobField(String label) {
        super();
        setLabel(label);
        // ...
    }

    @Override
    protected Clob generateModelValue() {
        return clob;
    }

    @Override
    protected void setPresentationValue(@Nullable Clob clob) {
        this.clob = clob;

        if(clob==null) {
            // ...
            return;
        }

        // ...
    }

}

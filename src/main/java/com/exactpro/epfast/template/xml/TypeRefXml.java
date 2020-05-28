/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
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
 ******************************************************************************/

package com.exactpro.epfast.template.xml;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;

public class TypeRefXml extends AbstractReferenceImpl {

    @Override
    public String getNamespace() {
        if (super.getNamespace() != null) {
            return super.getNamespace();
        }
        return getNamespaceProvider().getApplicationNamespace();
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        super.setName(name);
    }

    @XmlAttribute(name = "ns")
    public void setNamespace(String ns) {
        super.setNamespace(ns);
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            setNamespaceProvider((NamespaceProvider) parent);
        }
    }
}

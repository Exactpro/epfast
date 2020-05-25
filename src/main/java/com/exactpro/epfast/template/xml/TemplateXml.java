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

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Template;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "template")
public class TemplateXml extends InstructionsXml implements Template, NamespaceProvider {

    private AbstractNamespaceProvider nsProvider = new AbstractNamespaceProvider();

    private TemplateIdentity templateId = new TemplateIdentity(this);

    private ReferenceImpl typeRef = new ReferenceImpl(this);

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.templateId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.templateId.setAuxiliaryId(id);
    }

    @Override
    public String getTemplateNamespace() {
        return nsProvider.getTemplateNamespace();
    }

    @Override
    public String getApplicationNamespace() {
        return nsProvider.getApplicationNamespace();
    }

    @Override
    public Dictionary getDictionary() {
        return nsProvider.getDictionary();
    }

    @XmlAttribute(name = "templateNs")
    public void setTemplateNs(String templateNs) {
        nsProvider.setTemplateNs(templateNs);
    }

    @XmlAttribute(name = "ns")
    public void setApplicationNs(String ns) {
        nsProvider.setApplicationNs(ns);
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        nsProvider.setDictionaryName(dictionary);
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return typeRef;
    }

    @XmlAttribute(name = "typeRefName")
    public void setTypeRefName(String name) {
        typeRef.setName(name);
    }

    @XmlAttribute(name = "typeRefNs")
    public void setTypeRefNs(String ns) {
        typeRef.setNamespace(ns);
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        nsProvider.afterUnmarshal(unmarshaller, parent);
    }
}

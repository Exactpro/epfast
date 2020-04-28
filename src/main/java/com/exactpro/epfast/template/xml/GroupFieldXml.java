package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.*;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;

public class GroupFieldXml extends InstructionsXml implements Group, InstructionXml, NamespaceProvider {

    private NamespaceProvider parentNsProvider;

    private ApplicationIdentity fieldId = new ApplicationIdentity(this);

    private String localNamespace;

    private PresenceXml presence = PresenceXml.MANDATORY;

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private String typeRefName = "";

    private String typeRefNs = Reference.DEFAULT_NAMESPACE;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @Override
    public String getTemplateNamespace() {
        return null;
    }

    @Override
    public String getApplicationNamespace() {
        if (localNamespace != null) {
            return localNamespace;
        }
        return parentNsProvider.getApplicationNamespace();
    }

    @XmlAttribute(name = "namespace")
    public void setNamespace(String namespace) {
        this.localNamespace = namespace;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.fieldId.setName(name);
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.fieldId.setAuxiliaryId(id);
    }

    @XmlAttribute(name = "presence")
    public void setPresence(PresenceXml presence) {
        this.presence = presence;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionaryName(String dictionary) {
        this.dictionary = Dictionary.getDictionary(dictionary);
    }

    @Override
    public ReferenceImpl getTypeRef() {
        return new ReferenceImpl(typeRefName, typeRefNs);
    }

    @XmlAttribute(name = "typeRefName")
    public void setTypeRefName(String typeRefName) {
        this.typeRefName = typeRefName;
    }

    @XmlAttribute(name = "typeRefNs")
    public void setTypeRefNs(String typeRefNs) {
        this.typeRefNs = typeRefNs;
    }

    @Override
    public boolean isOptional() {
        return presence.equals(PresenceXml.OPTIONAL);
    }

    @Override
    public Instruction toInstruction() {
        return this;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof NamespaceProvider) {
            parentNsProvider = (NamespaceProvider) parent;
        }
    }
}

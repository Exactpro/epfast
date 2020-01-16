package com.exactpro.epfast.template;

import com.exactpro.epfast.template.additionalclasses.TypeRef;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Template extends Instruction {

    private String dictionary;

    private TypeRef typeRef;

    public String getDictionary() {
        return dictionary;
    }

    @XmlAttribute(name = "dictionary")
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public TypeRef getTypeRef() {
        return typeRef;
    }

    @XmlElement(name = "typeRef", namespace = "http://www.fixprotocol.org/ns/fast/td/1.1")
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }
}


package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Dictionary;
import com.exactpro.epfast.template.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Template implements com.exactpro.epfast.template.Template {

    private Identity templateId = new Identity();

    private Reference typeRef = new Reference();

    private Dictionary dictionary = Dictionary.getDictionary("global");

    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @Override
    public Reference getTypeRef() {
        return typeRef;
    }

    @Override
    public Dictionary getDictionary() {
        return dictionary;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setTemplateId(Identity templateId) {
        this.templateId = templateId;
    }

    public void setTypeRef(Reference typeRef) {
        this.typeRef = typeRef;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}

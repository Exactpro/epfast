package com.exactpro.epfast.template.xml.instructionfields;

import com.exactpro.epfast.template.*;
import com.exactpro.epfast.template.xml.IdentityXml;
import com.exactpro.epfast.template.xml.helper.InstructionXml;
import com.exactpro.epfast.template.xml.helper.Charset;
import com.exactpro.epfast.template.xml.FieldInstrContent;

import javax.xml.bind.annotation.XmlAttribute;

public class StringFieldXml extends FieldInstrContent implements InstructionXml {

    private Charset charset = Charset.ASCII;

    private IdentityXml lengthFieldId = new IdentityXml();

    public Charset getCharset() {
        return charset;
    }

    @XmlAttribute(name = "charset")
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public IdentityXml getLengthFieldId() {
        return lengthFieldId;
    }

    @XmlAttribute(name = "lengthName")
    public void setName(String name) {
        this.lengthFieldId.setName(name);
    }

    @XmlAttribute(name = "lengthNamespace")
    public void setNamespace(String templateNs) {
        this.lengthFieldId.setNamespace(templateNs);
    }

    @XmlAttribute(name = "lengthId")
    public void setId(String id) {
        this.lengthFieldId.setAuxiliaryId(id);
    }

    private class AsciiString implements AsciiStringField {
        @Override
        public FieldOperator getOperator() {
            return StringFieldXml.this.getOperator();
        }

        @Override
        public Identity getFieldId() {
            return StringFieldXml.this.getFieldId();
        }

        @Override
        public boolean isOptional() {
            return StringFieldXml.this.isOptional();
        }
    }

    private class UnicodeString implements UnicodeStringField {
        @Override
        public FieldOperator getOperator() {
            return StringFieldXml.this.getOperator();
        }

        @Override
        public Identity getLengthFieldId() {
            return StringFieldXml.this.getLengthFieldId();
        }

        @Override
        public Identity getFieldId() {
            return StringFieldXml.this.getFieldId();
        }

        @Override
        public boolean isOptional() {
            return StringFieldXml.this.isOptional();
        }
    }

    @Override
    public Instruction toInstruction() {
        if (charset == Charset.ASCII) {
            return new AsciiString();
        }
        return new UnicodeString();
    }
}

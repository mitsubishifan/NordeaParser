package com.nordea.parser.expression;

import org.xml.sax.Attributes;

public class ExpressionTree {
    private String name;
    private String data;

    private Attributes attr;

    public ExpressionTree(String name, Attributes attr) {
        this.name = name;
        this.attr = attr;
    }

    public ExpressionTree(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void addData(String d) {
        if (data == null) setData(d);
        else data += d;
    }

    private String getAttributesAsString() {
        StringBuffer buf = new StringBuffer();
        if (attr == null) return "";
        for (int i = 0; i < attr.getLength(); i++) {
            buf.append(attr.getQName(i));
            buf.append("=\"").append(attr.getValue(i));
            buf.append("\"");
        }
        return buf.toString();
    }

    @Override
    public String toString() {
        String a = getAttributesAsString();
        return name + ": " + a + (data == null ? "" : " (" + data + ")");
    }
}

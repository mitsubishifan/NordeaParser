package com.nordea.parser.expression;

/**
 * class stores the expression tree name and values
 */

public class ExpressionTree {
    private String name;
    private String data;

    public ExpressionTree(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return name + ": " + (data == null ? "" : " (" + data + ")");
    }
}

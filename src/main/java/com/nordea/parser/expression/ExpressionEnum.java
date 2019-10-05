package com.nordea.parser.expression;

public enum ExpressionEnum {
    ADD("addition"),
    SUB("subtraction"),
    MUL("multiplication"),
    DIV("division");

    public final String label;
    private ExpressionEnum(String label) {
        this.label = label;
    }
}

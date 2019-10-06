package com.nordea.parser.expression;

/**
 * main expression names - edit this enum if you plan to extend parser functionality
 * do not forget to add new operation class in operation package and add methods implementing @IOperation interface
 * last file to be updated is OperationFactory class - add new builder there
 */
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

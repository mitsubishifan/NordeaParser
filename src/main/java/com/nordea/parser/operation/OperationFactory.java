package com.nordea.parser.operation;

import com.nordea.parser.expression.ExpressionEnum;

public class OperationFactory {
    public static IOperation getOperation(String parentOpertor) {
        if (ExpressionEnum.ADD.label.equals(parentOpertor)) {
            return new AddOperation();
        } else if (ExpressionEnum.SUB.label.equals(parentOpertor)) {
            return new SubtractOperation();
        } else if (ExpressionEnum.MUL.label.equals(parentOpertor)) {
            return new MultiplyOperation();
        } else if (ExpressionEnum.DIV.label.equals(parentOpertor)) {
            return new DivideOperation();
        }
        throw new RuntimeException("Operation "+parentOpertor+" not supported yet!");
    }
}

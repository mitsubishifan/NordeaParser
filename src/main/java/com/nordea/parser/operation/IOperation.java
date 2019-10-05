package com.nordea.parser.operation;

import com.nordea.parser.expression.ExpressionTree;

import java.math.BigDecimal;
import java.util.List;

public interface IOperation {
    boolean isOperationCorrect(String nodeName);

    BigDecimal resolveNode(List<ExpressionTree> expressions);
}

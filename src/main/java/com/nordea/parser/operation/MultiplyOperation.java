package com.nordea.parser.operation;

import com.nordea.parser.expression.ExpressionTree;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Stream;

public class MultiplyOperation  implements IOperation {
    private enum MultiplyNodeName {
        FACTOR("factor");

        public final String label;
        private MultiplyNodeName(String label) {
            this.label = label;
        }
    }

    @Override
    public boolean isOperationCorrect(String nodeName) {
        return Stream.of(MultiplyNodeName.values()).anyMatch(x->x.label.equals(nodeName));
    }

    @Override
    public BigDecimal resolveNode(List<ExpressionTree> expressions) {
        BigDecimal retValue = BigDecimal.ONE;
        for (ExpressionTree expression : expressions) {
            BigDecimal toMultiply = new BigDecimal(expression.getData());
            retValue = retValue.multiply(toMultiply, MathContext.DECIMAL128);
        }
        return retValue;
    }
}

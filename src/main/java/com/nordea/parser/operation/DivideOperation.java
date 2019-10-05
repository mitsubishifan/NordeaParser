package com.nordea.parser.operation;

import com.nordea.parser.expression.ExpressionTree;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Stream;

public class DivideOperation  implements IOperation {

    private enum DivideNodeName {
        DIVIDEND("dividend"),
        DIVISOR("divisor");

        public final String label;
        private DivideNodeName(String label) {
            this.label = label;
        }
    }


    @Override
    public boolean isOperationCorrect(String nodeName) {
        return Stream.of(DivideNodeName.values()).anyMatch(x->x.label.equals(nodeName));
    }

    @Override
    public BigDecimal resolveNode(List<ExpressionTree> expressions) {
        BigDecimal dividend = BigDecimal.ZERO;
        BigDecimal divisor = BigDecimal.ZERO;
        for (ExpressionTree expression : expressions) {
            if (DivideNodeName.DIVIDEND.label.equals(expression.getName())) dividend = new BigDecimal(expression.getData());
            else if (DivideNodeName.DIVISOR.label.equals(expression.getName())) divisor = new BigDecimal(expression.getData());
        }
        if (!BigDecimal.ZERO.equals(dividend) && !BigDecimal.ZERO.equals(divisor)) return dividend.divide(divisor, MathContext.DECIMAL128);
        return BigDecimal.ZERO;
    }
}

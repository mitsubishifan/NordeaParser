package com.nordea.parser.operation;

import com.nordea.parser.expression.ExpressionTree;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Stream;

public class SubtractOperation implements IOperation {
    private enum SubtractNodeName {
        MINUED("minued"),
        SuBTRAHEND("subtrahend");

        public final String label;
        private SubtractNodeName(String label) {
            this.label = label;
        }
    }

    @Override
    public boolean isOperationCorrect(String nodeName) {
        return Stream.of(SubtractNodeName.values()).anyMatch(x->x.label.equals(nodeName));
    }

    @Override
    public BigDecimal resolveNode(List<ExpressionTree> expressions) {
        BigDecimal minued = BigDecimal.ZERO;
        BigDecimal substrahend = BigDecimal.ZERO;
        for (ExpressionTree expression : expressions) {
            if (SubtractNodeName.MINUED.label.equals(expression.getName())) minued = new BigDecimal(expression.getData());
            else if (SubtractNodeName.SuBTRAHEND.label.equals(expression.getName())) substrahend = new BigDecimal(expression.getData());
        }
        return minued.subtract(substrahend, MathContext.DECIMAL128);
    }
}

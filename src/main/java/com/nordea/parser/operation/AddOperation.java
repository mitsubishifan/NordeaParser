package com.nordea.parser.operation;

import com.nordea.parser.expression.ExpressionTree;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class AddOperation  implements IOperation {
    private enum AddNodeName {
        ITEM("item");

        public final String label;
        private AddNodeName(String label) {
            this.label = label;
        }
    }

    @Override
    public BigDecimal resolveNode(List<ExpressionTree> expressions) {
        return expressions.stream().map(e->e.getData()).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean isOperationCorrect(String nodeName) {
        return Stream.of(AddNodeName.values()).anyMatch(x->x.label.equals(nodeName));
    }
}

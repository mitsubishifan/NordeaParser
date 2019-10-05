package com.nordea.parser.helper;

import com.nordea.parser.expression.ExpressionEnum;

import java.util.stream.Stream;

/**
 * class created to store any tool methods to help Parser
 */
public class HelperTools {

    /**
     *
     * @param dir - output directory path
     * @param name - input file name
     * @return output file name with full path
     */
    public static final String getOutputFileName(final String dir, final String name) {
        String outputName = name.substring(0, name.lastIndexOf(".")) + "_result.xml";
        return dir + ((dir.endsWith("\\") || dir.endsWith("/")) ? "" : "\\" ) + outputName;
    }

    /**
     * check if expression name is in ExpressionEnum
     * @param expr - one of operation expression name
     * @return true if present
     */
    public static final boolean isExpressionInEnum(final String expr) {
        return Stream.of(ExpressionEnum.values()).anyMatch(x->x.label.startsWith(expr));
    }

    /**
     *
     * @param expr - node expression name
     * @return expression label from enumerated values
     */
    public static final String getCurrentExpressionName(final String expr) {
        for (ExpressionEnum name : ExpressionEnum.values()) {
            if (expr.equalsIgnoreCase(name.label)) return name.label;
        }
        return null;
    }

    /**
     *
     * @param name - current operation name
     * @return corresponded EExpresion value
     */
    public static final ExpressionEnum getExpressionByName(final String name) {
        for (ExpressionEnum ename : ExpressionEnum.values()){
            if (ename.label.equals(name)) return ename;
        }
        return null;
    }
}

package com.nordea.parser.helper;

import com.nordea.parser.expression.ExpressionEnum;

import java.util.stream.Stream;

/**
 * class created to store any static methods to help NordeaParser
 */
public class HelperTools {

    /**
     * creates output file name basing on output dir and input file name
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
     * gets top expression name from current expression xml
     * @param expr - node expression name
     * @return expression label from enumerated values
     */
    public static final String getCurrentExpressionName(final String expr) {
        for (ExpressionEnum name : ExpressionEnum.values()) {
            if (expr.equalsIgnoreCase(name.label)) return name.label;
        }
        return null;
    }
}

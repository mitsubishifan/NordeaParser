package com.nordea.parser.filetype;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Input file parser interface to perform parsing different kind of input file structure
 */

public interface IFileParser {
    void iterateOverExpressions(InputStream input, OutputStream out) throws Exception;

    default StringBuilder getOutputExpressionValue(final String id, final String value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t<result id=\"").append(id);
        stringBuilder.append("\">").append(value).append("</result>\r\n");
        return stringBuilder;
    }
}

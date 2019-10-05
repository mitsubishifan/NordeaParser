package com.nordea.parser;

import com.nordea.parser.expression.ExpressionParserAndResolver;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

public class MathOperationsTest {
    private ExpressionParserAndResolver worker;

    @Before
    public void testSetup() {
        worker = new ExpressionParserAndResolver();
    }

    @Test
    public void checSimplekDivide() {

    }

    @Test
    public void checkSimpleAddition() {

    }

    @Test
    public void checkSimpleMultiply() {

    }

    @Test
    public void checkSimpleSubtraction() {

    }

    @Test
    public void checkMixedExpressions() {

    }

    @Test (expected = ParserConfigurationException.class)
    public void checkWrongXMLContent() {

    }
}

package com.nordea.parser;

import com.nordea.parser.expression.ExpressionParserAndResolver;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;

public class MathOperationsTest {
    private ExpressionParserAndResolver worker;

    @Before
    public void testSetup() {
        worker = new ExpressionParserAndResolver();
    }

    @Test
    public void checSimplekDivide() throws Exception {
        StringBuilder inputData = new StringBuilder("<division id=\"1\">\n" +
                    "\t\t<dividend>54</dividend>\n" +
                    "\t\t<divisor>9</divisor>\n" +
                "\t</division>");
        BigDecimal expectedValue = new BigDecimal(6);
        BigDecimal resolvedValue = worker.parseAndResplveExpression(inputData, "division");
        assertEquals(expectedValue, resolvedValue);

        // check inverted parameters
        inputData = new StringBuilder("<division id=\"1\">\n" +
                    "\t\t<divisor>9</divisor>\n" +
                    "\t\t<dividend>54</dividend>\n" +
                "\t</division>");
        expectedValue = new BigDecimal(6);
        resolvedValue = worker.parseAndResplveExpression(inputData, "division");
        assertEquals(expectedValue, resolvedValue);

        // divide by 0
        inputData = new StringBuilder("<division id=\"1\">\n" +
                "\t\t<dividend>54</dividend>\n" +
                "\t\t<divisor>0</divisor>\n" +
                "\t</division>");
        expectedValue = BigDecimal.ZERO;
        resolvedValue = worker.parseAndResplveExpression(inputData, "division");
        assertEquals(expectedValue, resolvedValue);
    }

    @Test
    public void checkSimpleAddition() throws Exception {
        StringBuilder inputData = new StringBuilder("<addition id=\"7\">\n" +
                    "\t\t<item>5</item>\n" +
                    "\t\t<item>6</item>\n" +
                    "\t\t<item>8</item>\n" +
                "\t</addition>");
        BigDecimal expectedValue = new BigDecimal(19);
        BigDecimal resolvedValue = worker.parseAndResplveExpression(inputData, "addition");
        assertEquals(expectedValue, resolvedValue);
    }

    @Test
    public void checkSimpleMultiply() throws Exception {
        StringBuilder inputData = new StringBuilder("<multiplication id=\"1\">\n" +
                    "\t\t<factor>5</factor>\n" +
                    "\t\t<factor>6</factor>\n" +
                    "\t\t<factor>8</factor>\n" +
                "\t</multiplication>");
        BigDecimal expectedValue = new BigDecimal(240);
        BigDecimal resolvedValue = worker.parseAndResplveExpression(inputData, "multiplication");
        assertEquals(expectedValue, resolvedValue);
    }

    @Test
    public void checkSimpleSubtraction() throws Exception {
        StringBuilder inputData = new StringBuilder("<subtraction id=\"1\">\n" +
                    "\t\t<minued>1234</minued>\n" +
                    "\t\t<subtrahend>234</subtrahend>\n" +
                "\t</subtraction>");
        BigDecimal expectedValue = new BigDecimal(1000);
        BigDecimal resolvedValue = worker.parseAndResplveExpression(inputData, "subtraction");
        assertEquals(expectedValue, resolvedValue);

        // check inverted parameters
        inputData = new StringBuilder("<subtraction id=\"1\">\n" +
                    "\t\t<subtrahend>234</subtrahend>\n" +
                    "\t\t<minued>1234</minued>\n" +
                "\t</subtraction>");
        expectedValue = new BigDecimal(1000);
        resolvedValue = worker.parseAndResplveExpression(inputData, "subtraction");
        assertEquals(expectedValue, resolvedValue);
    }

    @Test
    public void checkMixedExpressions() throws Exception {
        StringBuilder inputData = new StringBuilder("<division id=\"1\" complex=\"true\">\n" +
                "\t\t<dividend>60</dividend>\n" +
                "\t\t<divisor>\n" +
                "\t\t\t<addition>\n" +
                "\t\t\t\t<item>3</item>\n" +
                "\t\t\t\t<item>\n" +
                "\t\t\t\t\t<multiplication>\n" +
                "\t\t\t\t\t\t<factor>\n" +
                "\t\t\t\t\t\t\t<subtraction>\n" +
                "\t\t\t\t\t\t\t\t<minued>10</minued>\n" +
                "\t\t\t\t\t\t\t\t<subtrahend>\n" +
                "\t\t\t\t\t\t\t\t\t<addition>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>1</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>1</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>2</item>\n" +
                "\t\t\t\t\t\t\t\t\t</addition>\n" +
                "\t\t\t\t\t\t\t\t</subtrahend>\n" +
                "\t\t\t\t\t\t\t</subtraction>\n" +
                "\t\t\t\t\t\t</factor>\n" +
                "\t\t\t\t\t\t<factor>1</factor>\n" +
                "\t\t\t\t\t\t<factor>2</factor>\n" +
                "\t\t\t\t\t</multiplication>\n" +
                "\t\t\t\t</item>\n" +
                "\t\t\t</addition>\n" +
                "\t\t</divisor>\n" +
                "\t</division>");
        BigDecimal expectedValue = new BigDecimal(4, MathContext.DECIMAL128);
        BigDecimal resolvedValue = worker.parseAndResplveExpression(inputData, "division");
        assertEquals(expectedValue, resolvedValue);
    }

    @Test (expected = ParserConfigurationException.class)
    public void checkWrongXMLExpression() throws Exception {
        StringBuilder inputData = new StringBuilder("<division id=\"1\" complex=\"true\">\n" +
                "\t\t<dividend>54</dividend>\n" +
                "\t\t<divisor>\n" +
                "\t\t\t<addition>\n" +
                "\t\t\t\t<item>3</item>\n" +
                "\t\t\t\t<item>\n" +
                "\t\t\t\t\t<multiplication>\n" +
                "\t\t\t\t\t\t<factor>\n" +
                "\t\t\t\t\t\t\t<subtraction>\n" +
                "\t\t\t\t\t\t\t\t<minued>3</minued>\n" +
                "\t\t\t\t\t\t\t\t<subtrahend>\n" +
                "\t\t\t\t\t\t\t\t\t<addition>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>5</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>6</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>3</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<factor>3</factor>\n" +  // factor element in addition
                "\t\t\t\t\t\t\t\t\t</addition>\n" +
                "\t\t\t\t\t\t\t\t</subtrahend>\n" +
                "\t\t\t\t\t\t\t</subtraction>\n" +
                "\t\t\t\t\t\t</factor>\n" +
                "\t\t\t\t\t\t<factor>6</factor>\n" +
                "\t\t\t\t\t\t<factor>8</factor>\n" +
                "\t\t\t\t\t</multiplication>\n" +
                "\t\t\t\t</item>\n" +
                "\t\t\t</addition>\n" +
                "\t\t</divisor>\n" +
                "\t</division>");
        worker.parseAndResplveExpression(inputData, "division");
    }

    @Test (expected = SAXParseException.class)
    public void checkWrongXMLContent() throws Exception {
        StringBuilder inputData = new StringBuilder("<division id=\"1\" complex=\"true\">\n" +
                "\t\t<dividend>54</dividend>\n" +
                "\t\t<divisor>\n" +
                "\t\t\t<addition>\n" +
                "\t\t\t\t<item>3</item>\n" +
                "\t\t\t\t<item>\n" +
                "\t\t\t\t\t<multiplication>\n" +
                "\t\t\t\t\t\t<factor>\n" +
                "\t\t\t\t\t\t\t<subtraction>\n" +
                "\t\t\t\t\t\t\t\t<minued>3</minued>\n" +
                "\t\t\t\t\t\t\t\t<subtrahend>\n" +
                "\t\t\t\t\t\t\t\t\t<addition>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>5</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>6</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<item>3</item>\n" +
                "\t\t\t\t\t\t\t\t\t\t<factor>3</item>\n" +  // factor element in addition
                "\t\t\t\t\t\t\t\t\t</addition>\n" +
                "\t\t\t\t\t\t\t\t</subtrahend>\n" +
                "\t\t\t\t\t\t\t</subtraction>\n" +
                "\t\t\t\t\t\t</factor>\n" +
                "\t\t\t\t\t\t<factor>6</factor>\n" +
                "\t\t\t\t\t\t<factor>8</factor>\n" +
                "\t\t\t\t\t</multiplication>\n" +
                "\t\t\t\t</item>\n" +
                "\t\t\t</addition>\n" +
                "\t\t</divisor>\n" +
                "\t</division>");
        worker.parseAndResplveExpression(inputData, "division");
    }
}

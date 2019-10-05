package com.nordea.parser.expression;

import com.nordea.parser.operation.IOperation;
import com.nordea.parser.operation.OperationFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ExpressionParserAndResolver {

    public BigDecimal parseAndResplveExpression(final StringBuilder xmlContent, final String rootNodeName)
            throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        ExpressionTreeHandler handler = new ExpressionTreeHandler();
        SAXParser saxParser = saxFactory.newSAXParser();
        saxParser.parse(new InputSource(new StringReader(xmlContent.toString())), handler);
        DefaultMutableTreeNode parsedNode = handler.getRoot();

        return resolveExpression(parsedNode, rootNodeName);
    }

    private BigDecimal resolveExpression(final DefaultMutableTreeNode expressionTree, final String rootNodeNanme)
            throws ParserConfigurationException {
        IOperation mainOperation = OperationFactory.getOperation(rootNodeNanme);
        Enumeration childrens = expressionTree.children();
        List<ExpressionTree> dataToProcess = new ArrayList<>();
        while (childrens.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) childrens.nextElement();
            ExpressionTree userObject = (ExpressionTree)(childNode.getUserObject());
            if (mainOperation.isOperationCorrect(userObject.getName())) {
                if (childNode.isLeaf()) {
                    dataToProcess.add(userObject);
                } else {
                    BigDecimal internalExpression = resolveExpression(childNode.getNextNode(), ((ExpressionTree) childNode.getNextNode().getUserObject()).getName());
                    ExpressionTree eTree = new ExpressionTree(userObject.getName());
                    eTree.setData(internalExpression.toPlainString());
                    dataToProcess.add(eTree);
                }
            } else throw new ParserConfigurationException("Wrong XML content");
        }

        return mainOperation.resolveNode(dataToProcess);
    }
}

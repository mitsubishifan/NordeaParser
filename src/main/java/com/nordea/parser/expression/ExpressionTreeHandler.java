package com.nordea.parser.expression;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * class created to build up current expression tree
 * basing on SAX parser extends it DefaultHandler class
 */
public class ExpressionTreeHandler extends DefaultHandler {

    private DefaultMutableTreeNode root, currentNode;
    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    @Override
    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        String eName = lName;
        if ("".equals(eName)) eName = qName;
        ExpressionTree t = new ExpressionTree(eName);
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(t);
        if (currentNode == null) root = newNode;
        else currentNode.add(newNode);

        currentNode = newNode;
    }

    @Override
    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        currentNode = (DefaultMutableTreeNode) currentNode.getParent();
    }

    @Override
    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len).trim();
        ((ExpressionTree) currentNode.getUserObject()).addData(s);
    }
}

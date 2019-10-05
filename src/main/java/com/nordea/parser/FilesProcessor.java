package com.nordea.parser;

import com.nordea.parser.expression.ExpressionParserAndResolver;
import com.nordea.parser.helper.HelperTools;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.math.BigDecimal;
import java.util.Iterator;

public class FilesProcessor {

    private final File inputFile;
    private final File outputFile;
    private final XMLInputFactory inputFactory = XMLInputFactory.newInstance();

    public FilesProcessor(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void resolve() {
        try (InputStream inputStream = new FileInputStream(inputFile); OutputStream out = new FileOutputStream(outputFile)) {
            out.write("<expressions>\r\n".getBytes());
            iterateOverExpressions(inputStream, out);
            out.write("</expressions>".getBytes());
        } catch (IOException fnf) {
            System.out.println("IOException occured: " + fnf.getLocalizedMessage());
        } catch (XMLStreamException xse) {
            System.out.println("XML Stram Exception occured: "+ xse.getLocalizedMessage());
        }
    }

    private void iterateOverExpressions(final InputStream input, final OutputStream out) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(input);

        String currentID = null, currentTopExpressionName = null;
        int currentExpressionLevel = 0;
        StringBuilder expressionXML = new StringBuilder();

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                String elementName = startElement.getName().getLocalPart();
                if (HelperTools.isExpressionInEnum(elementName)) {
                    Iterator<Attribute> attributes = startElement.getAttributes();
                    while (attributes.hasNext()) {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("id")) {
                            currentTopExpressionName = HelperTools.getCurrentExpressionName(elementName);
                            expressionXML.setLength(0);
                            currentID = attribute.getValue();
                        }
                    }
                    if (currentTopExpressionName != null && currentTopExpressionName.equalsIgnoreCase(elementName)) currentExpressionLevel++;
                }
            }

            if (!"expressions".equalsIgnoreCase(event.toString())) expressionXML.append(event.toString().replaceAll("[\\t\\n\\r]", ""));

            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals(currentTopExpressionName)) currentExpressionLevel--;
                if (currentExpressionLevel == 0 && currentTopExpressionName != null) {
                    ExpressionParserAndResolver parserAndResolver = new ExpressionParserAndResolver();
                    try {
                        BigDecimal value = parserAndResolver.parseAndResplveExpression(expressionXML, currentTopExpressionName);
                        out.write((getOutputExpressionValue(currentID, value.toPlainString())).toString().getBytes());
                        currentTopExpressionName = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new XMLStreamException("Error occured during processing XML file - wrong XML format: "+ expressionXML);
                    }
                }
            }
        }
    }

    private StringBuilder getOutputExpressionValue(final String id, final String value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t<result id=\"").append(id);
        stringBuilder.append("\">").append(value).append("</result>\r\n");
        return stringBuilder;
    }
}

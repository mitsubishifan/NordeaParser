package com.nordea.parser.filetype;

import com.nordea.parser.expression.ExpressionParserAndResolver;
import com.nordea.parser.helper.HelperTools;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Parse XML input file - produces in memory XML structure of expressions that can be proceed further
 * output StringBuilder structure have to be like:
 * <division id="16" complex="true">
 * 		<dividend>54</dividend>
 * 		<divisor>
 * 			<addition>
 * 				<item>3</item>
 * 				<item>
 * 					<multiplication>
 * 						<factor>
 * 							<subtraction>
 * 								<minued>3</minued>
 * 								<subtrahend>
 * 									<addition>
 * 										<item>5</item>
 * 										<item>6</item>
 * 										<item>3</item>
 * 									</addition>
 * 								</subtrahend>
 * 							</subtraction>
 * 						</factor>
 * 						<factor>6</factor>
 * 						<factor>8</factor>
 * 					</multiplication>
 * 				</item>
 * 			</addition>
 * 		</divisor>
 * 	</division>
 */
public class XMLFileParser implements IFileParser {

    /**
     * method uses SAX parser to build particular expression XML just after root node
     * SAX used to get file partially and process only one main expression at a time
     *
     * @param input input file stream
      * @throws XMLStreamException
     */
    @Override
    public void iterateOverExpressions(final InputStream input, final OutputStream out) throws XMLStreamException {
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
                        out.write((getOutputExpressionValue(currentID, value.toPlainString())).toString().getBytes("UTF-8"));
                        currentTopExpressionName = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new XMLStreamException("Error occured during processing XML file - wrong XML format: "+ expressionXML);
                    }
                }
            }
        }
    }
}

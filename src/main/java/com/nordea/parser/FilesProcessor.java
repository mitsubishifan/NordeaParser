package com.nordea.parser;

import com.nordea.parser.filetype.IFileParser;
import com.nordea.parser.filetype.XMLFileParser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;

public class FilesProcessor {

    private final File inputFile;
    private final File outputFile;
    private final XMLInputFactory inputFactory = XMLInputFactory.newInstance();

    public FilesProcessor(File inputFile, File outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    /**
     * File procesor - to choose different input file resolver please add new file parser in filetype package
     * and use it as fileParser instead of XMLFileParser
     * @return true if file proceed succeeded
     */
    public boolean resolve() {
        IFileParser fileParser = new XMLFileParser();
        try (InputStream inputStream = new FileInputStream(inputFile); OutputStream out = new FileOutputStream(outputFile)) {
            out.write("<expressions>\r\n".getBytes("UTF-8"));
            fileParser.iterateOverExpressions(inputStream, out);
            out.write("</expressions>".getBytes("UTF-8"));
        } catch (IOException fnf) {
            System.out.println("IOException occured: " + fnf.getLocalizedMessage());
            return false;
        } catch (XMLStreamException xse) {
            System.out.println("XML Stram Exception occured: "+ xse.getLocalizedMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Unknown Exception occured: "+ e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}

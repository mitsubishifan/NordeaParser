package com.nordea.parser;

import com.nordea.parser.helper.HelperTools;

import java.io.File;

/**
 * NordeaParser project created to perform technical task
 * takes a input directory and output directory names as a params and process all xml files from input directory
 * performing mathematical operations defined in operation package that implements IOperation interface
 *
 */

public class NordeaParser {
    public static void main(String []args) {
        if (args.length != 2) {
            System.out.println("Wrong number of arguments. Two arguments required, ex.:");
            System.out.println("MainParser C:\\Temp C:\\Temp\\Output");
            return;
        }

        String inputDir = args[0];
        String outputDir = args[1];

        boolean fileSucceed;
        File folder = new File(inputDir);
        File[] listOfFiles = folder.listFiles(); {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")) {
                    String outFileName = HelperTools.getOutputFileName(outputDir, file.getName());
                    File outFile = new File(outFileName);
                    FilesProcessor resolver = new FilesProcessor(file, outFile);
                    fileSucceed = resolver.resolve();
                    System.out.println("Parsing file: \""+file.getName()+"\" ended, status: "+(fileSucceed ? "SUCCESS" : "FAILED"));
                }
            }
        }

        System.out.println("Nordea Parser - processing of input directory: \""+inputDir+"\" finished.");
    }
}

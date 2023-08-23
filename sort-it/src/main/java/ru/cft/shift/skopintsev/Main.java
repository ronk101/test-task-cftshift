package ru.cft.shift.skopintsev;

import ru.cft.shift.skopintsev.utils.CommandLineParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser(args);

        String sortingMode = parser.getSortingMode();
        String dataType = parser.getDataType();
        String outputFile = parser.getOutputFile();
        List<String> inputFiles = parser.getInputFiles();

        System.out.println(sortingMode);
        System.out.println(dataType);
        System.out.println(outputFile);
        for (String inputFile: inputFiles) {
            System.out.println(inputFile);
        }
    }
}
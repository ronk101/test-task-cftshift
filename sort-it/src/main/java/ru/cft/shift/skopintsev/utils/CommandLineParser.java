package ru.cft.shift.skopintsev.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineParser {
    private String sortingMode;
    private String dataType;
    private String outputFile;
    private final List<String> inputFiles;

    public CommandLineParser(String[] args) {
        inputFiles = new ArrayList<>();
        parseArgs(args);
    }
    private void parseArgs(String[] args) {
        if (args.length < 2) {
            printUsageAndExit();
        }

        int argIndex = 0;

        if (args[argIndex].equals("-a") || args[argIndex].equals("-d")) {
            sortingMode = args[argIndex++];
        } else {
            sortingMode = "-a"; // По умолчанию сортировка по возрастанию
        }

        if (!args[argIndex].equals("-i") && !args[argIndex].equals("-s")) {
            printUsageAndExit();
        }
        dataType = args[argIndex++];

        if (argIndex >= args.length) {
            printUsageAndExit();
        }
        outputFile = args[argIndex++];

        if (argIndex >= args.length) {
            printUsageAndExit();
        }

        inputFiles.addAll(Arrays.asList(args).subList(argIndex, args.length));
    }

    private void printUsageAndExit() {
        System.out.println("Usage: [sorting mode] <data type> <output file> <input files...>");
        System.out.println("Sorting mode: -a (по возрастанию), -d (по убыванию)");
        System.out.println("Data type: -i (целые числа), -s (строки)");
        System.exit(1);
    }

    public String getSortingMode() {
        return sortingMode;
    }

    public String getDataType() {
        return dataType;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }
}

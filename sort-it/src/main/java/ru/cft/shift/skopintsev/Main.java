package ru.cft.shift.skopintsev;

import ru.cft.shift.skopintsev.sort.FileMerger;
import ru.cft.shift.skopintsev.utils.CommandLineParser;

public class Main {
    public static void main(String[] args) {
        CommandLineParser parser = new CommandLineParser(args);

        FileMerger fileMerger = new FileMerger(parser.getInputFiles(), parser.getOutputFile(),
                parser.getDataType(), parser.getSortingMode());

        fileMerger.mergeSortedFiles();
    }
}
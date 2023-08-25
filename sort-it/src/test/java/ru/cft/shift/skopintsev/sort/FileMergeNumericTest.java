package ru.cft.shift.skopintsev.sort;

import org.junit.After;
import org.junit.Test;
import ru.cft.shift.skopintsev.utils.DataType;
import ru.cft.shift.skopintsev.utils.SortingMode;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileMergeNumericTest {

    @Test
    public void testMergeAscending() throws IOException {
        List<String> sortedFiles = new LinkedList<>(List.of("in1.txt", "in2.txt", "in3.txt"));
        String outputFile = "out.txt";
        DataType dataType = DataType.NUMERIC;
        SortingMode sortingMode = SortingMode.ASCENDING;

        try (PrintWriter writer1 = new PrintWriter("in1.txt");
             PrintWriter writer2 = new PrintWriter("in2.txt");
             PrintWriter writer3 = new PrintWriter("in3.txt")) {
            writer1.println("1");
            writer1.println("4");
            writer1.println("9");

            writer2.println("1");
            writer2.println("8");
            writer2.println("27");

            writer3.println("1");
            writer3.println("2");
            writer3.println("3");
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            List<String> lines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(Arrays.asList("1", "1", "1", "2", "3", "4", "8", "9", "27"), lines);
        }
    }

    @Test
    public void testMergeDescending() throws IOException {
        List<String> sortedFiles = new LinkedList<>(List.of("in1.txt", "in2.txt", "in3.txt"));
        String outputFile = "out.txt";
        DataType dataType = DataType.NUMERIC;
        SortingMode sortingMode = SortingMode.DESCENDING;

        try (PrintWriter writer4 = new PrintWriter("in1.txt");
             PrintWriter writer5 = new PrintWriter("in2.txt");
             PrintWriter writer6 = new PrintWriter("in3.txt")) {
            writer4.println("9");
            writer4.println("4");
            writer4.println("1");

            writer5.println("27");
            writer5.println("8");
            writer5.println("1");

            writer6.println("3");
            writer6.println("2");
            writer6.println("1");
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            List<String> lines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(Arrays.asList("27", "9", "8", "4", "3", "2", "1", "1", "1"), lines);
        }
    }

    @After
    public void cleanup() {
        new File("in1.txt").delete();
        new File("in2.txt").delete();
        new File("in3.txt").delete();
        new File("out.txt").delete();
    }
}

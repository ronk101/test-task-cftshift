package ru.cft.shift.skopintsev.sort;

import org.junit.After;
import org.junit.Test;
import ru.cft.shift.skopintsev.utils.DataType;
import ru.cft.shift.skopintsev.utils.SortingMode;

import java.io.*;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileMergerTest {

    @Test
    public void testMergeSortedFiles() throws IOException {
        List<String> sortedFiles = new LinkedList<>(Arrays.asList("file1.txt", "file2.txt"));
        String outputFile = "output.txt";
        DataType dataType = DataType.NUMERIC;
        SortingMode sortingMode = SortingMode.ASCENDING;

        try (PrintWriter writer1 = new PrintWriter("file1.txt");
             PrintWriter writer2 = new PrintWriter("file2.txt")) {
            writer1.println("1");
            writer1.println("3");
            writer2.println("2");
            writer2.println("4");
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            List<String> lines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(Arrays.asList("1", "2", "3", "4"), lines);
        }
    }

    @Test
    public void testMergeSingleFile() throws IOException {
        List<String> sortedFiles = new LinkedList<>(List.of("file1.txt"));
        String outputFile = "output.txt";
        DataType dataType = DataType.STRING;
        SortingMode sortingMode = SortingMode.DESCENDING;

        try (PrintWriter writer = new PrintWriter("file1.txt")) {
            writer.println("apple");
            writer.println("banana");
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            List<String> lines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(Arrays.asList("apple", "banana"), lines);
        }
    }

    @Test
    public void testMergeEmptyFiles() throws IOException {
        List<String> sortedFiles = new LinkedList<>(Arrays.asList("file1.txt", "file2.txt"));
        String outputFile = "output.txt";
        DataType dataType = DataType.STRING;
        SortingMode sortingMode = SortingMode.ASCENDING;

        try (PrintWriter writer1 = new PrintWriter("file1.txt");
             PrintWriter writer2 = new PrintWriter("file2.txt")) {
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                assertTrue("Output file should be empty", line.isEmpty());
            }
        }
    }

    @After
    public void cleanup() {
        new File("file1.txt").delete();
        new File("file2.txt").delete();
        new File("output.txt").delete();
    }
}

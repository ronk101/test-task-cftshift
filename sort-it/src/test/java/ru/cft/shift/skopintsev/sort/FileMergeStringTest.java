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

public class FileMergeStringTest {

    @Test
    public void testMergeAscending() throws IOException {
        List<String> sortedFiles = new LinkedList<>(List.of("in1.txt", "in2.txt", "in3.txt"));
        String outputFile = "out.txt";
        DataType dataType = DataType.STRING;
        SortingMode sortingMode = SortingMode.ASCENDING;

        try (PrintWriter writer1 = new PrintWriter("in1.txt");
             PrintWriter writer2 = new PrintWriter("in2.txt");
             PrintWriter writer3 = new PrintWriter("in3.txt");
             PrintWriter writer4 = new PrintWriter("in4.txt")) {
            writer1.println("aaa");
            writer1.println("cec");
            writer1.println("dec");

            writer2.println("agfa");
            writer2.println("agronom");
            writer2.println("bfsa");
            writer2.println("freg");

            writer3.println("vasya");
            writer3.println("zed");

            writer4.println("agronoa");
            writer4.println("zad");
            writer4.println("zzzz");
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            List<String> lines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(Arrays.asList("aaa", "agfa", "agronom", "bfsa", "cec", "dec", "freg", "vasya", "zed"), lines);
        }
    }

    @Test
    public void testMergeDescending() throws IOException {
        List<String> sortedFiles = new LinkedList<>(List.of("in1.txt", "in2.txt", "in3.txt"));
        String outputFile = "out.txt";
        DataType dataType = DataType.STRING;
        SortingMode sortingMode = SortingMode.DESCENDING;

        try (PrintWriter writer1 = new PrintWriter("in1.txt");
             PrintWriter writer2 = new PrintWriter("in2.txt");
             PrintWriter writer3 = new PrintWriter("in3.txt")) {

            writer1.println("zed");
            writer2.println("vasya");

            writer2.println("freg");
            writer2.println("dec");
            writer2.println("cec");
            writer2.println("bfsa");

            writer3.println("agronom");
            writer3.println("agfa");
            writer3.println("aaa");
        }

        FileMerger merger = new FileMerger(sortedFiles, outputFile, dataType, sortingMode);
        merger.mergeSortedFiles();

        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            List<String> lines = new LinkedList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(Arrays.asList("zed", "vasya", "freg", "dec", "cec", "bfsa", "agronom", "agfa", "aaa"), lines);
        }
    }

    @After
    public void cleanup() {
        new File("in1.txt").delete();
        new File("in2.txt").delete();
        new File("in3.txt").delete();
        new File("in4.txt").delete();
        new File("out.txt").delete();
    }
}

package ru.cft.shift.skopintsev.sort;

import ru.cft.shift.skopintsev.utils.DataType;

import java.io.*;
import java.util.List;

public class FileMerger {
    private final List<String> sortedFiles;
    private final String outputFile;
    private final DataType dataType;

    public FileMerger(List<String> sortedFiles, String outputFile, DataType dataType) {
        this.sortedFiles = sortedFiles;
        this.outputFile = outputFile;
        this.dataType = dataType;
    }

    public void mergeSortedFiles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            if (sortedFiles.size() == 1) {
                copySingleFile(sortedFiles.get(0), writer);
            } else {
                mergeMultipleFiles(writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copySingleFile(String inputFile, BufferedWriter writer) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void mergeMultipleFiles(BufferedWriter writer) throws IOException {
        while (sortedFiles.size() > 1) {
            String firstFile = sortedFiles.remove(0);
            String secondFile = sortedFiles.remove(0);
            String mergedFileName = mergeTwoFiles(firstFile, secondFile);
            sortedFiles.add(0, mergedFileName);
        }

        copySingleFile(sortedFiles.get(0), writer);
    }

    private String mergeTwoFiles(String inputFile1, String inputFile2) throws IOException {
        String mergedFileName = createTempFileName();

        try (BufferedWriter mergedWriter = new BufferedWriter(new FileWriter(mergedFileName));
             BufferedReader reader1 = new BufferedReader(new FileReader(inputFile1));
             BufferedReader reader2 = new BufferedReader(new FileReader(inputFile2))) {

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null && line2 != null) {
                if ((dataType == DataType.NUMERIC && Integer.parseInt(line1) <= Integer.parseInt(line2)) ||
                        (dataType == DataType.STRING && line1.compareTo(line2) <= 0)) {
                    writeLine(mergedWriter, line1);
                    line1 = reader1.readLine();
                } else {
                    writeLine(mergedWriter, line2);
                    line2 = reader2.readLine();
                }
            }

            while (line1 != null) {
                writeLine(mergedWriter, line1);
                line1 = reader1.readLine();
            }

            while (line2 != null) {
                writeLine(mergedWriter, line2);
                line2 = reader2.readLine();
            }
        }

        return mergedFileName;
    }

    private String createTempFileName() throws IOException {
        File tempFile = File.createTempFile("merged", ".tmp");
        tempFile.deleteOnExit();
        return tempFile.getAbsolutePath();
    }

    private void writeLine(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }
}

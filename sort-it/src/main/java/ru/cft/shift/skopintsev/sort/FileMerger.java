package ru.cft.shift.skopintsev.sort;

import ru.cft.shift.skopintsev.utils.DataType;
import ru.cft.shift.skopintsev.utils.SortingMode;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class FileMerger {
    private final LinkedList<String> sortedFiles;
    private final String outputFile;
    private final DataType dataType;
    private final SortingMode sortingMode;

    public FileMerger(List<String> sortedFiles, String outputFile, DataType dataType, SortingMode sortingMode) {
        this.sortedFiles = (LinkedList<String>) sortedFiles;
        this.outputFile = outputFile;
        this.dataType = dataType;
        this.sortingMode = sortingMode;
    }

    public void mergeSortedFiles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            if (sortedFiles.size() == 1) {
                copySingleFile(sortedFiles.get(0), writer);
            } else {
                mergeMultipleFiles(writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать выходной файл" + e);
        }
    }

    private void copySingleFile(String inputFile, BufferedWriter writer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть файл" + inputFile + e);
        }
    }

    private void mergeMultipleFiles(BufferedWriter writer) {
        while (sortedFiles.size() > 1) {

            String firstFile = sortedFiles.poll();
            if (!fileExists(firstFile)) {
                System.err.println("Файл '" + firstFile + "не существует. Пропуск");
                continue;
            }
            if (!isFileSorted(firstFile)) {
                System.err.println("Файл " + firstFile + " неправильно отсортирован. Пропуск");
                continue;
            }

            String secondFile = sortedFiles.poll();
            if (!fileExists(secondFile)) {
                System.err.println("Файл '" + secondFile + "не существует. Пропуск");
                sortedFiles.addFirst(firstFile);
                continue;
            }
            if (!isFileSorted(secondFile)) {
                System.err.println("Файл " + secondFile + " неправильно отсортирован. Пропуск");
                sortedFiles.addFirst(firstFile);
                continue;
            }

            String mergedFileName = mergeTwoFiles(firstFile, secondFile);
            if (mergedFileName != null) {
                sortedFiles.addFirst(mergedFileName);
            }
        }

        copySingleFile(sortedFiles.getFirst(), writer);
    }

    private boolean fileExists(String fileName) {
        return new File(fileName).exists();
    }

    private String mergeTwoFiles(String inputFile1, String inputFile2) {
        String mergedFileName = createTempFileName();

        try (BufferedWriter mergedWriter = new BufferedWriter(new FileWriter(mergedFileName));
             BufferedReader reader1 = new BufferedReader(new FileReader(inputFile1));
             BufferedReader reader2 = new BufferedReader(new FileReader(inputFile2))) {

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null && line2 != null) {
                try {
                    int comparison = dataType == DataType.NUMERIC ?
                            Integer.compare(Integer.parseInt(line1), Integer.parseInt(line2)) :
                            line1.compareTo(line2);

                    boolean shouldWriteLine1 = (sortingMode == SortingMode.ASCENDING && comparison <= 0) ||
                            (sortingMode == SortingMode.DESCENDING && comparison >= 0);

                    if (shouldWriteLine1) {
                        if (!line1.contains(" ")) {
                            writeLine(mergedWriter, line1);
                        }
                        line1 = reader1.readLine();
                    } else {
                        if (!line2.contains(" ")) {
                            writeLine(mergedWriter, line2);
                        }
                        line2 = reader2.readLine();
                    }


                } catch (NumberFormatException e) {
                    // Пропускаем некорректные данные, переходим к следующим строкам
                    if (dataType == DataType.NUMERIC) {
                        line1 = reader1.readLine();
                        line2 = reader2.readLine();
                    } else if (dataType == DataType.STRING) {
                        if (line1.compareTo(line2) <= 0) {
                            writeLine(mergedWriter, line1);
                            line1 = reader1.readLine();
                        } else {
                            writeLine(mergedWriter, line2);
                            line2 = reader2.readLine();
                        }
                    }
                }
            }

            // Пропускаем оставшиеся строки из файлов
            while (line1 != null) {
                writeLine(mergedWriter, line1);
                line1 = reader1.readLine();
            }

            while (line2 != null) {
                writeLine(mergedWriter, line2);
                line2 = reader2.readLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
            return null; // Возвращаем null, чтобы обозначить ошибку
        }

        return mergedFileName;
    }

    private String createTempFileName() {
        File tempFile;
        try {
            tempFile = File.createTempFile("merged", ".tmp");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл");
        }
        return tempFile.getAbsolutePath();
    }

    private void writeLine(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    private boolean isFileSorted(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String prevLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                // Пропуск строк с пробелами
                if (line.contains(" ")) {
                    continue;
                }

                if (prevLine != null) {
                    int comparison;
                    if (dataType == DataType.NUMERIC) {
                        comparison = Integer.compare(Integer.parseInt(line), Integer.parseInt(prevLine));
                    } else {
                        comparison = line.compareTo(prevLine);
                    }

                    boolean isCorrectOrder = (sortingMode == SortingMode.ASCENDING && comparison >= 0) ||
                            (sortingMode == SortingMode.DESCENDING && comparison <= 0);

                    if (!isCorrectOrder) {
                        return false; // Файл не отсортирован
                    }
                }
                prevLine = line;
            }
            return true; // Файл отсортирован
        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка при проверке файла: " + e.getMessage());
            return false;
        }
    }
}

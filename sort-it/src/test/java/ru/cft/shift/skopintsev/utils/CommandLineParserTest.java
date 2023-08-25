package ru.cft.shift.skopintsev.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommandLineParserTest {

    @Test
    public void testParsingWithSortingMode() {
        String[] args = {"-d", "-i", "output.txt", "input1.txt", "input2.txt"};
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals(SortingMode.DESCENDING, parser.getSortingMode());
        assertEquals(DataType.NUMERIC, parser.getDataType());
    }

    @Test
    public void testParsingWithoutSortingMode() {
        String[] args = {"-i", "output.txt", "input1.txt", "input2.txt"};
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals(SortingMode.ASCENDING, parser.getSortingMode());
        assertEquals(DataType.NUMERIC, parser.getDataType());
    }

    @Test
    public void testParsingWithString() {
        String[] args = {"-s", "output.txt", "input1.txt", "input2.txt"};
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals(DataType.STRING, parser.getDataType());
    }

    @Test
    public void testParsingWith1InputFile() {
        String[] args = {"-s", "output.txt", "input1.txt"};
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals("output.txt", parser.getOutputFile());
        assertEquals(1, parser.getInputFiles().size());
    }

    @Test
    public void testParsingWithALotOfInputFile() {
        String[] args = {"-s", "output.txt", "input1.txt", "input2.txt", "input3.txt", "input4.txt"};
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals("output.txt", parser.getOutputFile());
        assertEquals(4, parser.getInputFiles().size());
    }

    //TODO сделать тесты для ошибок
}

package ru.cft.shift.skopintsev;

import java.util.NoSuchElementException;

public class FileEntry<T> implements Comparable<FileEntry<T>> {
    private final T data;
    final String type;

    public FileEntry(T data, String type) {
        this.data = data;
        this.type = type;
    }

    // По-хорошему тут нужно использовать дженерики и не отталкиваться от типа данных,
    // но задание подразумевает только два типа данных
    @Override
    public int compareTo(FileEntry other) {
        switch (type) {
            case ("string"):
                return 1;
            case ("integer"):
                return 0;
            default:
                //throw new NoSuchElementException();
                return -1;
        }
    }
}

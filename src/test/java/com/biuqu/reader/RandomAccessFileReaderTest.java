package com.biuqu.reader;

import com.biuqu.utils.JsonUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomAccessFileReaderTest
{
    @Test
    public void readLines() throws IOException
    {
        String basePath = getClass().getResource("/").getPath();
        String file = basePath + "file/random_access.csv";
        System.out.println("file path=" + file);
        RandomAccessFileReader reader = new RandomAccessFileReader(file);
        List<String> records = reader.readLines(2, 5);
        System.out.println(JsonUtil.toJson(records));

        List<String> records2 = reader.readLines(0, 5);
        System.out.println(JsonUtil.toJson(records2));

        List<String> records3 = reader.readLines(10, 5);
        System.out.println(JsonUtil.toJson(records3));
    }

    @Test
    public void readLines2() throws IOException
    {
        String basePath = getClass().getResource("/").getPath();
        String file = basePath + "file/random_access.csv";
        System.out.println("file path=" + file);

        Stream<String> lines = Files.lines(Paths.get(file));
        List<String> selectedLines = lines.skip(2).limit(5).collect(Collectors.toList());
        System.out.println(selectedLines);
    }
}
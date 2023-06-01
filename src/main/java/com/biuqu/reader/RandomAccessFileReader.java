package com.biuqu.reader;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 随机文件读取器
 *
 * @author BiuQu
 * @date 2023/4/28 15:40
 */
@Slf4j
public class RandomAccessFileReader
{
    public RandomAccessFileReader(String path)
    {
        this.path = path;
        log.info("read file[{}] now.", path);
    }

    /**
     * 读取指定行数据
     * <p>
     * 从第${start}行开始，读取${num}行
     *
     * @param start 起始位置
     * @param num   行数(包含)
     * @return 指定行的数据集合
     * @throws IOException 磁盘IO异常
     */
    public List<String> readLines(int start, int num) throws IOException
    {
        Stream<String> streamLines = Files.lines(Paths.get(path));
        List<String> lines = streamLines.skip(start).limit(num).collect(Collectors.toList());
        return lines;
    }

    /**
     * 文件路径
     */
    private final String path;
}

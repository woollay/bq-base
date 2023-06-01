package com.biuqu.model;

import com.biuqu.utils.IoUtil;
import com.biuqu.utils.JsonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileTypeTest
{

    @Test
    public void getMaxBytes()
    {
    }

    @Test
    public void getType2() throws IOException
    {
        byte[] jpeg1 = IOUtils.toByteArray(IoUtil.readInputStream("file/direnjie.jpeg"));
        FileType type0 = FileType.getType(jpeg1);
        System.out.println("jpeg0=" + type0 + ",value=" + Hex.toHexString(Arrays.copyOf(jpeg1, 100)));
        Assert.assertTrue(type0 == FileType.JPEG);
    }

    @Test
    public void values()
    {
        List<FileType> types = Lists.newArrayList();
        types.addAll(Lists.newArrayList(FileType.values()));
        Collections.sort(types, new Comparator<FileType>()
        {
            @Override
            public int compare(FileType o1, FileType o2)
            {
                return o2.getType().length() - o1.getType().length();
            }
        });

        System.out.println("list=" + JsonUtil.toJson(types));
        Assert.assertTrue(types.get(types.size() - 1).name().equals("BMP"));
    }
}
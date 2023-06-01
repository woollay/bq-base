package com.biuqu.model;

import com.google.common.collect.Lists;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件流判断文件类型枚举
 * 参考：https://www.cnblogs.com/rainsilence/archive/2012/06/06/2538294.html
 * <p>
 * 注意：以下类型仅限于有标准协议的，像properties等文件是没有固定二进制前缀的
 *
 * @author BiuQu
 * @date 2023/1/6 09:24
 */
public enum FileType
{
    /**
     * JEPG.
     */
    JPEG("FFD8FF"),

    /**
     * PNG.
     */
    PNG("89504E47"),

    /**
     * GIF.
     */
    GIF("47494638"),

    /**
     * TIFF.
     */
    TIFF("49492A00"),

    /**
     * Windows Bitmap.
     */
    BMP("424D"),

    /**
     * CAD.
     */
    DWG("41433130"),

    /**
     * Adobe Photoshop.
     */
    PSD("38425053"),

    /**
     * Rich Text Format.
     */
    RTF("7B5C727466"),

    /**
     * XML.
     */
    XML("3C3F786D6C"),

    /**
     * HTML.
     */
    HTML("68746D6C3E"),

    /**
     * Email [thorough only].
     */
    EML("44656C69766572792D646174653A"),

    /**
     * Outlook Express.
     */
    DBX("CFAD12FEC5FD746F"),

    /**
     * Outlook (pst).
     */
    PST("2142444E"),

    /**
     * MS Word/Excel(兼容格式).
     */
    XLS_DOC("D0CF11E0"),

    /**
     * XLSX(excel新版本)
     */
    XLSX("504B030414000600080000002100"),

    /**
     * DOCX(word新版本)
     */
    DOCX("504B03041400060008000000210077"),

    /**
     * MS Access.
     */
    MDB("5374616E64617264204A"),

    /**
     * WordPerfect.
     */
    WPD("FF575043"),

    /**
     * Postscript.
     */
    EPS("252150532D41646F6265"),

    /**
     * Adobe Acrobat.
     */
    PDF("255044462D312E"),

    /**
     * Quicken.
     */
    QDF("AC9EBD8F"),

    /**
     * Windows Password.
     */
    PWL("E3828596"),

    /**
     * ZIP Archive.
     */
    ZIP("504B0304"),

    /**
     * RAR Archive.
     */
    RAR("52617221"),

    /**
     * Wave.
     */
    WAV("57415645"),

    /**
     * AVI.
     */
    AVI("41564920"),

    /**
     * Real Audio.
     */
    RAM("2E7261FD"),

    /**
     * Real Media.
     */
    RM("2E524D46"),

    /**
     * MPEG (mpg).
     */
    MPG("000001BA"),

    /**
     * Quicktime.
     */
    MOV("6D6F6F76"),

    /**
     * Windows Media.
     */
    ASF("3026B2758E66CF11"),

    /**
     * MIDI.
     */
    MID("4D546864");

    /**
     * 获取最长的二进制文件格式对应的内容
     *
     * @return 文件格式对应的最长的二进制长度
     */
    public static int getMaxBytes()
    {
        return MAX_LEN.get();
    }

    /**
     * 获取文件类型
     *
     * @param data 文件二进制
     * @return 文件类型对象
     */
    public static FileType getType(byte[] data)
    {
        if (null == data)
        {
            LOGGER.warn("unknown file type by stream.");
            return null;
        }

        //1.截取最长的文件格式的二进制位数
        int maxLen = Math.min(data.length, MAX_LEN.get());
        byte[] suffixBytes = Arrays.copyOf(data, maxLen);
        //2.把最长的长度的二进制转成十六进制
        String suffixTypes = Hex.toHexString(suffixBytes);
        for (FileType fileType : SORTED_TYPES)
        {
            if (suffixTypes.toUpperCase(Locale.US).contains(fileType.type.toUpperCase(Locale.US)))
            {
                LOGGER.info("current file type by stream:{}.", fileType.name());
                return fileType;
            }
        }
        LOGGER.warn("unknown file type by stream.");
        return null;
    }

    /**
     * 获取到十六进制的类型
     *
     * @return 十六进制的类型
     */
    public String getType()
    {
        return this.type;
    }

    @Override
    public String toString()
    {
        return this.name().toLowerCase(Locale.US);
    }

    /**
     * 构造方法
     *
     * @param type 文件类型
     */
    FileType(String type)
    {
        this.type = type;
    }

    /**
     * 最大长度的文件二进制数位
     */
    private static final AtomicInteger MAX_LEN = new AtomicInteger();

    /**
     * 排序后的文件类型集合
     */
    private static final List<FileType> SORTED_TYPES = Lists.newArrayList();

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileType.class);

    /**
     * 文件类型
     */
    private String type;

    //初始化
    static
    {
        //1.获取最长的文件格式的二进制长度
        int maxHexLen = 0;
        int maxBytesLen = 0;
        for (FileType fileType : values())
        {
            int len = fileType.type.length();
            if (len > maxHexLen)
            {
                maxBytesLen = Hex.decode(fileType.type.getBytes(StandardCharsets.UTF_8)).length;
            }
            maxHexLen = Math.max(maxHexLen, len);
        }

        //2.把遍历的最长的二进制前缀长度保存下来
        MAX_LEN.set(maxBytesLen);

        //3.把所有格式枚举保存起来
        SORTED_TYPES.addAll(Lists.newArrayList(values()));

        //4.把所有的格式枚举重新排序(从长到短,为了避免文件格式存在包含而取错的情况，比如：docx格式应该包含了doc)
        Collections.sort(SORTED_TYPES, new Comparator<FileType>()
        {
            @Override
            public int compare(FileType o1, FileType o2)
            {
                return o2.type.length() - o1.type.length();
            }
        });
    }
}


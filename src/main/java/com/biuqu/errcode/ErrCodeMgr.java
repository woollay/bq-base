package com.biuqu.errcode;

import com.biuqu.constants.Const;
import com.biuqu.i18n.ErrCodeI18nMgr;
import com.biuqu.model.ErrCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * 错误码管理器
 *
 * @author BiuQu
 * @date 2023/1/7 21:18
 */
public final class ErrCodeMgr
{
    /**
     * 获取标准的全局的内部错误码对象
     *
     * @return 标准的内部错误的错误码对象
     */
    public static ErrCode getServerErr()
    {
        String code = ErrCodeEnum.SERVER_ERROR.getCode();
        String msgKey = code + CODE_SUFFIX;
        String msg = I18N.get(Locale.SIMPLIFIED_CHINESE, msgKey);
        if (StringUtils.isEmpty(msg))
        {
            msg = StringUtils.EMPTY;
        }
        return ErrCode.build(code, msg);
    }

    /**
     * 获取外部错误码对象
     *
     * @param outCode 外部错误码code
     * @return 外部错误码对象
     */
    public static ErrCode getOut(String outCode)
    {
        return getOut(outCode, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 获取外部错误码对象
     *
     * @param outCode 外部错误码code
     * @param locale  语言
     * @return 外部错误码对象
     */
    public static ErrCode getOut(String outCode, Locale locale)
    {
        if (StringUtils.isEmpty(outCode))
        {
            LOGGER.error("invalid out code[{}/{}].", outCode, locale);
            return getToOut(ErrCodeEnum.SERVER_ERROR.getCode());
        }
        String outMsg = I18N.getOut(locale, outCode);
        if (StringUtils.isEmpty(outMsg))
        {
            LOGGER.error("not exist out code[{}/{}].", outCode, locale);
            return getToOut(ErrCodeEnum.SERVER_ERROR.getCode(), locale);
        }
        return ErrCode.build(outCode, outMsg, OUT_TYPE);
    }

    /**
     * 获取标准的错误码(默认中文)
     *
     * @param code 错误码code
     * @return 错误码对象
     */
    public static ErrCode get(String code)
    {
        return get(code, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 获取参数校验标准的错误码(默认中文)
     *
     * @param code 错误码code
     * @return 错误码对象
     */
    public static ErrCode getValid(String code)
    {
        return getValid(code, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 获取标准的错误码
     *
     * @param code   错误码code
     * @param locale 语言
     * @return 错误码对象
     */
    public static ErrCode get(String code, Locale locale)
    {
        if (StringUtils.isEmpty(code))
        {
            LOGGER.error("no standard code[{}/{}].", code, locale);
            return getServerErr();
        }
        String msgKey = code + CODE_SUFFIX;
        String msg = I18N.get(locale, msgKey);
        if (StringUtils.isEmpty(msg))
        {
            LOGGER.error("not exist standard code[{}/{}].", code, locale);
            return getServerErr();
        }
        return ErrCode.build(code, msg);
    }

    /**
     * 获取标准的错误码(带detail,适用于参数校验场景)
     *
     * @param code   错误码code
     * @param locale 语言
     * @return 错误码对象
     */
    public static ErrCode getValid(String code, Locale locale)
    {
        if (StringUtils.isEmpty(code))
        {
            LOGGER.error("no standard parameter code[{}/{}].", code, locale);
            return getServerErr();
        }
        String[] codes = StringUtils.split(code, Const.LINK);
        String realCode = codes[0];
        ErrCode errCode = get(realCode);
        if (null == errCode)
        {
            LOGGER.error("no standard code[{}/{}] in parameter config.", code, locale);
            return getServerErr();
        }
        //只有当参数校验对应的标准错误码存在时，才添加detail
        if (code.contains(errCode.getCode()))
        {
            errCode.setDetail(I18N.getValid(code));
        }
        return errCode;
    }

    /**
     * 根据内部错误码获取标准错误码(默认获取中文)
     *
     * @param inCode 内部错误码
     * @return 标准错误码对象
     */
    public static ErrCode getFromIn(String inCode)
    {
        return getFromIn(inCode, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 根据内部错误码获取标准错误码
     *
     * @param inCode 内部错误码code
     * @param locale 语言
     * @return 标准错误码对象
     */
    public static ErrCode getFromIn(String inCode, Locale locale)
    {
        String key = inCode + OUT_SUFFIX;
        String code = I18N.getIn(locale, key);
        if (StringUtils.isEmpty(code))
        {
            LOGGER.error("no in code[{}/{}] to standard.", inCode, locale);
            return getServerErr();
        }

        return get(code, locale);
    }

    /**
     * 根据标准错误码获取外部错误码(默认获取中文)
     *
     * @param code 标准错误码
     * @return 外部错误码对象
     */
    public static ErrCode getToOut(String code)
    {
        return getToOut(code, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 根据标准错误码获取外部错误码
     *
     * @param code   标准错误码
     * @param locale 语言
     * @return 外部错误码对象
     */
    public static ErrCode getToOut(String code, Locale locale)
    {
        String outKey = code + OUT_SUFFIX;
        String outCode = I18N.get(locale, outKey);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(outCode))
        {
            LOGGER.error("no code[{}/{}] to out.", code, locale);
            return getToOut(ErrCodeEnum.SERVER_ERROR.getCode());
        }

        return getOut(outCode, locale);
    }

    /**
     * 加载标准的错误码的国际化文件中的内容(默认添加中文和英文)
     *
     * @param path 文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     */
    public static void load(String path)
    {
        I18N.loadI18n(path, Locale.US);
        I18N.loadI18n(path, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 加载外部错误码的国际化文件中的内容(默认添加中文和英文)
     *
     * @param path 文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     */
    public static void loadOut(String path)
    {
        I18N.loadI18nOut(path, Locale.US);
        I18N.loadI18nOut(path, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 加载内部错误码的国际化文件中的内容(默认添加中文和英文)
     *
     * @param path 文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     */
    public static void loadIn(String path)
    {
        I18N.loadI18nIn(path, Locale.US);
        I18N.loadI18nIn(path, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 加载参数校验错误码的国际化文件中的内容(默认添加中文和英文)
     *
     * @param path 文件路径(注意不能带语言及properties后缀，即不能包含zh_CN.properties)
     */
    public static void loadValid(String path)
    {
        I18N.loadI18nValid(path, Locale.US);
        I18N.loadI18nValid(path, Locale.SIMPLIFIED_CHINESE);
    }

    private ErrCodeMgr()
    {
    }

    /**
     * 日志句柄
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrCodeMgr.class);

    /**
     * 引入错误码的国际化管理器
     */
    private static final ErrCodeI18nMgr I18N = ErrCodeI18nMgr.getInstance();

    /**
     * 错误码国际化文件中的code key后缀
     */
    private static final String CODE_SUFFIX = ".MSG";

    /**
     * 错误码国际化文件中的code映射到外部的code的key后缀
     */
    private static final String OUT_SUFFIX = ".OUT";

    /**
     * 外部错误码的类型
     */
    private static final int OUT_TYPE = 1;
}

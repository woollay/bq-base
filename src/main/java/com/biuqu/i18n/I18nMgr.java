package com.biuqu.i18n;

import java.util.Locale;

/**
 * 前端显示的国际化数据管理类
 *
 * @author BiuQu
 * @date 2023/1/6 08:49
 */
public final class I18nMgr extends BaseI18nMgr
{
    /**
     * 获取国际化单例
     *
     * @return 单例对象
     */
    public static I18nMgr getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected String getI18nPath()
    {
        return I18N_PATH;
    }

    private I18nMgr()
    {
    }

    /**
     * 单例
     */
    private static final I18nMgr INSTANCE = new I18nMgr();

    /**
     * 默认文件路径
     */
    private static final String I18N_PATH = "i18n/i18n";

    static
    {
        //默认加载2种语言的国际化
        INSTANCE.loadI18n(Locale.US);
        INSTANCE.loadI18n(Locale.SIMPLIFIED_CHINESE);
    }
}

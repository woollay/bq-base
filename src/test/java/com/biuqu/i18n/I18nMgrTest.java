package com.biuqu.i18n;

import com.biuqu.utils.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class I18nMgrTest
{
    @Test
    public void get()
    {
        I18nMgr.getInstance().loadI18n("classpath:i18n/i18n", Locale.US);
        I18nMgr.getInstance().loadI18n("classpath:i18n/i18n", Locale.SIMPLIFIED_CHINESE);
        Assert.assertTrue("狄仁杰".equalsIgnoreCase(I18nMgr.getInstance().getI18n().get("test1.name")));
        Assert.assertTrue("RenJieDi".equalsIgnoreCase(I18nMgr.getInstance().getI18n(Locale.US).get("test1.name")));
        Assert.assertTrue(null != I18nMgr.getInstance().getI18n(Locale.SIMPLIFIED_CHINESE).get("test1.name"));
    }

    @Test
    public void getI18n()
    {
        System.out.println("i18n[zh_CN]=" + JsonUtil.toJson(I18nMgr.getInstance().getI18n()));
        System.out.println("i18n[en_US]=" + JsonUtil.toJson(I18nMgr.getInstance().getI18n(Locale.US)));
        Assert.assertTrue(!I18nMgr.getInstance().getI18n().isEmpty());
        Assert.assertTrue(!I18nMgr.getInstance().getI18n(Locale.US).isEmpty());
    }

    @Test
    public void getI18ns()
    {
        System.out.println("i18n[zh_CN/en_US]=" + JsonUtil.toJson(I18nMgr.getInstance().getI18ns()));
        Assert.assertTrue(!I18nMgr.getInstance().getI18ns().isEmpty());
    }

    @Test
    public void loadBundle()
    {
        Assert.assertTrue(I18nMgr.getInstance().getI18n(Locale.US).containsKey("test1.name"));
        Assert.assertTrue(I18nMgr.getInstance().getI18n(Locale.SIMPLIFIED_CHINESE).containsKey("test1.name"));
        Assert.assertTrue(!I18nMgr.getInstance().getI18n(Locale.US).containsKey("test2.name"));
        Assert.assertTrue(!I18nMgr.getInstance().getI18n(Locale.SIMPLIFIED_CHINESE).containsKey("test2.name"));

        I18nMgr.getInstance().loadI18n("classpath:i18n/errcode_valid", Locale.US);
        I18nMgr.getInstance().loadI18n("classpath:i18n/errcode_valid", Locale.SIMPLIFIED_CHINESE);
        Assert.assertTrue(I18nMgr.getInstance().getI18n(Locale.US).containsKey("test2.name"));
        Assert.assertTrue(I18nMgr.getInstance().getI18n(Locale.SIMPLIFIED_CHINESE).containsKey("test2.name"));
    }

    @Test
    public void clear()
    {
        Assert.assertTrue(!I18nMgr.getInstance().getI18ns().isEmpty());
        I18nMgr.getInstance().clear();
        Assert.assertTrue(I18nMgr.getInstance().getI18ns().isEmpty());
        I18nMgr.getInstance().loadI18n("classpath:i18n/i18n", Locale.US);
        I18nMgr.getInstance().loadI18n("classpath:i18n/i18n", Locale.SIMPLIFIED_CHINESE);
        I18nMgr.getInstance().loadI18n("classpath:i18n/errcode_valid", Locale.US);
        I18nMgr.getInstance().loadI18n("classpath:i18n/errcode_valid", Locale.SIMPLIFIED_CHINESE);
    }
}
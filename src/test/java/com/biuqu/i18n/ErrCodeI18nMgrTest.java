package com.biuqu.i18n;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.Locale;

public class ErrCodeI18nMgrTest
{
    @Test
    public void getOut()
    {
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getOut("0")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getOut(Locale.US, "-1")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getOut(Locale.SIMPLIFIED_CHINESE, "0")));
        Assert.assertTrue(StringUtils.isEmpty(INSTANCE.getOut(Locale.US, "-2")));
    }

    @Test
    public void getIn()
    {
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getIn("101.MSG")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getIn("101.OUT")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getIn(Locale.US, "999.MSG")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getIn(Locale.US, "999.OUT")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getIn(Locale.SIMPLIFIED_CHINESE, "101.MSG")));
        Assert.assertTrue(!StringUtils.isEmpty(INSTANCE.getIn(Locale.SIMPLIFIED_CHINESE, "101.OUT")));
        Assert.assertTrue(StringUtils.isEmpty(INSTANCE.getIn(Locale.US, "-2.MSG")));
        Assert.assertTrue(StringUtils.isEmpty(INSTANCE.getIn(Locale.US, "-2.OUT")));
    }

    @Test
    public void getI18nOut()
    {
        Assert.assertTrue(!CollectionUtils.isEmpty(INSTANCE.getI18nOut()));
    }

    @Test
    public void getI18nIn()
    {
        Assert.assertTrue(!CollectionUtils.isEmpty(INSTANCE.getI18nIn()));
    }

    @Test
    public void getI18nsOut()
    {
        Assert.assertTrue(!CollectionUtils.isEmpty(INSTANCE.getI18nsOut()));
    }

    @Test
    public void getI18nsIn()
    {
        Assert.assertTrue(!CollectionUtils.isEmpty(INSTANCE.getI18nsIn()));
    }

    private static final ErrCodeI18nMgr INSTANCE = ErrCodeI18nMgr.getInstance();
}
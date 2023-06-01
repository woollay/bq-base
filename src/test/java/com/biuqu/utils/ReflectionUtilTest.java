package com.biuqu.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class ReflectionUtilTest
{

    @Test
    public void updateField()
    {
        Person2 person2 = new Person2();
        String value1 = ReflectionUtil.getField(person2, "email");
        Assert.assertTrue(null == value1);

        ReflectionUtil.updateField(person2, "email", "xxx@xxx.com");
        String value2 = ReflectionUtil.getField(person2, "email");
        Assert.assertTrue("xxx@xxx.com".equals(value2));
    }

    @Test
    public void getAnnValues()
    {
        //获取注解，只对@Retention(RetentionPolicy.SOURCE)类型不生效
        Assert.assertTrue(null == Person.class.getAnnotation(Data.class));
        //获取注解，仅对@Retention(RetentionPolicy.RUNTIME)类型生效
        Assert.assertTrue(null != Person.class.getAnnotation(TestClassAnn.class));

        System.out.println("getDeclaredAnnotations=" + JsonUtil.toJson(Person.class.getDeclaredAnnotations()));
        System.out.println("getAnnotations=" + JsonUtil.toJson(Person.class.getAnnotations()));
        System.out.println("getAnnotation=" + JsonUtil.toJson(Person.class.getAnnotation(TestClassAnn.class)));

        TestClassAnn ann1 = Person.class.getAnnotation(TestClassAnn.class);
        Map<Object, Object> annMap1 = ReflectionUtil.getAnnValues(ann1);
        System.out.println("annMap1=" + JsonUtil.toJson(annMap1));
        Assert.assertTrue(null != annMap1 && annMap1.containsKey("callSuper"));

        Set<Method> methods2 = ReflectionUtil.getMethods(Person.class, TestMethodAnn.class);
        for (Method method : methods2)
        {
            TestMethodAnn ann2 = method.getAnnotation(TestMethodAnn.class);
            Map<?, ?> annMap2 = ReflectionUtil.getAnnValues(ann2);
            System.out.println("annMap2=" + JsonUtil.toJson(annMap2));
            Assert.assertTrue(null != annMap2 && annMap2.containsKey("callSuper"));
        }

        Set<Field> fields3 = ReflectionUtil.getFields(Person.class, TestFieldAnn.class);
        for (Field field : fields3)
        {
            TestFieldAnn ann3 = field.getAnnotation(TestFieldAnn.class);
            Map<?, ?> annMap3 = ReflectionUtil.getAnnValues(ann3);
            System.out.println("annMap3=" + JsonUtil.toJson(annMap3));
            Assert.assertTrue(null != annMap3 && annMap3.containsKey("callSuper"));
        }
    }

    @Test
    public void updateFieldAnn()
    {
        Set<Field> fields = ReflectionUtil.getFields(Person.class, TestFieldAnn.class);
        for (Field field : fields)
        {
            if (field.getName().equals("name"))
            {
                //1.验证原始值获取是否正确
                TestFieldAnn ann1 = field.getAnnotation(TestFieldAnn.class);
                Assert.assertTrue(!(Boolean)ReflectionUtil.getAnnValue(ann1, "callSuper"));
                String[] of = (String[])ReflectionUtil.getAnnValue(ann1, "of");
                Assert.assertTrue(of[0].equals("field111"));
                Assert.assertTrue(ReflectionUtil.getAnnValue(ann1, "value").toString().equals("name-1"));

                //2.验证map更新时，会直接更新注解的属性值
                Map<Object, Object> annMap1 = ReflectionUtil.getAnnValues(ann1);
                try
                {
                    annMap1.put("value", "name-00");
                    Assert.assertTrue(false);
                }
                catch (Exception e)
                {
                    Assert.assertTrue(true);
                }
                Assert.assertTrue(annMap1.get("value").toString().equals("name-1"));
                Assert.assertTrue(ReflectionUtil.getAnnValue(ann1, "value").toString().equals("name-1"));

                //3.验证注解更新是否正确
                String[] newOf = {"ddd"};
                ReflectionUtil.updateAnn(ann1, "callSuper", true);
                ReflectionUtil.updateAnn(ann1, "value", "true-xxx");
                ReflectionUtil.updateAnn(ann1, "of", newOf);
                Map<Object, Object> annMap3 = ReflectionUtil.getAnnValues(ann1);
                Assert.assertTrue(annMap3.get("value").toString().equals("true-xxx"));
                Assert.assertTrue(annMap3.get("callSuper").toString().equals("true"));
                Assert.assertTrue(((String[])annMap3.get("of"))[0].equals("ddd"));

                //4.还原原始数据
                ReflectionUtil.updateAnn(ann1, "callSuper", false);
                ReflectionUtil.updateAnn(ann1, "value", "name-1");
                ReflectionUtil.updateAnn(ann1, "of", new String[] {"field111"});
            }
        }
    }

    @Test
    public void updateMethodAnn()
    {
        Set<Method> methods = ReflectionUtil.getMethods(Person.class, TestMethodAnn.class);
        for (Method method : methods)
        {
            if (method.getName().equals("test"))
            {
                //1.验证原始值获取是否正确
                TestMethodAnn ann1 = method.getAnnotation(TestMethodAnn.class);
                Assert.assertTrue(!(Boolean)ReflectionUtil.getAnnValue(ann1, "callSuper"));
                String[] of = (String[])ReflectionUtil.getAnnValue(ann1, "of");
                Assert.assertTrue(of[0].equals("method111"));
                Assert.assertTrue(ReflectionUtil.getAnnValue(ann1, "value").toString().equals("3333"));

                //2.验证map更新时，会直接更新注解的属性值
                Map<Object, Object> annMap1 = ReflectionUtil.getAnnValues(ann1);
                try
                {
                    annMap1.put("value", "name-00");
                    Assert.assertTrue(false);
                }
                catch (Exception e)
                {
                    Assert.assertTrue(true);
                }
                Assert.assertTrue(ReflectionUtil.getAnnValue(ann1, "value").toString().equals("3333"));

                //3.验证注解更新是否正确
                String[] newOf = {"ddd"};
                ReflectionUtil.updateAnn(ann1, "callSuper", true);
                ReflectionUtil.updateAnn(ann1, "value", "true-xxx");
                ReflectionUtil.updateAnn(ann1, "of", newOf);
                Map<Object, Object> annMap3 = ReflectionUtil.getAnnValues(ann1);
                Assert.assertTrue(annMap3.get("value").toString().equals("true-xxx"));
                Assert.assertTrue(annMap3.get("callSuper").toString().equals("true"));
                Assert.assertTrue(((String[])annMap3.get("of"))[0].equals("ddd"));

                //4.还原原始数据
                ReflectionUtil.updateAnn(ann1, "callSuper", false);
                ReflectionUtil.updateAnn(ann1, "value", "3333");
                ReflectionUtil.updateAnn(ann1, "of", new String[] {"method111"});
            }
        }
    }

    @Test
    public void updateClassAnn()
    {
        //1.验证原始值获取是否正确
        TestClassAnn ann1 = Person.class.getAnnotation(TestClassAnn.class);
        Assert.assertTrue(!(Boolean)ReflectionUtil.getAnnValue(ann1, "callSuper"));
        String[] of = (String[])ReflectionUtil.getAnnValue(ann1, "of");
        Assert.assertTrue(of[0].equals("class123"));
        Assert.assertTrue(ReflectionUtil.getAnnValue(ann1, "value").toString().equals("111"));

        //2.验证map更新时，会直接更新注解的属性值
        Map<Object, Object> annMap1 = ReflectionUtil.getAnnValues(ann1);
        try
        {
            annMap1.put("value", "name-00");
            Assert.assertTrue(false);
        }
        catch (Exception e)
        {
            Assert.assertTrue(true);
        }
        Assert.assertTrue(ReflectionUtil.getAnnValue(ann1, "value").toString().equals("111"));

        //3.验证注解更新是否正确
        String[] newOf = {"ddd"};
        ReflectionUtil.updateAnn(ann1, "callSuper", true);
        ReflectionUtil.updateAnn(ann1, "value", "true-xxx");
        ReflectionUtil.updateAnn(ann1, "of", newOf);
        Map<Object, Object> annMap3 = ReflectionUtil.getAnnValues(ann1);
        Assert.assertTrue(annMap3.get("value").toString().equals("true-xxx"));
        Assert.assertTrue(annMap3.get("callSuper").toString().equals("true"));
        Assert.assertTrue(((String[])annMap3.get("of"))[0].equals("ddd"));

        //4.还原原始数据
        ReflectionUtil.updateAnn(ann1, "callSuper", false);
        ReflectionUtil.updateAnn(ann1, "value", "111");
        ReflectionUtil.updateAnn(ann1, "of", new String[] {"class123"});
    }

    @NoArgsConstructor
    @Data
    @TestClassAnn(value = "111", of = {"class123"})
    public class Person
    {
        //姓名
        @TestFieldAnn(value = "name-1", of = {"field111"})
        private String name;

        //base64
        @TestFieldAnn(callSuper = true)
        private String base64;

        //卡id
        @TestFieldAnn(value = "card-3", callSuper = true)
        private String cardId;

        @TestMethodAnn(value = "3333", of = {"method111"})
        public void test()
        {
            System.out.println("test");
        }
    }

    public class Person2
    {
        private String email;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestClassAnn
    {
        String value() default "classValue";

        String[] of() default {};

        boolean callSuper() default false;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestMethodAnn
    {
        String value() default "methodValue";

        String[] of() default {};

        boolean callSuper() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestFieldAnn
    {
        String value() default "fieldValue";

        String[] of() default {};

        boolean callSuper() default false;
    }
}
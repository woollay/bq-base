package com.biuqu.json;

import com.biuqu.annotation.JsonMaskAnn;
import com.biuqu.model.JsonRule;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtilTest
{

    @Test
    public void toObject()
    {
        String json = JsonUtil.toJson(person);
        Person object1 = JsonUtil.toObject(json, Person.class);
        System.out.println("object1==" + JsonUtil.toJson(object1));
        Assert.assertTrue(object1.getCardId() != null);

        String json2 = JsonUtil.toJson(person, true);
        System.out.println("json2==" + json2);
        Assert.assertTrue(json2.contains("card_id"));
        Person object2 = JsonUtil.toObject(json2, Person.class, true);
        System.out.println("object2==" + JsonUtil.toJson(object2));
        Assert.assertTrue(object2.getCardId() != null);
    }

    @Test
    public void toList()
    {
        List<Person> persons = Lists.newArrayList(person);
        String json = JsonUtil.toJson(persons);
        List<Person> list1 = JsonUtil.toList(json, Person.class);
        System.out.println("list1==" + JsonUtil.toJson(list1));
        Assert.assertTrue(list1.get(0).getCardId() != null);

        String json2 = JsonUtil.toJson(persons, true);
        System.out.println("json2==" + json2);
        Assert.assertTrue(json2.contains("card_id"));
        List<Person> list2 = JsonUtil.toList(json2, Person.class, true);
        System.out.println("list2==" + JsonUtil.toJson(list2));
        Assert.assertTrue(list2.get(0).getCardId() != null);
    }

    @Test
    public void toMap()
    {
        String json = JsonUtil.toJson(person);
        Map<String, Object> map = JsonUtil.toMap(json, String.class, Object.class);
        System.out.println("map==" + JsonUtil.toJson(map));
        Assert.assertTrue(map.containsKey("name"));

        String json2 = JsonUtil.toJson(person, true);
        System.out.println("json2==" + json2);
        Assert.assertTrue(json2.contains("card_id"));
        Map<String, Object> map2 = JsonUtil.toMap(json2, String.class, Object.class, true);
        System.out.println("map2==" + JsonUtil.toJson(map2));
        Assert.assertTrue(map2.containsKey("card_id"));
        Assert.assertTrue(map2.containsKey("name"));
        Person newPerson = JsonUtil.toObject(json2, Person.class, true);
        System.out.println("object2==" + JsonUtil.toJson(newPerson));
        Assert.assertTrue(newPerson.getCardId() != null);
    }

    @Test
    public void toComplex()
    {
        List<Person> persons = Lists.newArrayList(person);
        ResultCode<List<Person>> result = ResultCode.ok(persons);
        String json = JsonUtil.toJson(result);
        System.out.println("complex json==" + json);
        Assert.assertTrue(json.contains("["));

        ResultCode<List<Person>> newResult = JsonUtil.toComplex(json, new TypeReference<ResultCode<List<Person>>>()
        {
        });
        System.out.println("complex new json==" + JsonUtil.toJson(newResult));
        Assert.assertTrue(newResult.getData().size() == 1);
    }

    @Test
    public void toJson2()
    {
        String json1 = JsonUtil.toJson(this.person);
        System.out.println("json1=" + json1);
        Assert.assertTrue(json1.contains("pwd"));

        Set<String> ignoreFields = Sets.newHashSet("pwd");
        String json2 = JsonUtil.toJson(this.person, ignoreFields);
        System.out.println("json2=" + json2);
        Assert.assertTrue(!json2.contains("pwd"));
    }

    @Test
    public void toJson()
    {
        //case1:不忽略+不打码
        String json1 = JsonUtil.toJson(this.person);
        System.out.println("json1=" + json1);
        Assert.assertTrue(json1.contains("20000101"));
        Assert.assertTrue(json1.contains("pwd"));

        //case2:不忽略+打码
        String json2 = JsonUtil.toMask(this.person);
        System.out.println("json2=" + json2);
        Assert.assertTrue(!json2.contains("20000101"));
        Assert.assertTrue(json2.contains("pwd"));

        //case3:忽略+不打码
        Set<String> ignoreFields = Sets.newHashSet("pwd");
        String json3 = JsonUtil.toJson(this.person, ignoreFields);
        System.out.println("json3=" + json3);
        Assert.assertTrue(json3.contains("20000101"));
        Assert.assertTrue(!json3.contains("pwd"));

        //case4:忽略+打码
        String json4 = JsonUtil.toMask(this.person, ignoreFields);
        System.out.println("json4=" + json4);
        Assert.assertTrue(!json4.contains("pwd"));
        Assert.assertTrue(!json4.contains("20000101"));
    }

    @Test
    public void testToJson()
    {
        Person person = new Person();
        person.setName("haha");
        person.setTime(Instant.now());
        String json = JsonUtil.toJson(person);
        System.out.println("json=" + json);
        Assert.assertTrue(json.contains("time"));

        JSONObject jsonObject = new JSONObject(json);
        Object time = jsonObject.get("time");
        Assert.assertTrue((time instanceof Long) && time.toString().length() == 13);
    }

    @Test
    public void testAlias()
    {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", "msg1");
        String json = JsonUtil.toJson(map);
        Result result1 = JsonUtil.toObject(json, Result.class);
        System.out.println("alias json1=" + JsonUtil.toJson(result1));

        Map<String, String> map2 = Maps.newHashMap();
        map2.put("tips", "tip1");
        String json2 = JsonUtil.toJson(map2);
        Result result2 = JsonUtil.toObject(json2, Result.class);
        System.out.println("alias json2=" + JsonUtil.toJson(result2));

        Map<String, String> map3 = Maps.newHashMap();
        map3.put("msg", "msg2");
        String json3 = JsonUtil.toJson(map3);
        Result result3 = JsonUtil.toObject(json3, Result.class);
        System.out.println("alias json3=" + JsonUtil.toJson(result3));
    }

    @Test
    public void testProperty()
    {
        Map<String, String> map = Maps.newHashMap();
        map.put("user", "user1");
        String json = JsonUtil.toJson(map);
        Result result1 = JsonUtil.toObject(json, Result.class);
        System.out.println("property json1=" + JsonUtil.toJson(result1));

        Map<String, String> map2 = Maps.newHashMap();
        map2.put("name", "user2");
        String json2 = JsonUtil.toJson(map2);
        Result result2 = JsonUtil.toObject(json2, Result.class);
        System.out.println("property json2=" + JsonUtil.toJson(result2));

        Result param3 = new Result();
        param3.setUser("user3");
        String json3 = JsonUtil.toJson(param3);
        Result result3 = JsonUtil.toObject(json3, Result.class);
        System.out.println("property json3=" + JsonUtil.toJson(result3));
    }

    @Test
    public void testIgnore()
    {
        Map<String, String> map = Maps.newHashMap();
        map.put("spanId", "span1");
        String json = JsonUtil.toJson(map);
        Result result1 = JsonUtil.toObject(json, Result.class);
        System.out.println("ignore json1=" + JsonUtil.toJson(result1));

        Result param2 = new Result();
        param2.setSpanId("span2");
        System.out.println("ignore json2=" + JsonUtil.toJson(param2));
    }

    @Test
    public void testIgnore2()
    {
        Result result1 = new Result();
        result1.setId("req001");
        String json1 = JsonUtil.toJson(result1);
        System.out.println("json1=" + json1);
        Assert.assertTrue(json1.contains("id"));

        Set<String> ignoreFields = Sets.newHashSet("id");
        String json2 = JsonUtil.toJson(result1, ignoreFields);
        System.out.println("json2=" + json2);
        Assert.assertTrue(!json2.contains("id"));
    }

    @NoArgsConstructor
    @Data
    private static class Result
    {
        @JsonAlias({"message", "tips", "msg"})
        private String msg;

        @JsonProperty("name")
        private String user;

        @JsonIgnore
        private String spanId;

        private String id;
    }

    @NoArgsConstructor
    @Data
    private static class Person
    {
        //姓名
        @JsonMaskAnn
        private String name;

        //人脸base64
        @JsonMaskAnn
        private String base64;

        //密码
        private String pwd;

        //电话号码
        @JsonMaskAnn
        private String phone;

        //身份证号
        @JsonMaskAnn
        private String cardId;

        //银行卡号
        @JsonMaskAnn
        private String bankNo;

        private Instant time;
    }

    @Before
    public void before()
    {
        List<JsonRule> rules = Lists.newArrayList();
        //rule1:姓名字段的第一个字符之后的所有字符都打码
        JsonRule rule1 = new JsonRule();
        rule1.setName("name");
        rule1.setIndex(1);
        rule1.setMask("$");
        rule1.setLen(0);
        rules.add(rule1);

        //rule2:超过50个字符的后面都不显示打码
        JsonRule rule2 = new JsonRule();
        rule2.setName("base64");
        rule2.setIndex(-1);
        rule2.setMask(".");
        rule2.setLen(50);
        rules.add(rule2);

        //rule3:手机号前3后4之外的都脱敏
        JsonRule rule3 = new JsonRule();
        rule3.setName("phone");
        rule3.setIndex(3);
        rule3.setLen(-4);
        rules.add(rule3);

        //rule4:身份证号6后4之外的都脱敏
        JsonRule rule4 = new JsonRule();
        rule4.setName("cardId");
        rule4.setIndex(6);
        rule4.setLen(-4);
        rules.add(rule4);

        //rule5:银行卡号前6后4之外的都脱敏
        JsonRule rule5 = new JsonRule();
        rule5.setName("bankNo");
        rule5.setIndex(6);
        rule5.setLen(-4);
        rules.add(rule5);

        JsonRuleMgr.addRule(rules);

        person.setBase64("/9j/4AAQSkZJRgABAQEAYABgAAD/4QAwRXhpZgAATU0AKgAAAAgAAQExAAIAAAAOAAAAGgAAVpdHUuY29tAP/bAEMA");
        person.setName("狄仁杰");
        person.setPhone("13234567890");
        person.setPwd("123456");
        person.setCardId("444444200001016666");
        person.setBankNo("666444444200001016666333");
    }

    private Person person = new Person();
}
package com.acooly.core.test.utils;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.arithmetic.tree.TreeNode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2019-05-16 17:08
 */
@Slf4j
public class ToStringTest {

    String userName = "zhangpu";
    String idCardNo = "510221198209476371";
    String mobileNo = "13896177630";
    String bankCardNo = "6221880231092323876";
    String email = "zhangpu@acooly.cn";

    @Test
    public void testSimpleEntityToString() {
//        ToString.logSource = true;
        MaskEntity maskEntity = new MaskEntity();
        System.out.println(maskEntity);
    }

    @Test
    public void testTreeEntityToString() {
        ToString.logSource = true;
        MaskNode maskNode = new MaskNode(1L, null, "TOP");
        MaskNode subNode1 = new MaskNode(11L, 1L, "firstLevel1");
        maskNode.setChildren(Lists.newArrayList(subNode1));
        System.out.println(maskNode);
    }

    @Test
    public void testJsonHandle() {
        String json = "{\"bankCardNo\":\"6221************876\"," +
                "\"customAllMask\":\"******\"," +
                "\"customProperty\":\"123*************89\"," +
                "\"customXProperty\":\"******************\"," +
                "\"email\":\"zh*****@acooly.cn\"," +
                "\"idCardNo\":\"510***********6371\"," +
                "\"lists\":[\"123\",\"234\",\"abc\",\"中国\"]," +
                "\"longAmount\":1289," +
                "\"maskSubEntities\":[{\"num1\":\"12312312\",\"text1\":\"1231*****213\",\"text2\":\"123123123123\"}]," +
                "\"maskSubEntity\": {\"num1\":\"12312312\",\"text1\":\"1231*****213\",\"text2\":\"123123123123\"}," +
                "\"mobileNo\":\"1311111111630\"," +
                "\"userName\":\"null\"}";
        JSONObject jsonObject = JSON.parseObject(json);
//        jsonObject.remove("text1");
//        jsonObject.replace("text2", "text2");
//        jsonObject.replace("mobileNo", "13897677630")

        List<String> ignores = Lists.newArrayList("text1");
        List<String> masks = Lists.newArrayList("mobileNo");

        Map map = doParse(jsonObject, ignores, masks);
        System.out.println(JSON.toJSONString(map, true));
    }

    protected Map<Object, Object> doParse(JSONObject jsonObject, List<String> ignores, List<String> masks) {
        Map<Object, Object> newMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getValue() instanceof JSONObject) {
                newMap.put(entry.getKey(), doParse((JSONObject) entry.getValue(), ignores, masks));
            } else if (entry.getValue() instanceof JSONArray) {
                newMap.put(entry.getKey(),parseArray((JSONArray) entry.getValue(), ignores, masks));
            } else {

                if (ignores.contains(entry.getKey())) {
                    continue;
                } else if (masks.contains(entry.getKey())) {
                    newMap.put(entry.getKey(), ToString.mask(String.valueOf(entry.getValue())));
                } else {
                    newMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return newMap;
    }

    protected List<Object> parseArray(JSONArray jsonArray, List<String> ignores, List<String> masks) {
        List<Object> list = Lists.newArrayList();
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject) {
                list.add(doParse((JSONObject) obj, ignores, masks));
            } else if (obj instanceof JSONArray) {
                list.add(parseArray((JSONArray) obj, ignores, masks));
            } else {
                list.add(obj);
            }
        }
        return list;
    }


    @Getter
    @Setter
    public static class MaskEntity {

        @ToString.Maskable
        String customXProperty = "123456789123456789";

        @ToString.Maskable(maskType = Strings.MaskType.UserName)
        String userName;

        @ToString.Maskable(maskType = Strings.MaskType.IdCardNo)
        String idCardNo = "510221198209476371";

        @ToString.Maskable(maskType = Strings.MaskType.MobileNo)
        String mobileNo = "13896177630";

        @ToString.Maskable(maskType = Strings.MaskType.BankCardNo)
        String bankCardNo = "6221880231092323876";

        @ToString.Maskable(maskType = Strings.MaskType.Email)
        String email = "zhangpu@acooly.cn";

        @ToString.Maskable(prefixLen = 3, postfixLen = 2)
        String customProperty = "123456789123456789";

        @ToString.Maskable(maskAll = true)
        String customAllMask = "123456789123456789";

        @ToString.Maskable(prefixLen = 3, postfixLen = 2)
        Long longAmount = 123456789L;

        /**
         * 不支持Maskable,支持Invisible
         */
        @ToString.Maskable(maskAll = true)
        @ToString.Invisible
        private Date now = new Date();

        /**
         * 简单对象List
         */
        @ToString.Maskable(maskAll = true)
        List<String> lists = Lists.newArrayList("123", "234", "abc", "中国");

        MaskSubEntity maskSubEntity = new MaskSubEntity();

        List<MaskSubEntity> maskSubEntities = Lists.newArrayList(new MaskSubEntity());

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }

    @Getter
    @Setter
    public static class MaskSubEntity {
        @ToString.Maskable(maskType = Strings.MaskType.BankCardNo)
        private String text1 = "123123123213";
        private String text2 = "123123123123";
        private Long num1 = 12312312L;

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }

    @Getter
    @Setter
    public static class MaskNode implements TreeNode<MaskNode> {

        private Long id;

        private Long parentId;

        private String name;

        @ToString.Maskable(maskType = Strings.MaskType.UserName)
        String userName = "zhangpu" + id;

        @ToString.Maskable(maskType = Strings.MaskType.IdCardNo)
        String idCardNo = "510221198209476371" + id;

        @ToString.Maskable(maskType = Strings.MaskType.MobileNo)
        String mobileNo = "13896177630";

        private List<MaskNode> children;

        public MaskNode() {
        }

        public MaskNode(Long id, Long parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }

}

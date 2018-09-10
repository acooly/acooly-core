package com.acooly.core.utils;

import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.enums.Zodiac;
import com.acooly.core.common.facade.InfoBase;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 身份证号码工具
 *
 * @author zhangpu
 * @author shuijing
 */
@Slf4j
public class IdCards {
    /**
     * 省
     */
    private static Map<String, String> provinces;

    /**
     * 市
     */
    private static Map<String, String> citys;

    /**
     * 区
     */
    private static Map<String, String> areas;

    /**
     * 6位区码对应
     */
    private static Map<String, Map<String, String>> govCodes;

    public static final String PROVINCE_CODE = "provinceCode";

    public static final String CITY_CODE = "cityCode";

    public static final String AREA_CODE = "areaCode";


    /**
     * 身份证号码前17位各位校验计算值
     */
    private static int[] IDCARD_CHECK_CALC_VALS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 身份证号码校验位码表
     */
    private static char[] IDCARD_VERIFY_CODES = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};


    static {
        //解析省
        Map provinceJsons = getJsonMap("META-INF/idcard/province.json.zip");
        provinces = praseJsonMap(provinceJsons);

        //解析市
        Map cityJsons = getJsonMap("META-INF/idcard/city.json.zip");
        citys = praseJsonMap(cityJsons);

        //解析区
        Map areaJsons = getJsonMap("META-INF/idcard/area.json.zip");
        areas = praseJsonMap(areaJsons);

        //解析6位码对应
        Map govCodeJsons = getJsonMap("META-INF/idcard/govCode.json.zip");
        govCodes = praseGovCode(govCodeJsons);

    }

    private static Map<String, String> praseJsonMap(Map jsonMap) {
        Map<String, String> jprovinces = Maps.newHashMapWithExpectedSize(jsonMap.size());
        jsonMap.keySet().forEach(v -> {
            Map lm = (Map) jsonMap.get(v);
            String code = (String) lm.get("text");
            jprovinces.put(v.toString(), code);
        });
        return jprovinces;
    }

    private static Map<String, Map<String, String>> praseGovCode(Map jsonMap) {
        Map<String, Map<String, String>> jgovCode = Maps.newHashMap();
        jsonMap.keySet().forEach(v -> {
            Map lm = (Map) jsonMap.get(v);

            String code = (String) lm.get("code");
            String provinceCode = (String) lm.get(PROVINCE_CODE);
            String cityCode = (String) lm.get(CITY_CODE);
            String areaCode = (String) lm.get(AREA_CODE);

            Map<String, String> innerMap = Maps.newHashMap();
            innerMap.put(PROVINCE_CODE, provinces.get(provinceCode));
            innerMap.put(CITY_CODE, citys.get(cityCode));
            innerMap.put(AREA_CODE, areas.get(areaCode));

            jgovCode.put(code, innerMap);
        });
        return jgovCode;
    }

    private static Map getJsonMap(String idcardPath) {
        Map map = null;
        try (InputStream inputStream = IdCards.class.getClassLoader().getResourceAsStream(idcardPath)) {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            zipInputStream.getNextEntry();
            map = JSON.parseObject(zipInputStream, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 升级
     * <p>
     * 15位身份证号码为18位
     *
     * @param oldCardNo
     * @return
     */
    public static String upgrade(String oldCardNo) {
        if (Strings.isNotBlank(oldCardNo) && Strings.length(oldCardNo) == 15) {
            oldCardNo = oldCardNo.trim();
            StringBuffer sb = new StringBuffer(oldCardNo);
            sb.insert(6, "19");
            sb.append(calcCheckBit(sb.toString()));
            return sb.toString();
        } else {
            return oldCardNo;
        }
    }

    /**
     * 验证
     * <p>
     * 验证18位身份证号码
     *
     * @param cardNo
     * @return
     */
    public static boolean verify(String cardNo) {
        return Strings.equalsIgnoreCase(calcCheckBit(cardNo), Strings.right(cardNo, 1));
    }

    /**
     * 解析身份证信息
     *
     * @param cardNo
     * @return
     */
    public static IdCardInfo parse(String cardNo) {
        return parseIdCard(cardNo);
    }

    @Deprecated
    public static IdCardInfo parseIdCard(String cardNo) {

        if (StringUtils.isBlank(cardNo)) {
            throw new RuntimeException("身份证号码不能为空");
        }
        int len = StringUtils.trimToEmpty(cardNo).length();
        if (len != 15 && len != 18) {
            throw new RuntimeException("身份证号码长度错误，只能是15或18位");
        }
        if (len == 15) {
            cardNo = upgrade(cardNo);
        }
        IdCardInfo info = new IdCardInfo();
        info.setCardNo(cardNo);
        setPlaceInfo(info, cardNo);
        info.setBirthday(StringUtils.substring(cardNo, 6, 14));
        info.setGender(Integer.parseInt(StringUtils.substring(cardNo, 14, 17)) % 2 == 0 ? Gender.female : Gender.male);
        int month = Integer.parseInt(StringUtils.substring(cardNo, 10, 12));
        int date = Integer.parseInt(StringUtils.substring(cardNo, 12, 14));
        info.setZodiac(Zodiac.to(month, date));
        return info;
    }

    private static void setPlaceInfo(IdCardInfo info, String cardNo) {
        String govNo = StringUtils.substring(cardNo, 0, 6);
        Map<String, String> govMap = govCodes.get(govNo);
        if (govMap != null) {
            info.setProvince(govMap.get(PROVINCE_CODE));
            info.setCity(govMap.get(CITY_CODE));
            info.setArea(govMap.get(AREA_CODE));
        }
    }


    private static String calcCheckBit(String partCardNo) {
        String newCardNo = partCardNo;
        if (Strings.length(newCardNo) != 17) {
            newCardNo = Strings.substring(newCardNo, 0, 17);
        }
        char[] ch = newCardNo.toCharArray();
        int sum = 0;
        for (int i = 0; i < newCardNo.length(); i++) {
            sum += (ch[i] - '0') * IDCARD_CHECK_CALC_VALS[i];
        }
        int residue = sum % 11;
        return String.valueOf(IDCARD_VERIFY_CODES[residue]);
    }


    @Getter
    @Setter
    public static class IdCardInfo extends InfoBase {
        private String cardNo;
        private Gender gender;
        private String birthday;
        private String province;
        private String city;
        private String area;
        private Zodiac zodiac;
    }

}

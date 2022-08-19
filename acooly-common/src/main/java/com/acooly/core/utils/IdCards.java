package com.acooly.core.utils;

import com.acooly.core.common.enums.AnimalSign;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.common.enums.StarSign;
import com.acooly.core.common.facade.InfoBase;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Date;
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

    public static final int IDCARD_LENGTH_OLD = 15;
    public static final int IDCARD_LENGTH_NEW = 18;

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
        Map<String, Map<String, Object>> provinceJsons = getJsonMap("META-INF/idcard/province.json.zip");
        provinces = parseJsonMap(provinceJsons);

        //解析市
        Map<String, Map<String, Object>> cityJsons = getJsonMap("META-INF/idcard/city.json.zip");
        citys = parseJsonMap(cityJsons);

        //解析区
        Map<String, Map<String, Object>> areaJsons = getJsonMap("META-INF/idcard/area.json.zip");
        areas = parseJsonMap(areaJsons);

        //解析6位码对应
        Map<String, Map<String, Object>> govCodeJsons = getJsonMap("META-INF/idcard/govCode.json.zip");
        govCodes = parseGovCode(govCodeJsons);

    }

    private static Map<String, String> parseJsonMap(Map<String, Map<String, Object>> jsonMap) {
        Map<String, String> jProvinces = Maps.newHashMapWithExpectedSize(jsonMap.size());
        jsonMap.keySet().forEach(v -> {
        	Map<String, Object> lm = jsonMap.get(v);
            String code = (String) lm.get("text");
            jProvinces.put(v.toString(), code);
        });
        return jProvinces;
    }

    private static Map<String, Map<String, String>> parseGovCode(Map<String, Map<String, Object>> jsonMap) {
        Map<String, Map<String, String>> jGovCode = Maps.newHashMap();
        jsonMap.keySet().forEach(v -> {
            Map<String, Object> lm = jsonMap.get(v);
            String code = (String) lm.get("code");
            String provinceCode = (String) lm.get(PROVINCE_CODE);
            String cityCode = (String) lm.get(CITY_CODE);
            String areaCode = (String) lm.get(AREA_CODE);
            Map<String, String> innerMap = Maps.newHashMap();
            innerMap.put(PROVINCE_CODE, provinces.get(provinceCode));
            innerMap.put(CITY_CODE, citys.get(cityCode));
            innerMap.put(AREA_CODE, areas.get(areaCode));
            jGovCode.put(code, innerMap);
        });
        return jGovCode;
    }

    private static Map<String, Map<String, Object>> getJsonMap(String idcardPath) {
        Map<String, Map<String, Object>> map = null;
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
        if (Strings.isNotBlank(oldCardNo) && Strings.length(oldCardNo) == IDCARD_LENGTH_OLD) {
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
        return doParse(cardNo);
    }

    @Deprecated
    public static IdCardInfo parseIdCard(String cardNo) {
        return doParse(cardNo);
    }

    protected static IdCardInfo doParse(String cardNo) {
        if (StringUtils.isBlank(cardNo)) {
            throw new RuntimeException("身份证号码不能为空");
        }
        int len = StringUtils.trimToEmpty(cardNo).length();
        if (len != IDCARD_LENGTH_OLD && len != IDCARD_LENGTH_NEW) {
            throw new RuntimeException("身份证号码长度错误，只能是15或18位");
        }
        if (len == IDCARD_LENGTH_OLD) {
            cardNo = upgrade(cardNo);
        }
        IdCardInfo info = new IdCardInfo();
        info.setCardNo(cardNo);
        setPlaceInfo(info, cardNo);
        info.setBirthday(StringUtils.substring(cardNo, 6, 14));
        info.setBirthdate(Dates.parse(info.birthday, "yyyyMMdd"));
        info.setStarSign(StarSign.to(info.birthdate));
        info.setAnimalSign(AnimalSign.to(info.birthdate));
        info.setGender(Integer.parseInt(StringUtils.substring(cardNo, 14, 17)) % 2 == 0 ? Gender.female : Gender.male);
        return info;
    }

    private static void setPlaceInfo(IdCardInfo info, String cardNo) {
        String govNo = StringUtils.substring(cardNo, 0, 6);
        info.setRegionCode(govNo);
        Map<String, String> govMap = govCodes.get(govNo);
        if (govMap != null) {
            info.setProvince(govMap.get(PROVINCE_CODE));
            info.setCity(govMap.get(CITY_CODE));
            info.setArea(govMap.get(AREA_CODE));
        }
    }


    private static String calcCheckBit(String partCardNo) {
        String newCardNo = partCardNo;
        if (Strings.length(newCardNo) != IDCARD_LENGTH_NEW - 1) {
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
        private Date birthdate;
        private String regionCode;
        private String province;
        private String city;
        private String area;

        /**
         * 星座
         */
        private StarSign starSign;
        /**
         * 生肖
         */
        private AnimalSign animalSign;
    }

}

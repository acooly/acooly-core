package com.acooly.core.test.utils.bean;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Lists;

import java.util.Date;
import java.util.List;

/**
 * @author zhangpu
 * @date 2019-05-23 11:54
 */
@Getter
@Setter
public class MaskEntity {

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

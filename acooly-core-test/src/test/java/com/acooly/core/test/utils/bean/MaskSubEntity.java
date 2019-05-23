package com.acooly.core.test.utils.bean;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangpu
 * @date 2019-05-23 11:54
 */
@Getter
@Setter
public class MaskSubEntity {
    @ToString.Maskable(maskType = Strings.MaskType.BankCardNo)
    private String text1 = "123123123213";
    private String text2 = "123123123123";
    private Long num1 = 12312312L;

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
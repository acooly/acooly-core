package com.acooly.core.test.utils;

import com.acooly.core.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author qiuboboy@qq.com
 * @date 2019-05-16 17:08
 */
@Slf4j
public class StringsTest {

    String userName = "zhangpu";
    String idCardNo = "510221198209476371";
    String mobileNo = "13896177630";
    String bankCardNo = "6221880231092323876";
    String email = "zhangpu@acooly.cn";

    @Test
    public void testMaskByMaskType() {
        System.out.println("用户名: " + Strings.mask(userName, Strings.MaskType.UserName));
        System.out.println("身份证: " + Strings.mask(idCardNo, Strings.MaskType.IdCardNo));
        System.out.println("手机号: " + Strings.mask(mobileNo, Strings.MaskType.MobileNo));
        System.out.println("银行卡: " + Strings.mask(bankCardNo, Strings.MaskType.BankCardNo));
        System.out.println("邮件: " + Strings.mask(email, Strings.MaskType.Email));
    }

    @Test
    public void testMaskReverse() {
        System.out.println("用户名: " + Strings.maskReverse(userName, 6, 0, '*', 5));
        System.out.println("身份证: " + Strings.maskReverse(idCardNo, 3, 4, '*'));
        System.out.println("手机号: " + Strings.maskReverse(mobileNo, 3, 3, '*'));
        System.out.println("银行卡: " + Strings.maskReverse(bankCardNo, 4, 3, '*'));
    }

}

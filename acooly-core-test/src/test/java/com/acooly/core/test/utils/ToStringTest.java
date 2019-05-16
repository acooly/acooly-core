package com.acooly.core.test.utils;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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
        MaskEntity maskEntity = new MaskEntity();
        System.out.println(maskEntity);
    }


    @Getter
    @Setter
    public static class MaskEntity {
        @ToString.Maskable(maskType = Strings.MaskType.UserName)
        String userName = "zhangpu";

        @ToString.Maskable(maskType = Strings.MaskType.IdCardNo)
        String idCardNo = "510221198209476371";

        @ToString.Maskable(maskType = Strings.MaskType.MobileNo)
        String mobileNo = "13896177630";

        @ToString.Maskable(maskType = Strings.MaskType.BankCardNo)
        String bankCardNo = "6221880231092323876";

        @ToString.Maskable(maskType = Strings.MaskType.Email)
        String email = "zhangpu@acooly.cn";

        @Override
        public String toString() {
            return ToString.toString(this);
        }
    }

}

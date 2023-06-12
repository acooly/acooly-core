package com.acooly.core.utils.arithmetic.perm;

import com.acooly.core.utils.Exceptions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 位运算权限控制工具
 *
 * @author zhangpu
 * @date 2018-12-06 15:43
 */
@Slf4j
public class BitPermissions {


    /**
     * 与运算权限控制：判断是否有权限
     *
     * @param permValue 你的权限值 (你拥有权限的权限单元值的代数和，如：3表示你拥有1+2两个权限)
     * @param permUnit  权限单元值，每个值代表一个权限 （2的幂次方：1,2,4,8,16,32,64,128...）
     * @return true:有权限，false:无权限
     */
    public static boolean hasPerm(int permValue, int permUnit) {
        checkPermUnit(permUnit);
        return (permValue & permUnit) == permUnit;
    }


    /**
     * 与运算权限控制：计算权限值
     *
     * @param permUnits 权限单元值集合
     * @return 总计权限值
     */
    public static int calcPerm(Integer... permUnits) {
        return calcPerm(Lists.newArrayList(permUnits));
    }

    public static int calcPerm(List<Integer> permUnits) {
        int permValue = 0;
        for (int permUnit : permUnits) {
            checkPermUnit(permUnit);
            permValue = permValue + permUnit;
        }
        return permValue;
    }


    private static void checkPermUnit(int permUnit) {
        if ((permUnit & (permUnit - 1)) != 0) {
            throw Exceptions.runtimeException("权限单元permUnit必须是2的幂次方");
        }
    }


    /**
     * main方法方式Demo和测试
     *
     * @param args
     */
    public static void main(String[] args) {

        // 规划权限单元有（10个）：1, 2, 4, 8, 16, 32, 64, 128, 256, 512
        Integer[] permUnits = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};
        // 加上用户A拥有的权限有：1,8,32,512 这4个权限，则：
        int userApermValue = BitPermissions.calcPerm(new Integer[]{1, 8, 32, 512});
        log.info("用户A的权限值为：{}", userApermValue);
        // 分别判断用户A是否有这10个权限
        for (int permUnit : permUnits) {
            log.info("用户A 判断是否有权限单元:{} 的权限: {}", permUnit, BitPermissions.hasPerm(userApermValue, permUnit));
        }

    }

}

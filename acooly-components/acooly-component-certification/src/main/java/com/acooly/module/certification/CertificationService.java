/*
 * 修订记录:
 * zhike@yiji.com 2017-04-21 10:13 创建
 *
 */
package com.acooly.module.certification;

import com.acooly.module.certification.enums.CertResult;

/**
 * 修订记录：
 *
 * @author zhike@yiji.com
 */

public interface CertificationService {

    /**
     * 单条实名认证
     *
     * @param realName
     * @param idCardNo
     */
    CertResult certification(String realName, String idCardNo);
}

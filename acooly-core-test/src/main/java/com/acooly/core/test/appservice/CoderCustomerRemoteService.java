/**
 * acooly-core-5.2
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-11-16 23:11
 */
package com.acooly.core.test.appservice;

import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;
import com.acooly.core.test.core.entity.CoderCustomer;

/**
 * @author zhangpu
 * @date 2022-11-16 23:11
 */
public interface CoderCustomerRemoteService {

    /**
     * 根据ID唯一查询
     *
     * @param idOrder
     * @return
     */
    SingleResult<CoderCustomer> getUniqueOne(SingleOrder<Long> idOrder);

}

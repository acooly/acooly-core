/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2022-08-13 14:43
 */
package com.acooly.core.test.cloud.feign;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.web.support.JsonEntityResult;
import com.acooly.core.common.web.support.JsonListResult;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.test.core.entity.CoderCustomer;
import com.acooly.core.test.core.service.CoderCustomerService;
import com.acooly.core.utils.enums.Messageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangpu
 * @date 2022-08-13 14:43
 */
@Slf4j
@RestController
@RequestMapping("/cloud/service/customer")
public class CustomerProviderController {

    @Autowired
    private CoderCustomerService coderCustomerService;

    @RequestMapping(value = "page")
    public JsonListResult<CoderCustomer> page() {
        JsonListResult<CoderCustomer> result = new JsonListResult<>();
        try {
            PageInfo<CoderCustomer> pageInfo = new PageInfo<>();
            pageInfo = coderCustomerService.query(pageInfo, null, null);
            result.setTotal(pageInfo.getTotalCount());
            result.setRows(pageInfo.getPageResults());
            result.setHasNext(pageInfo.hasNext());
            result.setPageNo(pageInfo.getCurrentPage());
            result.setPageSize(pageInfo.getCountOfCurrentPage());
        } catch (Exception e) {
            handleException(result, "分页查询", e);
        }
        return result;
    }

    @RequestMapping(value = "create")
    public JsonEntityResult<CoderCustomer> create(CoderCustomer coderCustomer) {
        JsonEntityResult<CoderCustomer> result = new JsonEntityResult<>();
        coderCustomerService.save(coderCustomer);
        return result;
    }

    @RequestMapping(value = "read")
    public JsonEntityResult<CoderCustomer> read(@RequestParam("id") Long id) {
        JsonEntityResult<CoderCustomer> result = new JsonEntityResult<>();
        try {
            result.setEntity(coderCustomerService.get(id));
            result.setMessage("读取成功");
        } catch (Exception e) {
            handleException(result, "读取", e);
        }
        return result;
    }

    @RequestMapping(value = "delete")
    public JsonResult delete(@RequestParam("id") Long id) {
        JsonResult result = new JsonResult();
        try {
            coderCustomerService.removeById(id);
        } catch (Exception e) {
            handleException(result, "删除", e);
        }
        return result;
    }

    protected void handleException(JsonResult result, String action, Exception e) {
        String message = null;
        result.setSuccess(false);
        if (e instanceof Messageable) {
            Messageable be = (Messageable) e;
            result.setCode(be.code());
            result.setMessage(be.message());
        }
        log.error(result.getMessage(), e);
    }

}

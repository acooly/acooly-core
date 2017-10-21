package com.acooly.module.olog.storage.facade;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.olog.facade.api.OlogFacade;
import com.acooly.module.olog.facade.order.OlogOrder;
import com.acooly.module.olog.facade.result.OlogResult;
import com.acooly.module.olog.storage.domain.OlogEntity;
import com.acooly.module.olog.storage.service.OlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author qiubo@yiji.com */
@Component
public class OlogFacadeImpl implements OlogFacade {
  @Autowired private OlogService ologService;

  @Override
  public OlogResult logger(OlogOrder order) {
    OlogResult result = new OlogResult();
    try {
      order.check();
      OlogEntity ologEntity = new OlogEntity();
      ologEntity.from(order);
      ologService.save(ologEntity);
    } catch (Exception e) {
      result.setStatus(ResultStatus.failure);
      result.setDetail(e.getMessage());
    }
    return result;
  }
}

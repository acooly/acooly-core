package com.acooly.module.olog.collector.client;

import com.acooly.core.utils.Ids;
import com.acooly.module.olog.facade.api.OlogFacade;
import com.acooly.module.olog.facade.order.OlogOrder;
import com.acooly.module.olog.facade.result.OlogResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author qiubo@yiji.com */
@Component
public class OlogClient implements OlogFacade {
  @Autowired private OlogFacade ologFacade;

  @Override
  public OlogResult logger(OlogOrder order) {
    order.setGid(Ids.gid());
    order.setPartnerId("0");
    return ologFacade.logger(order);
  }
}

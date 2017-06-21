package com.acooly.core.utils.id;

import com.acooly.core.utils.system.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetAddressIdWorker
    implements IdentifierGenerator<Long>, IdentifierAnalyzer<Long, SnowflakeIDElement> {
  private Logger logger = LoggerFactory.getLogger(NetAddressIdWorker.class);
  private long workerId = 0L;
  private SnowflakeIDGenerator idGen = null;

  public NetAddressIdWorker() {
    try {
      String ip = IPUtil.getFirstNoLoopbackIPV4Address();
      this.logger.info("IP Address:" + ip);
      String last = ip.substring(ip.lastIndexOf('.') + 1);
      this.workerId = Long.parseLong(last);
    } catch (Exception e) {
      this.logger.error("Error Initializing ID Generator : " + e.getMessage(), e);
      System.exit(-1);
    }
    this.idGen = new SnowflakeIDGenerator(this.workerId);
  }

  public Long generate() {
    return this.idGen.generate();
  }

  public SnowflakeIDElement analyze(Long id) {
    return this.idGen.analyze(id);
  }
}

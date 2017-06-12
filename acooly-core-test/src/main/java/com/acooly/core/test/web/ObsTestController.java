package com.acooly.core.test.web;

import com.acooly.module.obs.ObsService;
import com.acooly.module.obs.model.ObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/** @author shuijing */
@Slf4j
@RestController
@RequestMapping(value = "/obstest")
public class ObsTestController {
  @Autowired private ObsService obsService;

  @RequestMapping("aliyun")
  public void testAliyunObs() {
    String bucketName = "yijifu-acooly";
    String key = "test/logo.png";
    File file = new File("D:\\tmp\\t.log");
    ObjectResult result = obsService.putObject(bucketName, key, file);
    log.info("put success:" + result.getUrl());
  }
}

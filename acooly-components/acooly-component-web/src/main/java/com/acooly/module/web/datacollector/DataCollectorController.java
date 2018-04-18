package com.acooly.module.web.datacollector;

import com.acooly.core.common.log.BusinessLog;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCollectorController {
    @RequestMapping("/xdata/ingest.data")
    public void collectData(@RequestBody BusinessLog businessLog) {
        BusinessLog.log(businessLog);
    }
}

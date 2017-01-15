#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */
package ${package};

import com.yiji.boot.core.Apps;
import org.springframework.boot.SpringApplication;
import com.yiji.boot.core.YijiBootApplication;


/**
 * @author qiubo@yiji.com
 */
@YijiBootApplication(sysName = "${rootArtifactId}", httpPort = ${webport})
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfNotExists("sdev");
        new SpringApplication(Main.class).run(args);
    }
}
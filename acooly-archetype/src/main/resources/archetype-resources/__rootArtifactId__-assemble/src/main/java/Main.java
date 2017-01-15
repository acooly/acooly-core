#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;
import com.acooly.core.common.BootApp;


/**
 * @author qiubo
 */
@BootApp(sysName = "${rootArtifactId}", httpPort = ${webport})
public class Main {
    public static void main(String[] args) {
        Apps.setProfileIfNotExists("sdev");
        new SpringApplication(Main.class).run(args);
    }
}
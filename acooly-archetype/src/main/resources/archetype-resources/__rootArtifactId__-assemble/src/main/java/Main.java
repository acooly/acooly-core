import com.acooly.core.common.BootApp;
import com.acooly.core.common.boot.Apps;
import org.springframework.boot.SpringApplication;

#set($symbol_pound='#')
        #set($symbol_dollar='$')
        #set($symbol_escape='\' )
        package ${package};


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
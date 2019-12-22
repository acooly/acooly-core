package com.acooly.core.common.transformer;

import com.acooly.core.common.boot.Apps;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
public class AgentInitializer implements ApplicationContextInitializer {

    public AgentInitializer() {

    }
    static {
        // make sure the agent load earlier
        Apps.INSTRUMENTATION = ByteBuddyAgent.install();
    }

    @Override
    public void initialize( ConfigurableApplicationContext applicationContext ) {
        //do nothing
    }

    /**
     * 容器感知
     */
    private void containerAware() {
        String path = "/proc/1/cgroup";
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String tmp;
            while (( tmp = in.readLine() ) != null) {
                if (tmp.contains("docker") || tmp.contains("kubepods")) {
                    Apps.isContainerd = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            //not in linux 忽略
        } catch (IOException e) {
            // do nothing
        }
    }


}

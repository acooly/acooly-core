#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

package ${package}.test;
import com.acooly.core.common.boot.Apps;
import com.acooly.module.test.AppTestBase;



/**
 * 不启动web容器的测试父类
 *
 * @author qiubo
 */
public abstract class NoWebTestBase extends AppTestBase {
    protected static final String PROFILE = "dev";

    static {
        Apps.setProfileIfNotExists(PROFILE);
    }

}

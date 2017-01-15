#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-09-20 00:43 创建
 *
 */
package ${package}.test;

import org.junit.Test;

import static com.github.kevinsawicki.http.HttpRequest.get;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author qiubo@yiji.com
 */
public class DemoTest extends TestBase {
    @Test
    public void testController() throws Exception {
        String url = buildUrl("hello");
        assertThat(get(url).body()).isEqualTo("hello world");
    }
    @Test
    public void testStaticResouce() throws Exception {
        String url = buildUrl("demo.html");
        assertThat(get(url).body()).isEqualTo("demo");
    }
    @Test
    public void testNotFound() throws Exception {
        String path = "xxxx";
        String url = buildUrl(path);
        assertThat(get(url).body()).contains("status=404");
        assertThat(get(url).body()).contains("path=/" + path);
    }
}

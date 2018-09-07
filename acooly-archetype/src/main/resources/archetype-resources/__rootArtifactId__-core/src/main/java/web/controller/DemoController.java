#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * @author qiubo
 */
@Controller
public class DemoController {
    @RequestMapping("/index")
    public String demo() {
        return "index";
    }
}

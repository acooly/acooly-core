package com.acooly.core.test.web;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.test.domain.App;
import com.acooly.core.utils.Money;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

/**
 * Zhouxi O_o
 *
 * @author xi
 * @description
 */
@Service
public class AppService extends EntityServiceImpl<App, Appdao> {


    @Transactional
    public void doit( HttpServletRequest request, HttpServletResponse response, Model model ) {
        App s = new App();
        s.setDisplayName("sss");
        s.setName("sss");
        s.setParentId(12121L);
        s.setType("ssss");
        s.setUserId(32323L);
        s.setParentAppId(121212L);
        s.setPrice(new Money(0));

        save(s);
        query(new PageInfo<>(),new HashMap<>());
    }

}

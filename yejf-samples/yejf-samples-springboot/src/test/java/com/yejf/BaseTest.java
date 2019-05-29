package com.yejf;

import com.alibaba.fastjson.JSON;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yejf
 * @date 2019/5/24 17:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
    public void out(String tc, Object result) {
        System.out.println("------" + tc + "------");
        System.out.println(JSON.toJSONString(result));
    }
}

package com.maoyan.bigdata.datalink.api;

import com.maoyan.bigdata.datalink.utils.TipUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class TestController {


    @GetMapping("/1")
    public Object test() {
        return TipUtil.success("success");
    }

}

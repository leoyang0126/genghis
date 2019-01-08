package com.leoyang.genghis.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leoyang.genghis.server.annotations.Controller;
import com.leoyang.genghis.server.annotations.RequestMapping;

/**
 * Created by yang.liu on 2018/11/15
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(String param) {
        System.out.println(param);
        return "ok";
    }

    @RequestMapping("/test/invoke")
    public String invokeTest(JSONObject param) {
        System.out.println("-----");
        return JSON.toJSONString(param);
    }
}

package com.inview.rentserver.controller;

import org.junit.jupiter.api.Test;
import person.inview.tools.RandomUtil;
import person.inview.tools.StringCompress;

class UpdateControllerTest {

    @Test
    void updateOrAdd() {
    }

    @Test
    void compress(){

        String ss = RandomUtil.randomNumbers(1000);
        System.out.println(ss);
        System.out.println(ss.length());
        String s1=StringCompress.zipBase64(ss);
        System.out.println(s1);
        System.out.println(s1.length());
        System.out.println(StringCompress.unZipBase64(s1));
    }
}
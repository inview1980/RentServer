package com.inview.rentserver.tool;

import org.junit.jupiter.api.Test;
import person.inview.tools.PropertyUtil;

import java.io.IOException;
import java.util.Properties;

public class OtherTest {
    @Test
    void propertyTest() throws IOException {
        String filename = "config.ini";
        Properties pp=new Properties();
        pp.setProperty("aa", "aa");
        pp.setProperty("bb", "bb");
        pp.setProperty("cc", "cc");
        PropertyUtil.writeProperty(filename, pp);

        Properties p2 = PropertyUtil.readProperty(filename);
        p2.list(System.out);
    }
}

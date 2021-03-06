package com.inview.rentserver.tool;

import com.alibaba.fastjson.JSONObject;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import person.inview.iface.IClassName;
import person.inview.receiver.Receiver;
import person.inview.tools.EncryptUtil;
import person.inview.tools.PropertyUtil;
import pojo.RentalRecord;
import pojo.RoomDetails;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.websocket.Decoder;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class OtherTest {
    @Test
    void propertyTest() throws Exception {
        RentalRecord rr = new RentalRecord();
        IClassName<BigDecimal> fun = rr::setDeposit;
        System.out.println(fun.getImplMethodName());

        List<Class<?>> classList = fun.getParameterTypes();
        classList.forEach(System.out::println);

        System.out.println(fun.getReturnType());
    }

    @Test
    void buildJSON() {
        Map<String, Object> map = new HashMap<>();
        PodamFactory factory = new PodamFactoryImpl();
        RoomDetails room = factory.manufacturePojo(RoomDetails.class);
        String str = JSONObject.toJSONString(room);
        map.put("room", str);
        Receiver receiver = new Receiver(11L, "changeRoom", JSONObject.toJSONString(map), "null");
        System.out.println(JSONObject.toJSONString(receiver));
    }

    @Test
    void encrypt() {
        //加密工具
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword("jjss2223kl");
        //加密
        String ciphertext = encryptor.encrypt("**");
        System.out.println("jjss2223kl" + " : " + ciphertext);
    }

    @Test
    void encrypt2() {
        String str = EncryptUtil.HmacSHA256("YW420102zxcvbnm,.asdfghjkl;", "**");
        System.out.println(str);
    }
}

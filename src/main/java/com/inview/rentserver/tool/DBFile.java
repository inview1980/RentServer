package com.inview.rentserver.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import person.inview.error.PasswordException;
import person.inview.tools.EncryptUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

@Slf4j
public class DBFile {
    private static String PATH = "serverDB/";

    public static boolean save(@NonNull List<?> lst, @NonNull String pwd) {
        if (lst.size() == 0) return true;
        try {
            String str = JSONArray.toJSONString(lst);
            String clazzName = lst.get(0).getClass().getSimpleName();
            byte[] bs = compressAndEncrypt(str, pwd);
            OutputStream os = new FileOutputStream(getFile(PATH, clazzName));
            os.write(bs);
            os.close();
            log.info("写入数据库[{}]成功！",clazzName);
            return true;
        } catch (IOException | PasswordException e) {
            log.error("保存数据时错误！", e);
            return false;
        }
    }

    public static <T extends Object> List<T> read(@NonNull Class<T> clazz, @NonNull String pwd) {
        try {
            InputStream is = new FileInputStream(getFile(PATH, clazz.getSimpleName()));
            byte[] bytes = new byte[is.available()];
            if(is.read(bytes)!=bytes.length){
                log.error("读取数据库文件出错！");
                return new ArrayList<>();
            }
            byte[] decode = EncryptUtil.desDecode(bytes, pwd);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (OutputStream outputStream = new InflaterOutputStream(os)) {
                outputStream.write(decode);
            }
            String str = new String(os.toByteArray(), StandardCharsets.UTF_8);
            List<T> ts = JSONArray.parseArray(str, clazz);
            log.info("读取数据库[{}]成功！",clazz.getSimpleName());
            return ts;
        } catch (PasswordException | IOException e) {
            log.error("解压或解密数据库内容出错！", e);
            return new ArrayList<>();
        }
    }

    private static byte[] compressAndEncrypt(String str, String pwd) throws IOException, PasswordException {
        //压缩
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out)) {
            deflaterOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        }
        //加密
        byte[] data = out.toByteArray();
        return EncryptUtil.desEncode(data, pwd);
    }

    private static File getFile(String dir, String filename) {
        File path = new File(dir);
        if (!path.exists()) path.mkdirs();

//        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-M-d(HH_mm)")) + ".xls";
        return new File(dir + filename);
    }
}

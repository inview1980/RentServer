package com.inview.rentserver.controller;

import com.inview.rentserver.config.TimedTask;
import com.inview.rentserver.event.ModifyFactory;
import person.inview.receiver.Receiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UpdateController {
    private final ModifyFactory modifyFactory;

//    @PostMapping("/updateOrAdd")
//    Result updateOrAdd(@RequestBody JSONObject jsonObject) {
//        Receiver receiver = JSON.toJavaObject(jsonObject, Receiver.class);
//        receiveSever.register(receiver);
//
//        return Result.Ok();
//    }

    /**
     * 此接口为修改数据的唯一接口，收到指令后，将根据{@link Receiver}类的dataCode字段，通知相应的接收器处理消息
     */
    @PostMapping("/updateOrAdd")
    Result updateOrAdd(@RequestBody Receiver receiver) {
        if (modifyFactory.notice(receiver)) {
            return Result.Ok();
        } else {
            return Result.Error(WebResultEnum.Error);
        }
    }

    @GetMapping("/DBBackup")
    Result DBBackup() throws IOException, InvocationTargetException, IllegalAccessException {
        TimedTask.backupDB();
        return Result.Ok();
    }
}

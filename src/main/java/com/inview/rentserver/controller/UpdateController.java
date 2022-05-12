package com.inview.rentserver.controller;

import com.inview.rentserver.event.ModifyFactory;
import person.inview.receiver.Receiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import person.inview.receiver.Result;
import person.inview.receiver.WebResultEnum;

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

    @PostMapping("/updateOrAdd")
    Result updateOrAdd(@RequestBody Receiver receiver) {
        if(modifyFactory.notice(receiver)){
            return Result.Ok();
        }else{
            return Result.Error(WebResultEnum.Error);
        }
    }
}

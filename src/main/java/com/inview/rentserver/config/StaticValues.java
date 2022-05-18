package com.inview.rentserver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public abstract class StaticValues {
    /**
     * token的有效期为7天
     * token存在的秒数，60 * 60 * 24 * 7=7天
     */
    public  static int TokenTimes = 604800;

    /**
     * VerificationCodeTimes的有效期为60秒
     */
    public  static int VerificationCodeTimes = 60;

    public  static int PasswordLength = 16;

    public  static int TokenLength = 16;

    public  static int VerificationCodeLength = 4;
}

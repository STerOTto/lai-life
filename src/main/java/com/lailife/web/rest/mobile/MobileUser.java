package com.lailife.web.rest.mobile;

import com.lailife.service.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Size;

/**
 * Created by SterOtto on 2017/4/26.
 */
public class MobileUser extends UserDTO {
    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isEmpty(){
        if(StringUtils.isEmpty(this.getLogin()) && StringUtils.isEmpty(this.getPhone())){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

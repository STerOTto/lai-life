package com.lailife.repository.impl;

import com.lailife.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by SterOtto on 2017/4/25.
 */
public class UserRepositoryImpl {
    public Page<User> findAllNearby(Pageable pageable, Long selfId, Double longitudeStart, Double longitudeEnd, Double latitudeStart, Double latitudeEnd) {
        return null;
    }
}

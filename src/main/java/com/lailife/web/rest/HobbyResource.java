package com.lailife.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lailife.domain.Hobby;
import com.lailife.domain.User;
import com.lailife.repository.HobbyRepository;
import com.lailife.repository.UserRepository;
import com.lailife.security.SecurityUtils;
import com.lailife.service.UserService;
import com.lailife.web.rest.util.Message;
import com.lailife.web.rest.util.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by SterOtto on 2017/5/29.
 */
@RestController
@RequestMapping("/api")
public class HobbyResource {
    private Logger logger = LoggerFactory.getLogger(HobbyResource.class);

    @Autowired
    private HobbyRepository hobbyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * get all hobbies
     *
     * @return
     */
    @GetMapping("/hobbies")
    @Timed
    public ResponseEntity<List<Hobby>> getHobby() {
        return new ResponseEntity<>(hobbyRepository.findAll(), HttpStatus.OK);

    }

    /**
     * get user bobbies
     *
     * @return
     */
    @GetMapping("/user/hobbies")
    @Timed
    public ResponseEntity<Set<Hobby>> getUserHobby() {
        return userService.getUserWithHobbies().map(user -> {
            Message<Set<Hobby>> message = Message.fromResponseStatus(ResponseStatus.SUCCESS, user.getHobbies());
            logger.info("get hobby for user : {}", user.getId());
            return new ResponseEntity(message, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity(Message.fromResponseStatus(ResponseStatus.FAILED), HttpStatus.OK));
    }

    @PostMapping("/user/hobbies")
    @Timed
    public ResponseEntity<Message> setUserHobby(@Valid @RequestBody Set<Long> hobbyIds) {
        Set<Hobby> hobbies = new HashSet<>();
        for (Long id :
            hobbyIds) {
            hobbyRepository.findOneByIdAndLevel(id, Hobby.LEVEL_TWO).ifPresent(hobby -> hobbies.add(hobby));
        }
        return userRepository.findOneById(SecurityUtils.getCurrentUserInfo().getId())
            .map(user -> {
                user.setHobbies(hobbies);
                userService.updateUser(user);
                return new ResponseEntity(Message.fromResponseStatus(ResponseStatus.SUCCESS), HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity(Message.fromResponseStatus(ResponseStatus.FAILED), HttpStatus.OK)
            );
    }

}

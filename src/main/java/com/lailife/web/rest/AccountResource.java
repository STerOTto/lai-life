package com.lailife.web.rest;

import com.codahale.metrics.annotation.Timed;

import com.lailife.config.Constants;
import com.lailife.domain.PersistentToken;
import com.lailife.domain.User;
import com.lailife.repository.PersistentTokenRepository;
import com.lailife.repository.UserRepository;
import com.lailife.security.SecurityUtils;
import com.lailife.security.UserInfo;
import com.lailife.service.MailService;
import com.lailife.service.SmsService;
import com.lailife.service.UserService;
import com.lailife.service.dto.UserDTO;
import com.lailife.service.util.Netease;
import com.lailife.web.rest.mobile.MobileUser;
import com.lailife.web.rest.util.*;
import com.lailife.web.rest.util.ResponseStatus;
import com.lailife.web.rest.vm.KeyAndPasswordVM;
import com.lailife.web.rest.vm.ManagedUserVM;

import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.cache.CacheManager;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SmsService smsService;

    public AccountResource(UserRepository userRepository, UserService userService,
                           MailService mailService, PersistentTokenRepository persistentTokenRepository) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    /**
     * GET /register/{phone} : checkPhone
     *
     * @param phone
     * @return the ResponseEntity with status 200 (OK), or status 409 (Phone already in use)
     */
    @GetMapping(path = "/register/{phone:" + Constants.PHONE_REGEX + "}")
    @Timed
    public ResponseEntity<Message> checkPhone(@PathVariable("phone") String phone) {
        log.info("check phone is {}", phone);
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
        return userRepository.findOneByPhoneNotNullAndPhone(phone)
            .map(user -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.IN_USE), textPlainHeaders, HttpStatus.OK))
            .orElseGet(() -> smsService.sendCode(phone)
                .map(netease -> {
                    if (netease.getCode() == Netease.CODE_OK){
                        log.info(netease.getMsg());
                        return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.SUCCESS), HttpStatus.OK);
                    }
                    else return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.ERROR), HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.FAILED), HttpStatus.OK))
            );
    }

    /**
     * GET /register/{phone}/{code}
     *
     * @param phone
     * @param code
     * @return the ResponseEntity with status 200 (check code success), or status 409 (the code is wrong), or status 500 (failed to check code)
     */
    @GetMapping(path = "/register/{phone:" + Constants.PHONE_REGEX + "}/{code:^\\d{4}}")
    @Timed
    public ResponseEntity<Message> checkPhoneCode(@PathVariable String phone, @PathVariable String code) {
        log.debug("check phone is {}, code is {}", phone, code);
        return smsService.checkCode(phone, code).map(netease -> {
            if (netease.getCode() == Netease.CODE_OK)
                return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.SUCCESS), HttpStatus.OK);
            else
                return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.WRONG), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.FAILED), HttpStatus.OK));
    }

    /**
     * GET /check/{phone}
     * @param phone
     * @return 200 (phone is register, now login) or 400 (phone is not register, please register)
     */
    @GetMapping(path = "/check/{phone:" + Constants.PHONE_REGEX + "}")
    public ResponseEntity<Message> checkPhoneLogin(@PathVariable String phone){
        return userRepository.findOneByPhoneNotNullAndPhone(phone)
            .map(user -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.EXIST), HttpStatus.OK))
            .orElseGet(()-> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.NOT_EXIST), HttpStatus.OK));
    }

    /**
     * @param mobileUser
     * @return the ResponseEntity with status 201 (create success), or status 200 (create user success, but failed to upload image)
     *          or status 400 (some errors in save user)
     */
    @PostMapping(path = "/register/mobile",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity<Message> mobileRegisterAccount(@Valid @RequestBody MobileUser mobileUser) {
        log.info("REST Request: {}", mobileUser);
        if(mobileUser == null || mobileUser.isEmpty()) return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.NULL), HttpStatus.OK);
        String login = mobileUser.getLogin();
        if (StringUtils.isNotEmpty(login)) login = login.toLowerCase();
        return userRepository.findOneByLoginNotNullAndLogin(login)
            .map(user -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.IN_USE, "login"), HttpStatus.OK))
            .orElseGet(() -> userRepository.findOneByPhoneNotNullAndPhone(mobileUser.getPhone())
                .map(user -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.IN_USE, "phone"), HttpStatus.OK))
                .orElseGet(() -> userRepository.findOneByWechatNotNullAndWechat(mobileUser.getWechat())
                    .map(user -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.IN_USE, "wechat"), HttpStatus.OK))
                    .orElseGet(() -> userRepository.findOneByEmailNotNullAndEmail(mobileUser.getEmail())
                        .map(user -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.IN_USE, "email"), HttpStatus.OK))
                        .orElseGet(() -> {
                            User user = userService.createUser(mobileUser, mobileUser.getPassword());
                            if (user != null) {
                                return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.SUCCESS), HttpStatus.OK);
                            } else
                                return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.ERROR), HttpStatus.OK);
                        }))));
    }

    /**
     * get icon path according to user id
     * @param id
     * @return
     */
    @GetMapping(path = "/get_icon/{id:^[0-9]*[1-9][0-9]*$}")
    public ResponseEntity<Message> getIcon(@PathVariable Long id){
        return userRepository.findOneById(id).map(user -> new ResponseEntity(Message.fromResponseStatus(ResponseStatus.SUCCESS, user.getImageUrl()), HttpStatus.OK))
            .orElseGet( () -> new ResponseEntity(Message.fromResponseStatus(ResponseStatus.FAILED), HttpStatus.OK)
        );
    }

    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM the managed user View Model
     * @return the ResponseEntity with status 201 (Created) if the user is registered or 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping(path = "/register",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Timed
    public ResponseEntity registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {

        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);

        return userRepository.findOneByLoginNotNullAndLogin(managedUserVM.getLogin().toLowerCase())
            .map(user -> new ResponseEntity<>("login already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmailNotNullAndEmail(managedUserVM.getEmail())
                .map(user -> new ResponseEntity<>("email address already in use", textPlainHeaders, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    User user = userService
                        .createUser(managedUserVM.getLogin(), managedUserVM.getPassword(),
                            managedUserVM.getFirstName(), managedUserVM.getLastName(),
                            managedUserVM.getEmail().toLowerCase(), managedUserVM.getImageUrl(), managedUserVM.getLangKey());

                    mailService.sendActivationEmail(user);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
            );
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @return the ResponseEntity with status 200 (OK) and the activated user in body, or status 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userService.activateRegistration(key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
     */
    @PostMapping("/account")
    @Timed
    public ResponseEntity saveAccount(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findOneByEmailNotNullAndEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userDTO.getLogin()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
        }
        return userRepository
            .findOneByLoginNotNullAndLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                    userDTO.getLangKey(), userDTO.getImageUrl());
                return new ResponseEntity(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @PostMapping(path = "/account/change_password",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/account/mobile/change_password")
    @Timed
    public ResponseEntity<Message> changePassword(String oldPassword, String newPassword) {
        if (!checkPasswordLength(newPassword)) {
            return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.ERROR, "Incorrect password"), HttpStatus.OK);
        }
        if (userService.changePassword(oldPassword, newPassword)) {
            return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.SUCCESS, "change password success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.WRONG, "please check your old password is right!"), HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/account/userInfo")
    @Timed
    public ResponseEntity<UserInfo> getCurrentUserInfo() {
        return new ResponseEntity<UserInfo>(SecurityUtils.getCurrentUserInfo(), HttpStatus.OK);
    }

    /**
     * GET  /account/sessions : get the current open sessions.
     *
     * @return the ResponseEntity with status 200 (OK) and the current open sessions in body,
     * or status 500 (Internal Server Error) if the current open sessions couldn't be retrieved
     */
    @GetMapping("/account/sessions")
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return userRepository.findOneByLoginNotNullAndLogin(SecurityUtils.getCurrentUserLogin())
            .map(user -> new ResponseEntity<>(
                persistentTokenRepository.findByUser(user),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * DELETE  /account/sessions?series={series} : invalidate an existing session.
     * <p>
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     * still be able to use that session, until you quit your browser: it does not work in real time (there is
     * no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     * your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     * anymore.
     * There is an API to invalidate the current session, but there is no API to check which session uses which
     * cookie.
     *
     * @param series the series of an existing session
     * @throws UnsupportedEncodingException if the series couldnt be URL decoded
     */
    @DeleteMapping("/account/sessions/{series}")
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        userRepository.findOneByLoginNotNullAndLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u ->
            persistentTokenRepository.findByUser(u).stream()
                .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
                .findAny().ifPresent(t -> persistentTokenRepository.delete(decodedSeries)));
    }

    /**
     * POST   /account/reset_password/init : Send an email to reset the password of the user
     *
     * @param mail the mail of the user
     * @return the ResponseEntity with status 200 (OK) if the email was sent, or status 400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset_password/init",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity requestPasswordReset(@RequestBody String mail) {
        return userService.requestPasswordReset(mail)
            .map(user -> {
                mailService.sendPasswordResetMail(user);
                return new ResponseEntity<>("email was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("email address not registered", HttpStatus.BAD_REQUEST));
    }

    /**
     * POST   /account/reset_password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @return the ResponseEntity with status 200 (OK) if the password has been reset,
     * or status 400 (Bad Request) or 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset_password/finish",
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    @PostMapping(path = "/account/nearby")
    @Timed
    public ResponseEntity<List<UserDTO>> getUserNearby(@ApiParam Pageable pageable,
                                                       Integer sex, Double longitudeStart, Double longitudeEnd,
                                                       Double latitudeStart, Double latitudeEnd, Long hobbyId) {
        Map params = new HashMap();
        params.put("currentId", SecurityUtils.getCurrentUserInfo().getId());
        if (sex != null) params.put("sex", sex);
        if (longitudeStart != null) params.put("longitudeStart", longitudeStart);
        if (longitudeEnd != null) params.put("longitudeEnd", longitudeEnd);
        if (latitudeStart != null) params.put("latitudeStart", latitudeStart);
        if (latitudeEnd != null) params.put("latitudeEnd", latitudeEnd);
        if (hobbyId != null) params.put("hobbyId", hobbyId);
        final Page<UserDTO> userDTOPage = userService.getNearbyUsers(params, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(userDTOPage, "/api/account/nearby");
        return new ResponseEntity<>(userDTOPage.getContent(), headers, HttpStatus.OK);
    }
}

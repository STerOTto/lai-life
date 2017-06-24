package com.lailife.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lailife.domain.Hobby;
import com.lailife.domain.Needs;
import com.lailife.domain.NeedsFiles;
import com.lailife.domain.User;
import com.lailife.repository.HobbyRepository;
import com.lailife.repository.NeedsFilesRepository;
import com.lailife.repository.NeedsRepository;
import com.lailife.repository.UserRepository;
import com.lailife.security.SecurityUtils;
import com.lailife.service.NeedsService;
import com.lailife.service.UserService;
import com.lailife.web.rest.util.Message;
import com.lailife.web.rest.util.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by SterOtto on 2017/4/24.
 * REST controller for managing the needs.
 */
@RestController
@RequestMapping("/api")
public class NeedsResource {
    private final Logger log = LoggerFactory.getLogger(NeedsResource.class);

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private NeedsRepository needsRepository;

    @Autowired
    private NeedsFilesRepository needsFilesRepository;

    @Autowired
    private NeedsService needsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    /**
     * @param hobbyId
     * @param content
     * @param images
     * @return
     */
    @PostMapping(path = "/needs/create")
    @Timed
    public ResponseEntity<Message> create(Long hobbyId, Long needsId,
                                          String content, String[] images) {
        log.info("Rest Request : {}, \n {}", hobbyId, content);
        return hobbyRepository.findOneByIdAndLevel(hobbyId, Hobby.LEVEL_TWO).map(
            hobby -> {
                // check if edit or new
                Needs needs = null;
                if (needsId != null)
                    needs = needsRepository.findOne(needsId);
                if (needs == null || needs.getUserId() != SecurityUtils.getCurrentUserInfo().getId())
                    needs = new Needs();
                needs.setHobby(hobby);
                needs.setUserId(SecurityUtils.getCurrentUserInfo().getId());
                needs.setContent(content);
                needs.setState(Needs.STATE_ZERO);
                needs.setCreateTime(ZonedDateTime.now());
                needs = needsService.save(needs);
                User user = userRepository.findOne(SecurityUtils.getCurrentUserInfo().getId());
                //update user's latest needs
                user.setLatestNeeds(needs);
                userService.updateUser(user);
                if (images != null && images.length>0 && needs.getId() != null) {
                    for (String image : images) {
                        NeedsFiles nf = new NeedsFiles();
                        nf.setNeeds(needs);
                        nf.setPath(image);
                        nf.setCreateTime(ZonedDateTime.now());
                        nf.setType("image");
                        needsFilesRepository.save(nf);
                    }
                }
                return new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.SUCCESS), HttpStatus.OK);
            }
        ).orElseGet(() -> new ResponseEntity<>(Message.fromResponseStatus(ResponseStatus.FAILED), HttpStatus.OK)
        );
    }

}

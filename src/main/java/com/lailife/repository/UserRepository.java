package com.lailife.repository;

import com.lailife.domain.User;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    Optional<User> findOneByActivationKey(String activationKey);

    Optional<User> findOneById(Long id);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailNotNullAndEmail(String email);

    Optional<User> findOneByLoginNotNullAndLogin(String login);

    Optional<User> findOneByPhoneNotNullAndPhone(String phone);

    Optional<User> findOneByWechatNotNullAndWechat(String wechat);

    @Query("select u from User u where u.login = ?1 or u.phone = ?1")
    Optional<User> findOneByLoginOrPhone(String loginOrPhone);

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Query("select u from User u where ( u.login is not null and u.login = ?1 ) or (u.phone is not null and u.phone = ?1 ) ")
    Optional<User> findOneWithAuthoritiesByLoginOrPhone(String loginOrPhone);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

    @EntityGraph(attributePaths = "hobbies")
    Optional<User> findOneWithHobbiesById(Long id);
}

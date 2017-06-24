package com.lailife.service.dto;

import com.lailife.config.Constants;

import com.lailife.domain.Authority;
import com.lailife.domain.Hobby;
import com.lailife.domain.Needs;
import com.lailife.domain.User;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(min = 11, max = 16)
    private String phone;

    @Size(max = 64)
    private String wechat;

    @Size(max = 256)
    private String imageUrl;

    @Size(max = 64)
    private String nickname;

    private Integer sex = 0;

    private ZonedDateTime birthday;

    @Size(max = 16)
    private String zodiac;

    @Size(max = 128)
    private String signature;

    private Long attraction;

    private Double longitude;

    private Double latitude;

    @Size(min = 18, max = 32)
    private String identityCard;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 16)
    private String province;

    @Size(max=16)
    private String city;

    @Size(max = 16)
    private String county;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = true;

    @Size(min = 2, max = 5)
    private String langKey;

    private String createdBy;

    private ZonedDateTime createdDate;

    private String lastModifiedBy;

    private ZonedDateTime lastModifiedDate;

    private ZonedDateTime lastLoginTime = null;

    private Integer isOnline = 0;

    private Integer shareCount;

    private Set<Hobby> hobbies;

    private Needs latestNeeds;

    private Set<String> authorities;

    public UserDTO() {
        // Empty constructor needed for MapStruct.
    }

    /*public UserDTO(User user) {
        this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getImageUrl(), user.getLangKey(),
            user.getCreatedBy(), user.getCreatedDate(), user.getLastModifiedBy(), user.getLastModifiedDate(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()));
    }*/
    public UserDTO(User user){
        this(user.getId(), user.getLogin(), user.getPhone(), user.getWechat(), user.getImageUrl(),
            user.getNickname(), user.getSex(), user.getBirthday(), user.getZodiac(), user.getSignature(),
            user.getAttraction(), user.getLongitude(), user.getLatitude(), user.getIdentityCard(),
            user.getFirstName(), user.getLastName(), user.getProvince(), user.getCity(), user.getCounty(),
            user.getEmail(), user.getActivated(), user.getLangKey(), user.getCreatedBy(), user.getCreatedDate(),
            user.getLastModifiedBy(), user.getLastModifiedDate(), user.getLastLoginTime(), user.getShareCount(),
            user.getIsOnline(), user.getHobbies(), user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
    }

    public static UserDTO buildObjectToUserDTO(Object o){
        User user = (User) o;
        return new UserDTO(user);
    }

    public UserDTO(Long id, String login, String firstName, String lastName,
        String email, boolean activated, String imageUrl, String langKey,
        String createdBy, ZonedDateTime createdDate, String lastModifiedBy, ZonedDateTime lastModifiedDate,
        Set<String> authorities) {

        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.authorities = authorities;
    }

    public UserDTO(Long id, String login, String phone, String wechat, String imageUrl, String nickname, Integer sex,
                   ZonedDateTime birthday, String zodiac, String signature, Long attraction, Double longitude,
                   Double latitude, String identityCard, String firstName, String lastName, String province, String city,
                   String county, String email, boolean activated, String langKey, String createdBy, ZonedDateTime createdDate,
                   String lastModifiedBy, ZonedDateTime lastModifiedDate, ZonedDateTime lastLoginTime, Integer shareCount,
                   Integer isOnline, Set<Hobby> hobbies, Set<String> authorities) {
        this.id = id;
        this.login = login;
        this.phone = phone;
        this.wechat = wechat;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
        this.zodiac = zodiac;
        this.signature = signature;
        this.attraction = attraction;
        this.longitude = longitude;
        this.latitude = latitude;
        this.identityCard = identityCard;
        this.firstName = firstName;
        this.lastName = lastName;
        this.province = province;
        this.city = city;
        this.county = county;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.lastLoginTime = lastLoginTime;
        this.shareCount = shareCount;
        this.isOnline = isOnline;
        this.hobbies = hobbies;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getAttraction() {
        return attraction;
    }

    public void setAttraction(Long attraction) {
        this.attraction = attraction;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(ZonedDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Set<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Needs getLatestNeeds() {
        return latestNeeds;
    }

    public void setLatestNeeds(Needs latestNeeds) {
        this.latestNeeds = latestNeeds;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", phone='" + phone + '\'' +
            ", wechat='" + wechat + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", nickname='" + nickname + '\'' +
            ", sex=" + sex +
            ", birthday=" + birthday +
            ", zodiac='" + zodiac + '\'' +
            ", signature='" + signature + '\'' +
            ", attraction=" + attraction +
            ", longitude=" + longitude +
            ", latitude=" + latitude +
            ", identityCard='" + identityCard + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", province='" + province + '\'' +
            ", city='" + city + '\'' +
            ", county='" + county + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", lastLoginTime=" + lastLoginTime +
            ", isOnline=" + isOnline +
            ", shareCount=" + shareCount +
            '}';
    }
}

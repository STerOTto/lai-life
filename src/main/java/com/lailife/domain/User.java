package com.lailife.domain;

import com.lailife.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.time.ZonedDateTime;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "user")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true)
    private String login;

    @Pattern(regexp = Constants.PHONE_REGEX)
    @Size(min = 11, max = 16)
    @Column(length = 16, unique = true)
    private String phone;

    @Size(max = 64)
    @Column(length = 64, unique = true)
    private String wechat;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash",length = 60)
    private String password;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 64)
    @Column(length = 64)
    private String nickname;

    private Integer sex = 0;//0 is woman, 1 is man

    private ZonedDateTime birthday;

    @Size(max = 16)
    @Column(length = 16)
    private String zodiac;

    @Size(max = 128)
    @Column(length = 128)
    private String signature;

    private Long attraction;

    private Double longitude;

    private Double latitude;

    @Size(min = 18, max = 32)
    @Column(name = "identity_card", length = 32, unique = true)
    private String identityCard;

    @Size(max = 16)
    @Column(length = 16)
    private String province;

    @Size(max=16)
    @Column(length = 16)
    private String city;

    @Size(max = 16)
    @Column(length = 16)
    private String county;


    @Column(name = "last_login_time")
    private ZonedDateTime lastLoginTime = null;

    @Column(name = "is_online")
    private Integer isOnline = 0;

    @Column(name = "share_count")
    private Integer shareCount;


    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = true;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "reset_date")
    private ZonedDateTime resetDate = null;

    @OneToOne
    @JoinColumn(name = "latest_needs")
    private Needs latestNeeds;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_hobby",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "hobby_id", referencedColumnName = "id")}
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Hobby> hobbies = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersistentToken> persistentTokens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    //Lowercase the login before saving it in database
    public void setLogin(String login) {
        if(login!=null)
            this.login = login.toLowerCase(Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public ZonedDateTime getResetDate() {
       return resetDate;
    }

    public void setResetDate(ZonedDateTime resetDate) {
       this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<PersistentToken> getPersistentTokens() {
        return persistentTokens;
    }

    public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
        this.persistentTokens = persistentTokens;
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

    public boolean isActivated() {
        return activated;
    }

    public Set<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public Needs getLatestNeeds() {
        return latestNeeds;
    }

    public void setLatestNeeds(Needs latestNeeds) {
        this.latestNeeds = latestNeeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        return wechat != null ? wechat.equals(user.wechat) : user.wechat == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (wechat != null ? wechat.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", phone='" + phone + '\'' +
            ", wechat='" + wechat + '\'' +
            ", password='" + password + '\'' +
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
            ", province='" + province + '\'' +
            ", city='" + city + '\'' +
            ", county='" + county + '\'' +
            ", lastLoginTime=" + lastLoginTime +
            ", isOnline=" + isOnline +
            ", shareCount=" + shareCount +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            ", resetKey='" + resetKey + '\'' +
            ", resetDate=" + resetDate +
            '}';
    }
}

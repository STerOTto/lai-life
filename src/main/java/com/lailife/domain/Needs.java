package com.lailife.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Created by SterOtto on 2017/4/24.
 */
@Entity
@Table(name = "needs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "needs")
public class Needs implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int STATE_ZERO = 0;
    public static final int STATE_NEGATIVE_ONE = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "latestNeeds")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hobby_id", nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Hobby hobby;

    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "needs")
    private Set<NeedsFiles> needsFiles;

    private int state;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hobby getHobby() {
        return hobby;
    }

    public void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

    public Set<NeedsFiles> getNeedsFiles() {
        return needsFiles;
    }

    public void setNeedsFiles(Set<NeedsFiles> needsFiles) {
        this.needsFiles = needsFiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Needs{" +
            "id=" + id +
            ", userId=" + userId +
            ", user=" + user +
            ", hobby=" + hobby +
            ", content='" + content + '\'' +
            ", state=" + state +
            ", createTime=" + createTime +
            '}';
    }
}

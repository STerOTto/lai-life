package com.lailife.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by SterOtto on 2017/4/24.
 */
@Entity
@Table(name = "needs_files")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NeedsFiles implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    @Size(max = 64)
    @Column(length = 64)
    private String type;

    private long size;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "needs_id", nullable = false)
    private Needs needs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public Needs getNeeds() {
        return needs;
    }

    public void setNeeds(Needs needs) {
        this.needs = needs;
    }

    @Override
    public String toString() {
        return "NeedsFiles{" +
            "id=" + id +
            ", path='" + path + '\'' +
            ", type='" + type + '\'' +
            ", size=" + size +
            ", createTime=" + createTime +
            ", need=" + needs.getId() +
            '}';
    }
}

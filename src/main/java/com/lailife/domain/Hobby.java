package com.lailife.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by SterOtto on 2017/4/18.
 */
@Entity
@Table(name = "hobby")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "hobby")
public class Hobby implements Serializable{
    private static final long serialVersionUID = 1L;
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 16)
    @Column(length = 16, nullable = false)
    private String name;

    @Size(max = 8)
    @Column(length = 8, nullable = false)
    private Integer level;

    private Long parent;

    private int hot = 0;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hobby")
    private Set<Needs> needses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public Set<Needs> getNeedses() {
        return needses;
    }

    public void setNeedses(Set<Needs> needses) {
        this.needses = needses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hobby hobby = (Hobby) o;

        return id != null ? id.equals(hobby.id) : hobby.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Hobby{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", level=" + level +
            ", parent=" + parent +
            ", hot=" + hot +
            '}';
    }
}

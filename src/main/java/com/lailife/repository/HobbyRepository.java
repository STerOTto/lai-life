package com.lailife.repository;

import com.lailife.domain.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by SterOtto on 2017/4/25.
 */
public interface HobbyRepository extends JpaRepository<Hobby, Long> {

    Optional<Hobby> findOneByIdAndLevel(Long id, Integer level);

    Optional<Hobby> findOneById(Long id);
}

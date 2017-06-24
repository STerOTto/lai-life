package com.lailife.repository;

import com.lailife.domain.Needs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Created by SterOtto on 2017/4/24.
 */
public interface NeedsRepository extends JpaRepository<Needs, Long>{
    Optional<Needs> findOneByIdAndUserIdAndState(Long id, Long userId, int state);
}

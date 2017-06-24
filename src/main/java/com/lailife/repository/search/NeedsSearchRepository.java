package com.lailife.repository.search;

import com.lailife.domain.Needs;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by SterOtto on 2017/6/12.
 */
public interface NeedsSearchRepository extends ElasticsearchCrudRepository<Needs, Long> {
}

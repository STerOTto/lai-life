package com.lailife.service;

import com.lailife.domain.Needs;
import com.lailife.repository.NeedsRepository;
import com.lailife.repository.search.NeedsSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by SterOtto on 2017/4/24.
 */
@Service
public class NeedsService {

    @Autowired
    private NeedsRepository needsRepository;
    @Autowired
    private NeedsSearchRepository needsSearchRepository;

    public Needs save(Needs needs){
        Needs need = needsRepository.save(needs);
        needsSearchRepository.save(need);
        return need;
    }
}

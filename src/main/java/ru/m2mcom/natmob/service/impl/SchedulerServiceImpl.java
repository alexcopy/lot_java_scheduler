package ru.m2mcom.natmob.service.impl;

import ru.m2mcom.natmob.service.SchedulerService;
import ru.m2mcom.natmob.domain.Scheduler;
import ru.m2mcom.natmob.repository.SchedulerRepository;
import ru.m2mcom.natmob.repository.search.SchedulerSearchRepository;
import ru.m2mcom.natmob.service.dto.SchedulerDTO;
import ru.m2mcom.natmob.service.mapper.SchedulerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Scheduler.
 */
@Service
@Transactional
public class SchedulerServiceImpl implements SchedulerService{

    private final Logger log = LoggerFactory.getLogger(SchedulerServiceImpl.class);
    
    private final SchedulerRepository schedulerRepository;

    private final SchedulerMapper schedulerMapper;

    private final SchedulerSearchRepository schedulerSearchRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository, SchedulerMapper schedulerMapper, SchedulerSearchRepository schedulerSearchRepository) {
        this.schedulerRepository = schedulerRepository;
        this.schedulerMapper = schedulerMapper;
        this.schedulerSearchRepository = schedulerSearchRepository;
    }

    /**
     * Save a scheduler.
     *
     * @param schedulerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SchedulerDTO save(SchedulerDTO schedulerDTO) {
        log.debug("Request to save Scheduler : {}", schedulerDTO);
        Scheduler scheduler = schedulerMapper.schedulerDTOToScheduler(schedulerDTO);
        scheduler = schedulerRepository.save(scheduler);
        SchedulerDTO result = schedulerMapper.schedulerToSchedulerDTO(scheduler);
        schedulerSearchRepository.save(scheduler);
        return result;
    }

    /**
     *  Get all the schedulers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schedulers");
        Page<Scheduler> result = schedulerRepository.findAll(pageable);
        return result.map(scheduler -> schedulerMapper.schedulerToSchedulerDTO(scheduler));
    }

    /**
     *  Get one scheduler by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SchedulerDTO findOne(Long id) {
        log.debug("Request to get Scheduler : {}", id);
        Scheduler scheduler = schedulerRepository.findOne(id);
        SchedulerDTO schedulerDTO = schedulerMapper.schedulerToSchedulerDTO(scheduler);
        return schedulerDTO;
    }

    /**
     *  Delete the  scheduler by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scheduler : {}", id);
        schedulerRepository.delete(id);
        schedulerSearchRepository.delete(id);
    }

    /**
     * Search for the scheduler corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SchedulerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Schedulers for query {}", query);
        Page<Scheduler> result = schedulerSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(scheduler -> schedulerMapper.schedulerToSchedulerDTO(scheduler));
    }
}

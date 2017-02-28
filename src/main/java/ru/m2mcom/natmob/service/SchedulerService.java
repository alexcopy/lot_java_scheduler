package ru.m2mcom.natmob.service;

import ru.m2mcom.natmob.service.dto.SchedulerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Scheduler.
 */
public interface SchedulerService {

    /**
     * Save a scheduler.
     *
     * @param schedulerDTO the entity to save
     * @return the persisted entity
     */
    SchedulerDTO save(SchedulerDTO schedulerDTO);

    /**
     *  Get all the schedulers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SchedulerDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" scheduler.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SchedulerDTO findOne(Long id);

    /**
     *  Delete the "id" scheduler.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the scheduler corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SchedulerDTO> search(String query, Pageable pageable);
}

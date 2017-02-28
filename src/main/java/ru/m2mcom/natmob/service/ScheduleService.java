package ru.m2mcom.natmob.service;

import ru.m2mcom.natmob.service.dto.ScheduleDTO;
import java.util.List;

/**
 * Service Interface for managing Schedule.
 */
public interface ScheduleService {

    /**
     * Save a schedule.
     *
     * @param scheduleDTO the entity to save
     * @return the persisted entity
     */
    ScheduleDTO save(ScheduleDTO scheduleDTO);

    /**
     *  Get all the schedules.
     *  
     *  @return the list of entities
     */
    List<ScheduleDTO> findAll();

    /**
     *  Get the "id" schedule.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ScheduleDTO findOne(String id);

    /**
     *  Delete the "id" schedule.
     *
     *  @param id the id of the entity
     */
    void delete(String id);
}

package ru.m2mcom.natmob.service.mapper;

import ru.m2mcom.natmob.domain.*;
import ru.m2mcom.natmob.service.dto.SchedulerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Scheduler and its DTO SchedulerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SchedulerMapper {

    SchedulerDTO schedulerToSchedulerDTO(Scheduler scheduler);

    List<SchedulerDTO> schedulersToSchedulerDTOs(List<Scheduler> schedulers);

    Scheduler schedulerDTOToScheduler(SchedulerDTO schedulerDTO);

    List<Scheduler> schedulerDTOsToSchedulers(List<SchedulerDTO> schedulerDTOs);
}

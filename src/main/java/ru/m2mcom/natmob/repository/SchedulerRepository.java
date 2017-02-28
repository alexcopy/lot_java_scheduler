package ru.m2mcom.natmob.repository;

import ru.m2mcom.natmob.domain.Scheduler;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Scheduler entity.
 */
@SuppressWarnings("unused")
public interface SchedulerRepository extends JpaRepository<Scheduler,Long> {

}

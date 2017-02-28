package ru.m2mcom.natmob.repository;

import ru.m2mcom.natmob.domain.Schedule;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Schedule entity.
 */
@SuppressWarnings("unused")
public interface ScheduleRepository extends MongoRepository<Schedule,String> {

}

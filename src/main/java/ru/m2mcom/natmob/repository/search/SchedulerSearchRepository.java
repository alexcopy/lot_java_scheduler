package ru.m2mcom.natmob.repository.search;

import ru.m2mcom.natmob.domain.Scheduler;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Scheduler entity.
 */
public interface SchedulerSearchRepository extends ElasticsearchRepository<Scheduler, Long> {
}

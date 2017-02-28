package ru.m2mcom.natmob.web.rest;

import com.codahale.metrics.annotation.Timed;
import ru.m2mcom.natmob.service.SchedulerService;
import ru.m2mcom.natmob.web.rest.util.HeaderUtil;
import ru.m2mcom.natmob.web.rest.util.PaginationUtil;
import ru.m2mcom.natmob.service.dto.SchedulerDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Scheduler.
 */
@RestController
@RequestMapping("/api")
public class SchedulerResource {

    private final Logger log = LoggerFactory.getLogger(SchedulerResource.class);

    private static final String ENTITY_NAME = "scheduler";
        
    private final SchedulerService schedulerService;

    public SchedulerResource(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    /**
     * POST  /schedulers : Create a new scheduler.
     *
     * @param schedulerDTO the schedulerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schedulerDTO, or with status 400 (Bad Request) if the scheduler has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedulers")
    @Timed
    public ResponseEntity<SchedulerDTO> createScheduler(@Valid @RequestBody SchedulerDTO schedulerDTO) throws URISyntaxException {
        log.debug("REST request to save Scheduler : {}", schedulerDTO);
        if (schedulerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new scheduler cannot already have an ID")).body(null);
        }
        SchedulerDTO result = schedulerService.save(schedulerDTO);
        return ResponseEntity.created(new URI("/api/schedulers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedulers : Updates an existing scheduler.
     *
     * @param schedulerDTO the schedulerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schedulerDTO,
     * or with status 400 (Bad Request) if the schedulerDTO is not valid,
     * or with status 500 (Internal Server Error) if the schedulerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedulers")
    @Timed
    public ResponseEntity<SchedulerDTO> updateScheduler(@Valid @RequestBody SchedulerDTO schedulerDTO) throws URISyntaxException {
        log.debug("REST request to update Scheduler : {}", schedulerDTO);
        if (schedulerDTO.getId() == null) {
            return createScheduler(schedulerDTO);
        }
        SchedulerDTO result = schedulerService.save(schedulerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, schedulerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedulers : get all the schedulers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedulers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedulers")
    @Timed
    public ResponseEntity<List<SchedulerDTO>> getAllSchedulers(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Schedulers");
        Page<SchedulerDTO> page = schedulerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedulers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /schedulers/:id : get the "id" scheduler.
     *
     * @param id the id of the schedulerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schedulerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/schedulers/{id}")
    @Timed
    public ResponseEntity<SchedulerDTO> getScheduler(@PathVariable Long id) {
        log.debug("REST request to get Scheduler : {}", id);
        SchedulerDTO schedulerDTO = schedulerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(schedulerDTO));
    }

    /**
     * DELETE  /schedulers/:id : delete the "id" scheduler.
     *
     * @param id the id of the schedulerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedulers/{id}")
    @Timed
    public ResponseEntity<Void> deleteScheduler(@PathVariable Long id) {
        log.debug("REST request to delete Scheduler : {}", id);
        schedulerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/schedulers?query=:query : search for the scheduler corresponding
     * to the query.
     *
     * @param query the query of the scheduler search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/schedulers")
    @Timed
    public ResponseEntity<List<SchedulerDTO>> searchSchedulers(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Schedulers for query {}", query);
        Page<SchedulerDTO> page = schedulerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/schedulers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

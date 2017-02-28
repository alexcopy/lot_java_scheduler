package ru.m2mcom.natmob.web.rest;

import ru.m2mcom.natmob.SchedulerApp;

import ru.m2mcom.natmob.domain.Scheduler;
import ru.m2mcom.natmob.repository.SchedulerRepository;
import ru.m2mcom.natmob.service.SchedulerService;
import ru.m2mcom.natmob.repository.search.SchedulerSearchRepository;
import ru.m2mcom.natmob.service.dto.SchedulerDTO;
import ru.m2mcom.natmob.service.mapper.SchedulerMapper;
import ru.m2mcom.natmob.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.m2mcom.natmob.domain.enumeration.WeekDay;
/**
 * Test class for the SchedulerResource REST controller.
 *
 * @see SchedulerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApp.class)
public class SchedulerResourceIntTest {

    private static final String DEFAULT_SCHEDULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RUN_EVERY = 1;
    private static final Integer UPDATED_RUN_EVERY = 2;

    private static final String DEFAULT_TIME_TO_RUN = "20:26";
    private static final String UPDATED_TIME_TO_RUN = "20:22";

    private static final WeekDay DEFAULT_WEEK_DAY = WeekDay.Sunday;
    private static final WeekDay UPDATED_WEEK_DAY = WeekDay.Monday;

    private static final Integer DEFAULT_LAST_RUN = 1;
    private static final Integer UPDATED_LAST_RUN = 2;

    private static final Integer DEFAULT_CREATED_AT = 1;
    private static final Integer UPDATED_CREATED_AT = 2;

    private static final Integer DEFAULT_UPDATED_AT = 1;
    private static final Integer UPDATED_UPDATED_AT = 2;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private SchedulerMapper schedulerMapper;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private SchedulerSearchRepository schedulerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSchedulerMockMvc;

    private Scheduler scheduler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SchedulerResource schedulerResource = new SchedulerResource(schedulerService);
        this.restSchedulerMockMvc = MockMvcBuilders.standaloneSetup(schedulerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scheduler createEntity(EntityManager em) {
        Scheduler scheduler = new Scheduler()
                .scheduleName(DEFAULT_SCHEDULE_NAME)
                .runEvery(DEFAULT_RUN_EVERY)
                .timeToRun(DEFAULT_TIME_TO_RUN)
                .weekDay(DEFAULT_WEEK_DAY)
                .lastRun(DEFAULT_LAST_RUN)
                .createdAt(DEFAULT_CREATED_AT)
                .updatedAt(DEFAULT_UPDATED_AT);
        return scheduler;
    }

    @Before
    public void initTest() {
        schedulerSearchRepository.deleteAll();
        scheduler = createEntity(em);
    }

    @Test
    @Transactional
    public void createScheduler() throws Exception {
        int databaseSizeBeforeCreate = schedulerRepository.findAll().size();

        // Create the Scheduler
        SchedulerDTO schedulerDTO = schedulerMapper.schedulerToSchedulerDTO(scheduler);

        restSchedulerMockMvc.perform(post("/api/schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDTO)))
            .andExpect(status().isCreated());

        // Validate the Scheduler in the database
        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeCreate + 1);
        Scheduler testScheduler = schedulerList.get(schedulerList.size() - 1);
        assertThat(testScheduler.getScheduleName()).isEqualTo(DEFAULT_SCHEDULE_NAME);
        assertThat(testScheduler.getRunEvery()).isEqualTo(DEFAULT_RUN_EVERY);
        assertThat(testScheduler.getTimeToRun()).isEqualTo(DEFAULT_TIME_TO_RUN);
        assertThat(testScheduler.getWeekDay()).isEqualTo(DEFAULT_WEEK_DAY);
        assertThat(testScheduler.getLastRun()).isEqualTo(DEFAULT_LAST_RUN);
        assertThat(testScheduler.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testScheduler.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Scheduler in Elasticsearch
        Scheduler schedulerEs = schedulerSearchRepository.findOne(testScheduler.getId());
        assertThat(schedulerEs).isEqualToComparingFieldByField(testScheduler);
    }

    @Test
    @Transactional
    public void createSchedulerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schedulerRepository.findAll().size();

        // Create the Scheduler with an existing ID
        Scheduler existingScheduler = new Scheduler();
        existingScheduler.setId(1L);
        SchedulerDTO existingSchedulerDTO = schedulerMapper.schedulerToSchedulerDTO(existingScheduler);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedulerMockMvc.perform(post("/api/schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSchedulerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkScheduleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = schedulerRepository.findAll().size();
        // set the field null
        scheduler.setScheduleName(null);

        // Create the Scheduler, which fails.
        SchedulerDTO schedulerDTO = schedulerMapper.schedulerToSchedulerDTO(scheduler);

        restSchedulerMockMvc.perform(post("/api/schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDTO)))
            .andExpect(status().isBadRequest());

        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRunEveryIsRequired() throws Exception {
        int databaseSizeBeforeTest = schedulerRepository.findAll().size();
        // set the field null
        scheduler.setRunEvery(null);

        // Create the Scheduler, which fails.
        SchedulerDTO schedulerDTO = schedulerMapper.schedulerToSchedulerDTO(scheduler);

        restSchedulerMockMvc.perform(post("/api/schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDTO)))
            .andExpect(status().isBadRequest());

        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchedulers() throws Exception {
        // Initialize the database
        schedulerRepository.saveAndFlush(scheduler);

        // Get all the schedulerList
        restSchedulerMockMvc.perform(get("/api/schedulers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduler.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduleName").value(hasItem(DEFAULT_SCHEDULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].runEvery").value(hasItem(DEFAULT_RUN_EVERY)))
            .andExpect(jsonPath("$.[*].timeToRun").value(hasItem(DEFAULT_TIME_TO_RUN.toString())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    public void getScheduler() throws Exception {
        // Initialize the database
        schedulerRepository.saveAndFlush(scheduler);

        // Get the scheduler
        restSchedulerMockMvc.perform(get("/api/schedulers/{id}", scheduler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scheduler.getId().intValue()))
            .andExpect(jsonPath("$.scheduleName").value(DEFAULT_SCHEDULE_NAME.toString()))
            .andExpect(jsonPath("$.runEvery").value(DEFAULT_RUN_EVERY))
            .andExpect(jsonPath("$.timeToRun").value(DEFAULT_TIME_TO_RUN.toString()))
            .andExpect(jsonPath("$.weekDay").value(DEFAULT_WEEK_DAY.toString()))
            .andExpect(jsonPath("$.lastRun").value(DEFAULT_LAST_RUN))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT));
    }

    @Test
    @Transactional
    public void getNonExistingScheduler() throws Exception {
        // Get the scheduler
        restSchedulerMockMvc.perform(get("/api/schedulers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduler() throws Exception {
        // Initialize the database
        schedulerRepository.saveAndFlush(scheduler);
        schedulerSearchRepository.save(scheduler);
        int databaseSizeBeforeUpdate = schedulerRepository.findAll().size();

        // Update the scheduler
        Scheduler updatedScheduler = schedulerRepository.findOne(scheduler.getId());
        updatedScheduler
                .scheduleName(UPDATED_SCHEDULE_NAME)
                .runEvery(UPDATED_RUN_EVERY)
                .timeToRun(UPDATED_TIME_TO_RUN)
                .weekDay(UPDATED_WEEK_DAY)
                .lastRun(UPDATED_LAST_RUN)
                .createdAt(UPDATED_CREATED_AT)
                .updatedAt(UPDATED_UPDATED_AT);
        SchedulerDTO schedulerDTO = schedulerMapper.schedulerToSchedulerDTO(updatedScheduler);

        restSchedulerMockMvc.perform(put("/api/schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDTO)))
            .andExpect(status().isOk());

        // Validate the Scheduler in the database
        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeUpdate);
        Scheduler testScheduler = schedulerList.get(schedulerList.size() - 1);
        assertThat(testScheduler.getScheduleName()).isEqualTo(UPDATED_SCHEDULE_NAME);
        assertThat(testScheduler.getRunEvery()).isEqualTo(UPDATED_RUN_EVERY);
        assertThat(testScheduler.getTimeToRun()).isEqualTo(UPDATED_TIME_TO_RUN);
        assertThat(testScheduler.getWeekDay()).isEqualTo(UPDATED_WEEK_DAY);
        assertThat(testScheduler.getLastRun()).isEqualTo(UPDATED_LAST_RUN);
        assertThat(testScheduler.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testScheduler.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Scheduler in Elasticsearch
        Scheduler schedulerEs = schedulerSearchRepository.findOne(testScheduler.getId());
        assertThat(schedulerEs).isEqualToComparingFieldByField(testScheduler);
    }

    @Test
    @Transactional
    public void updateNonExistingScheduler() throws Exception {
        int databaseSizeBeforeUpdate = schedulerRepository.findAll().size();

        // Create the Scheduler
        SchedulerDTO schedulerDTO = schedulerMapper.schedulerToSchedulerDTO(scheduler);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSchedulerMockMvc.perform(put("/api/schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedulerDTO)))
            .andExpect(status().isCreated());

        // Validate the Scheduler in the database
        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteScheduler() throws Exception {
        // Initialize the database
        schedulerRepository.saveAndFlush(scheduler);
        schedulerSearchRepository.save(scheduler);
        int databaseSizeBeforeDelete = schedulerRepository.findAll().size();

        // Get the scheduler
        restSchedulerMockMvc.perform(delete("/api/schedulers/{id}", scheduler.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean schedulerExistsInEs = schedulerSearchRepository.exists(scheduler.getId());
        assertThat(schedulerExistsInEs).isFalse();

        // Validate the database is empty
        List<Scheduler> schedulerList = schedulerRepository.findAll();
        assertThat(schedulerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchScheduler() throws Exception {
        // Initialize the database
        schedulerRepository.saveAndFlush(scheduler);
        schedulerSearchRepository.save(scheduler);

        // Search the scheduler
        restSchedulerMockMvc.perform(get("/api/_search/schedulers?query=id:" + scheduler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduler.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduleName").value(hasItem(DEFAULT_SCHEDULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].runEvery").value(hasItem(DEFAULT_RUN_EVERY)))
            .andExpect(jsonPath("$.[*].timeToRun").value(hasItem(DEFAULT_TIME_TO_RUN.toString())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scheduler.class);
    }
}

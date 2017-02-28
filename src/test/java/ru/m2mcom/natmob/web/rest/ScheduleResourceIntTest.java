package ru.m2mcom.natmob.web.rest;

import ru.m2mcom.natmob.SchedulerApp;

import ru.m2mcom.natmob.domain.Schedule;
import ru.m2mcom.natmob.repository.ScheduleRepository;
import ru.m2mcom.natmob.service.ScheduleService;
import ru.m2mcom.natmob.service.dto.ScheduleDTO;
import ru.m2mcom.natmob.service.mapper.ScheduleMapper;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.m2mcom.natmob.domain.enumeration.WeekDay;
/**
 * Test class for the ScheduleResource REST controller.
 *
 * @see ScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApp.class)
public class ScheduleResourceIntTest {

    private static final String DEFAULT_SCHEDULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RUN_EVERY = 1;
    private static final Integer UPDATED_RUN_EVERY = 2;

    private static final String DEFAULT_TIME = "12:23";
    private static final String UPDATED_TIME = "23:53";

    private static final WeekDay DEFAULT_WEEK_DAY = WeekDay.Sunday;
    private static final WeekDay UPDATED_WEEK_DAY = WeekDay.Monday;

    private static final Integer DEFAULT_LAST_RUN = 1;
    private static final Integer UPDATED_LAST_RUN = 2;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restScheduleMockMvc;

    private Schedule schedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleResource scheduleResource = new ScheduleResource(scheduleService);
        this.restScheduleMockMvc = MockMvcBuilders.standaloneSetup(scheduleResource)
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
    public static Schedule createEntity() {
        Schedule schedule = new Schedule()
                .scheduleName(DEFAULT_SCHEDULE_NAME)
                .runEvery(DEFAULT_RUN_EVERY)
                .time(DEFAULT_TIME)
                .weekDay(DEFAULT_WEEK_DAY)
                .lastRun(DEFAULT_LAST_RUN);
        return schedule;
    }

    @Before
    public void initTest() {
        scheduleRepository.deleteAll();
        schedule = createEntity();
    }

    @Test
    public void createSchedule() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate + 1);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getScheduleName()).isEqualTo(DEFAULT_SCHEDULE_NAME);
        assertThat(testSchedule.getRunEvery()).isEqualTo(DEFAULT_RUN_EVERY);
        assertThat(testSchedule.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testSchedule.getWeekDay()).isEqualTo(DEFAULT_WEEK_DAY);
        assertThat(testSchedule.getLastRun()).isEqualTo(DEFAULT_LAST_RUN);
    }

    @Test
    public void createScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // Create the Schedule with an existing ID
        Schedule existingSchedule = new Schedule();
        existingSchedule.setId("existing_id");
        ScheduleDTO existingScheduleDTO = scheduleMapper.scheduleToScheduleDTO(existingSchedule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkScheduleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleRepository.findAll().size();
        // set the field null
        schedule.setScheduleName(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkRunEveryIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduleRepository.findAll().size();
        // set the field null
        schedule.setRunEvery(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSchedules() throws Exception {
        // Initialize the database
        scheduleRepository.save(schedule);

        // Get all the scheduleList
        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId())))
            .andExpect(jsonPath("$.[*].scheduleName").value(hasItem(DEFAULT_SCHEDULE_NAME.toString())))
            .andExpect(jsonPath("$.[*].runEvery").value(hasItem(DEFAULT_RUN_EVERY)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].weekDay").value(hasItem(DEFAULT_WEEK_DAY.toString())))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN)));
    }

    @Test
    public void getSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.save(schedule);

        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", schedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schedule.getId()))
            .andExpect(jsonPath("$.scheduleName").value(DEFAULT_SCHEDULE_NAME.toString()))
            .andExpect(jsonPath("$.runEvery").value(DEFAULT_RUN_EVERY))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.weekDay").value(DEFAULT_WEEK_DAY.toString()))
            .andExpect(jsonPath("$.lastRun").value(DEFAULT_LAST_RUN));
    }

    @Test
    public void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.save(schedule);
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Update the schedule
        Schedule updatedSchedule = scheduleRepository.findOne(schedule.getId());
        updatedSchedule
                .scheduleName(UPDATED_SCHEDULE_NAME)
                .runEvery(UPDATED_RUN_EVERY)
                .time(UPDATED_TIME)
                .weekDay(UPDATED_WEEK_DAY)
                .lastRun(UPDATED_LAST_RUN);
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(updatedSchedule);

        restScheduleMockMvc.perform(put("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getScheduleName()).isEqualTo(UPDATED_SCHEDULE_NAME);
        assertThat(testSchedule.getRunEvery()).isEqualTo(UPDATED_RUN_EVERY);
        assertThat(testSchedule.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testSchedule.getWeekDay()).isEqualTo(UPDATED_WEEK_DAY);
        assertThat(testSchedule.getLastRun()).isEqualTo(UPDATED_LAST_RUN);
    }

    @Test
    public void updateNonExistingSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScheduleMockMvc.perform(put("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.save(schedule);
        int databaseSizeBeforeDelete = scheduleRepository.findAll().size();

        // Get the schedule
        restScheduleMockMvc.perform(delete("/api/schedules/{id}", schedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedule.class);
    }
}

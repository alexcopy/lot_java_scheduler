package ru.m2mcom.natmob.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import ru.m2mcom.natmob.domain.enumeration.WeekDay;

/**
 * A Scheduler.
 */
@Entity
@Table(name = "scheduler")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "scheduler")
public class Scheduler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "schedule_name", length = 20, nullable = false)
    private String scheduleName;

    @NotNull
    @Column(name = "run_every", nullable = false)
    private Integer runEvery;

    @Pattern(regexp = "(^[0-2][0-4]:[0-5][0-9]$)")
    @Column(name = "time_to_run")
    private String timeToRun;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private WeekDay weekDay;

    @Column(name = "last_run")
    private Integer lastRun;

    @Column(name = "created_at")
    private Integer createdAt;

    @Column(name = "updated_at")
    private Integer updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public Scheduler scheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Integer getRunEvery() {
        return runEvery;
    }

    public Scheduler runEvery(Integer runEvery) {
        this.runEvery = runEvery;
        return this;
    }

    public void setRunEvery(Integer runEvery) {
        this.runEvery = runEvery;
    }

    public String getTimeToRun() {
        return timeToRun;
    }

    public Scheduler timeToRun(String timeToRun) {
        this.timeToRun = timeToRun;
        return this;
    }

    public void setTimeToRun(String timeToRun) {
        this.timeToRun = timeToRun;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public Scheduler weekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getLastRun() {
        return lastRun;
    }

    public Scheduler lastRun(Integer lastRun) {
        this.lastRun = lastRun;
        return this;
    }

    public void setLastRun(Integer lastRun) {
        this.lastRun = lastRun;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public Scheduler createdAt(Integer createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public Scheduler updatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scheduler scheduler = (Scheduler) o;
        if (scheduler.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scheduler.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Scheduler{" +
            "id=" + id +
            ", scheduleName='" + scheduleName + "'" +
            ", runEvery='" + runEvery + "'" +
            ", timeToRun='" + timeToRun + "'" +
            ", weekDay='" + weekDay + "'" +
            ", lastRun='" + lastRun + "'" +
            ", createdAt='" + createdAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            '}';
    }
}

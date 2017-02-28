package ru.m2mcom.natmob.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import ru.m2mcom.natmob.domain.enumeration.WeekDay;

/**
 * A DTO for the Scheduler entity.
 */
public class SchedulerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    private String scheduleName;

    @NotNull
    private Integer runEvery;

    @Pattern(regexp = "(^[0-2][0-4]:[0-5][0-9]$)")
    private String timeToRun;

    private WeekDay weekDay;

    private Integer lastRun;

    private Integer createdAt;

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

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }
    public Integer getRunEvery() {
        return runEvery;
    }

    public void setRunEvery(Integer runEvery) {
        this.runEvery = runEvery;
    }
    public String getTimeToRun() {
        return timeToRun;
    }

    public void setTimeToRun(String timeToRun) {
        this.timeToRun = timeToRun;
    }
    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }
    public Integer getLastRun() {
        return lastRun;
    }

    public void setLastRun(Integer lastRun) {
        this.lastRun = lastRun;
    }
    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getUpdatedAt() {
        return updatedAt;
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

        SchedulerDTO schedulerDTO = (SchedulerDTO) o;

        if ( ! Objects.equals(id, schedulerDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SchedulerDTO{" +
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

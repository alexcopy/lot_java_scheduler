package ru.m2mcom.natmob.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import ru.m2mcom.natmob.domain.enumeration.WeekDay;

/**
 * A DTO for the Schedule entity.
 */
public class ScheduleDTO implements Serializable {

    private String id;

    @NotNull
    @Size(min = 3, max = 20)
    private String scheduleName;

    @NotNull
    private Integer runEvery;

    @Pattern(regexp = "(^[0-2][0-4]:[0-5][0-9]$)")
    private String time;

    private WeekDay weekDay;

    private Integer lastRun;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduleDTO scheduleDTO = (ScheduleDTO) o;

        if ( ! Objects.equals(id, scheduleDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
            "id=" + id +
            ", scheduleName='" + scheduleName + "'" +
            ", runEvery='" + runEvery + "'" +
            ", time='" + time + "'" +
            ", weekDay='" + weekDay + "'" +
            ", lastRun='" + lastRun + "'" +
            '}';
    }
}

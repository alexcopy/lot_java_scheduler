package ru.m2mcom.natmob.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import ru.m2mcom.natmob.domain.enumeration.WeekDay;

/**
 * A Schedule.
 */

@Document(collection = "schedule")
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 3, max = 20)
    @Field("schedule_name")
    private String scheduleName;

    @NotNull
    @Field("run_every")
    private Integer runEvery;

    @Pattern(regexp = "(^[0-2][0-4]:[0-5][0-9]$)")
    @Field("time")
    private String time;

    @Field("week_day")
    private WeekDay weekDay;

    @Field("last_run")
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

    public Schedule scheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Integer getRunEvery() {
        return runEvery;
    }

    public Schedule runEvery(Integer runEvery) {
        this.runEvery = runEvery;
        return this;
    }

    public void setRunEvery(Integer runEvery) {
        this.runEvery = runEvery;
    }

    public String getTime() {
        return time;
    }

    public Schedule time(String time) {
        this.time = time;
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public Schedule weekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getLastRun() {
        return lastRun;
    }

    public Schedule lastRun(Integer lastRun) {
        this.lastRun = lastRun;
        return this;
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
        Schedule schedule = (Schedule) o;
        if (schedule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, schedule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Schedule{" +
            "id=" + id +
            ", scheduleName='" + scheduleName + "'" +
            ", runEvery='" + runEvery + "'" +
            ", time='" + time + "'" +
            ", weekDay='" + weekDay + "'" +
            ", lastRun='" + lastRun + "'" +
            '}';
    }
}

package main.java;

/**
 * DayRecord represent Information about work done in one day
 * Created by oldrichkonecny on 13.05.16.
 */
public class DayRecord {

    private Long unixDate;
    private int workHours;
    private WorkType workType;


    public Long getUnixDate() {return unixDate;}
    public void setUnixDate(Long unixDate) {this.unixDate = unixDate;}

    public int getWorkHours() {return workHours;}
    public void setWorkHours(int workHours) {this.workHours = workHours;}

    public WorkType getWorkType() {return workType;}
    public void setWorkType(WorkType workType) {this.workType = workType;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DayRecord dayRecord = (DayRecord) o;

        return unixDate.equals(dayRecord.unixDate);

    }

    @Override
    public int hashCode() {
        return unixDate.hashCode();
    }
}

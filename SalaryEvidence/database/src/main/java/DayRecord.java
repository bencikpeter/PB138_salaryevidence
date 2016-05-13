/**
 * DayRecord represent Information about work done in one day
 * Created by oldrichkonecny on 13.05.16.
 */
public class DayRecord {

    private Long id;
    private Long unixDate;
    private int workHours;
    private WorkType workType;


    //private Person person;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getUnixDate() {return unixDate;}
    public void setUnixDate(Long unixDate) {this.unixDate = unixDate;}

    public int getWorkHours() {return workHours;}
    public void setWorkHours(int workHours) {this.workHours = workHours;}

    public WorkType getWorkType() {return workType;}
    public void setWorkType(WorkType workType) {this.workType = workType;}

    /*
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DayRecord)) return false;

        DayRecord dayRecord = (DayRecord) o;

        if (id != null ? !id.equals(dayRecord.id) : dayRecord.id != null) return false;
        return unixDate != null ? unixDate.equals(dayRecord.unixDate) : dayRecord.unixDate == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (unixDate != null ? unixDate.hashCode() : 0);
        return result;
    }
}

package Database;

import java.util.Objects;
import GUI.Jobs;

/**
 * Object represents one working day.

 * @author Peter Tirala
 */
public class Day {
    private int hours;
    private Long date;
    private Jobs job;
    private double perHours;



    public double getPerHours() {return perHours;}

    public void setPerHours(double perHours) { this.perHours = perHours; }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Jobs getJob() {
        return job;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.date);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Day other = (Day) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        return true;
    }
    
    
}

import java.time.LocalDate;
import java.util.List;

/**
 * Created by oldrichkonecny on 13.05.16.
 */
public class DatabaseManagerImpl implements DatabaseManager{


    @Override
    public void createRecord(DayRecord dayRecord) throws DatabaseFailureException {

    }

    @Override
    public void updateRecord(DayRecord dayRecord) throws DatabaseFailureException {

    }

    @Override
    public void deleteRecord(DayRecord dayRecord) throws DatabaseFailureException {

    }

    @Override
    public void deleteRecord(Long unixDate) throws DatabaseFailureException {

    }

    @Override
    public List<DayRecord> findRecord(Long id) throws DatabaseFailureException {
        return null;
    }

    @Override
    public List<DayRecord> findRecord(LocalDate date) throws DatabaseFailureException {
        return null;
    }

    @Override
    public List<DayRecord> findRecord(LocalDate from, LocalDate to) throws DatabaseFailureException {
        return null;
    }

    @Override
    public DayRecord getOldest() throws DatabaseFailureException {
        return null;
    }

    @Override
    public DayRecord getNewest() throws DatabaseFailureException {
        return null;
    }
}

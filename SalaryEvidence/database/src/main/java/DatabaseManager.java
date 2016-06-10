import javax.xml.bind.DataBindingException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for DatabaseManager
 * Created by oldrichkonecny on 13.05.16.
 */
public interface DatabaseManager {

    /**
     * Stores new record into database, id is generated automatically.
     * @param dayRecord record to be stored.
     * @throws DatabaseFailureException when db operation fails.
     */
    void createRecord(DayRecord dayRecord) throws DatabaseFailureException;

    /**
     * Update record in database, if record is not found in database, nothing happen.
     * @param dayRecord record to be updated.
     * @throws DatabaseFailureException when db operation fails.
     */
    void updateRecord(DayRecord dayRecord) throws DatabaseFailureException;

    /**
     * Delete record in database, if record is not found, nothing happen.
     * @param dayRecord record to be deleted.
     * @throws DatabaseFailureException when db operation fails.
     */
    void deleteRecord(DayRecord dayRecord) throws DatabaseFailureException;

    /**
     * Delete record in database, if record is not found, nothing happen.
     * @param unixDate date of record to be deleted.
     * @throws DatabaseFailureException when db operation fails.
     */
    void deleteRecord(Long unixDate) throws DatabaseFailureException;

    /**
     * Find record in database by id
     * @param id of Record to be found.
     * @return list with one or none record.
     * @throws DatabaseFailureException when db operation fails.
     */
    List<DayRecord> findRecord(Long id) throws DatabaseFailureException;

    /**
     * Find record in database by date.
     * @param date of record to be found.
     * @return list with one or none record.
     * @throws DatabaseFailureException when db operation fails.
     */
    List<DayRecord> findRecord(LocalDate date) throws DatabaseFailureException;

    /**
     * Find all records in database from - to.
     * @param from date from when records will be chosen.
     * @param to date to when records will be chosen
     * @return List of records
     * @throws DatabaseFailureException when db operation fails.
     */
    List<DayRecord> findRecord(LocalDate from, LocalDate to) throws DatabaseFailureException;

    /**
     * find oldest record in database.
     * @return record from database or null if database is empty.
     * @throws DatabaseFailureException when db operation fails.
     */
    DayRecord getOldest() throws DatabaseFailureException;

    /**
     * find newest record in database.
     * @return record from database of null if database is empty.
     * @throws DatabaseFailureException when db operation fails.
     */
    DayRecord getNewest() throws DatabaseFailureException;

}

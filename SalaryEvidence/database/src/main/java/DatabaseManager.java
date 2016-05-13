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


    List<DayRecord> findRecord(Long id) throws DatabaseFailureException;
}

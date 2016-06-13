package main.java;

import javax.xml.bind.DataBindingException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for DatabaseManager
 * Created by oldrichkonecny on 13.05.16.
 */
public interface DatabaseManager {

    /**
     * Stores new record into database, date is name of file and primary key.
     * If file with same date already exist in db file is updated
     * @param day record to be stored.
     * @throws DatabaseFailureException when db operation fails.
     */
    void createRecord(Day day) throws DatabaseFailureException;

    /**
     * Delete record in database, if record is not found, nothing happen.
     * @param day record to be deleted.
     * @throws DatabaseFailureException when db operation fails.
     */
    void deleteRecord(Day day) throws DatabaseFailureException;

    /**
     * Delete record in database, if record is not found, nothing happen.
     * @param unixDate date of record to be deleted.
     * @throws DatabaseFailureException when db operation fails.
     */
    void deleteRecord(long unixDate) throws DatabaseFailureException;


    /**
     * Find record in database by date.
     * @param date of record to be found.
     * @return list with one or none record.
     * @throws DatabaseFailureException when db operation fails.
     */
    List<Day> findRecord(long date) throws DatabaseFailureException;

    /**
     * Find all records in database from - to by date.
     * @param from date from when records will be chosen.
     * @param to date to when records will be chosen
     * @return List of records
     * @throws DatabaseFailureException when db operation fails.
     */
    List<Day> findRecord(long from, long to) throws DatabaseFailureException;


}

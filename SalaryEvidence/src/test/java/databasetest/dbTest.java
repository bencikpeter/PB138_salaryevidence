/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasetest;

import Logic.Day;
import Logic.Jobs;
import Logic.ServiceFailureException;

/**
 *
 * @author peter
 */
public class dbTest {

            
     @Test
     public void findDay( long date ) throws ServiceFailureException {
         Day day = new Day();
         day.setDate(date);
         day.setHours(8);
         day.setJob(Jobs.ACCOUNTANT);
         manager.createRecord(day);
         
         Day result = manager.findRecord(date);
         assertThat(result, is(equalTo(day)));
         assertDeepEquals(day,result);
     }     
     
     @Test
     public void createDay() throws ServiceFailureException {
         Day day = new Day();
         day.setDate(18L);
         day.setHours(8);
         day.setJob(Jobs.ACCOUNTANT);
         manager.createRecord(day);
         
         Long date = day.getDate();
         assertThat(day.getDate(),is(not(equalTo(null))));
         
         Day result = manager.findRecord(date);
         assertThat(result, is(equalTo(day)));
         assertThat(result, is(not(sameInstance(day))));
         assertDeepEquals(day,result);
     }
     
     @Test (expected = IllegalArgumentException.class)
    public void createNullDay() throws ServiceFailureException {
        manager.createRecord(null);
    }
    
    @Test
    public void createDayWithNullJob() throws ServiceFailureException {
        Day day = new Day();
        day.setDate(18L);
        day.setHours(8);
        day.setJob(null);
        
        expectedException.expect(ValidationException.class);
        manager.createRecord(day);
    }
    
    @Test
    public void createDaytWithNegativeHours() throws ServiceFailureException {
         Day day = new Day();
         day.setDate(18L);
         day.setHours(-8);
         day.setJob(Jobs.ACCOUNTANT);
        expectedException.expect(ValidationException.class);
        manager.createStudent(student);
    }     
    
    
     
     private void assertDeepEquals(Day expected, Day actual) {
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getHours(), actual.getHours());
        assertEquals(expected.getJob(), actual.getJob());
    }
     
}

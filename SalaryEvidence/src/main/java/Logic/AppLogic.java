package Logic;

/**
 *Interface for application logic. 
 * There are functions for GUI, which combine database and transformations.
 * @author Peter Tirala
 */
public interface AppLogic {
    void transoformDoc(int from,int to/*source from DB */);
    
    void transformPdf(int from,int to/*source from DB*/);
    
    void createXML(Jobs job,Day[] day);
}

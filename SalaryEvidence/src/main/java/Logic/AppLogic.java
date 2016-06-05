package Logic;

import java.nio.file.Path;
import java.util.List;

/**
 *Interface for application logic. 
 * There are functions for GUI, which combine database and transformations.
 * @author Peter Tirala
 */
public interface AppLogic {
    public Path createInvoice (List<Day> days);
}

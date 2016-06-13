package Logic;

import java.util.ResourceBundle;

/**
 * Jobs which can person do.
 * @author Peter Tirala
 */

public enum Jobs {
    REGIONAL_MANAGER, 
    ASSISTANT_REGIONAL_MANAGER, 
    HUMAN_RESOURCES_REPRESENTATIVE, 
    SALESPERSON, 
    ACCOUNTANT, 
    SUPPLIER_RELATIONS,
    QUALITY_ASSURANCE, 
    CUSTOMER_RELATIONS, 
    WAREHOUSE_FOREMAN, 
    WAREHOUSE_EMPLOYEE, 
    RECEPTIONIST;

    
  private static final ResourceBundle bundle = ResourceBundle.getBundle("GUI/Bundle");  
  /**
   * Returns localised job from the enum
   * @return localised enum string
   */
  @Override
  public String toString() {
    switch(this) {
      case REGIONAL_MANAGER: return bundle.getString("JOBS_REGIONAL_MANAGER");
      case ASSISTANT_REGIONAL_MANAGER: return bundle.getString("JOBS_ASSISTANT_REGIONAL_MANAGER");
      case HUMAN_RESOURCES_REPRESENTATIVE: return bundle.getString("JOBS_HUMAN_RESOURCES_REPRESENTATIVE");
      case SALESPERSON: return bundle.getString("JOBS_SALESPERSON");
      case ACCOUNTANT: return bundle.getString("JOBS_ACCOUNTANT");
      case SUPPLIER_RELATIONS: return bundle.getString("JOBS_SUPPLIER_RELATIONS");
      case QUALITY_ASSURANCE: return bundle.getString("JOBS_QUALITY_ASSURANCE");
      case CUSTOMER_RELATIONS: return bundle.getString("JOBS_CUSTOMER_RELATIONS");
      case WAREHOUSE_FOREMAN: return bundle.getString("JOBS_WAREHOUSE_FOREMAN");
      case WAREHOUSE_EMPLOYEE: return bundle.getString("JOBS_WAREHOUSE_EMPLOYEE");
      case RECEPTIONIST: return bundle.getString("JOBS_RECEPTIONIST");
      default: throw new IllegalArgumentException();
    }
  }
}
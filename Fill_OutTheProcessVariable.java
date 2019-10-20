package org.camunda.bpm.myproject.electronic_equipment_competitions;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.camunda.spin.Spin.*;


public class Fill_OutTheProcessVariable implements JavaDelegate {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:./camunda-h2-dbs/process-engine";  //poia einai thelei allagi

	// Database credentials 
	static final String USER = "sa"; 
	static final String PASS = "sa";

  	public void execute(DelegateExecution execution) throws Exception {
  
		Map<String, String> productTypes = new HashMap<String, String>();

		productTypes.put("001", "Notebook");
		productTypes.put("002", "Server");
		productTypes.put("003", "Workstation");
		//execution.setVariable("PRODUCT_CATEGORIES_2",  org.camunda.spin.Spin.JSON(productTypes));

		System.out.println("Connected database successfully...");
		Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
         
		// STEP 3: Execute a query 
		System.out.println("Connected database successfully...");
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM PRODUCT_CATEGORIES";
		ResultSet rs = stmt.executeQuery(sql);

		List<HashMap<String, String>> listOfMaps = new ArrayList<HashMap<String, String>>();
		while(rs.next()){
			Map<String, String> productCategory = new HashMap<String, String>();
			// Retrieve by column name 
			//int id  = rs.getInt("id");
			productCategory.put("name",rs.getString("name"));
			System.out.print(", name: " + rs.getString("name"));
			listOfMaps.add((HashMap<String, String>) productCategory);
		}
		execution.setVariable("PRODUCT_CATEGORIES",  org.camunda.spin.Spin.JSON(listOfMaps));
		rs.close();
  
  }
  
}  
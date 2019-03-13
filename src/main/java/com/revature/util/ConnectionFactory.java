package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.jdbc.driver.OracleDriver;

public class ConnectionFactory {
	
	private static Logger log = LogManager.getLogger(ConnectionFactory.class);
	private static ConnectionFactory cf = new ConnectionFactory();
	
	private ConnectionFactory() {
		super();
	}
	
	public static ConnectionFactory getInstance() {
		return cf;
	}
	
	public Connection getConnection() {
		
		Connection conn = null;
		
		// We use a .properties file so we do not hard-code our DB credentials into the source code
		Properties prop = new Properties();
		
		try {
			
			DriverManager.registerDriver(new OracleDriver());
			
			// Load the properties file (application.properties) keys/values into the Properties object
			prop.load(new FileReader("C:\\Users\\Wezley\\batch-repos\\190107-Java-Spark-USF\\Week_3-Client_SDLC\\workspace\\revature_book_store_v3\\src\\main\\resources\\application.properties"));
			
			// Get a connection from the DriverManager class
			conn = DriverManager.getConnection(
					prop.getProperty("url"),
					prop.getProperty("usr"),
					prop.getProperty("pw"));
			
		} catch (SQLException sqle) {
			log.error(sqle.getMessage());
		} catch (FileNotFoundException fnfe) {
			log.error(fnfe.getMessage());
		} catch (IOException ioe) {
			log.error(ioe.getMessage());
		}
		
		if (conn == null) log.warn("Connection object is null");
		return conn;
	}

}

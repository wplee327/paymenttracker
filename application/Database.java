package application;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	Connection dbConnection = null;

	public Database(String dbName) {
		if (existsDatabase(dbName)) {
			try {
				Class.forName("org.sqlite.JDBC");
				dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else {
			try {
				Class.forName("org.sqlite.JDBC");
				dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			initDatabase();
		}
	}

	public boolean openConnection(String dbName) {
		try {
			Class.forName("org.sqlite.JDBC");
			dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			if (dbConnection.isValid(10)) {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	public boolean closeConnection() {
		try {
			dbConnection.close();
			if (dbConnection.isClosed()) {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	public boolean addClient(String lastName, String firstName) {
		Statement sqlStatement = null;
		int result = Statement.EXECUTE_FAILED;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "INSERT INTO CLIENTS (LASTNAME, FIRSTNAME) VALUES ('" + lastName + "', '" + firstName
					+ "')";
			result = sqlStatement.executeUpdate(sqlString);
			sqlStatement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (result == Statement.EXECUTE_FAILED) {
			return false;
		}
		return true;
	}

	public boolean removeClient(int idNum) {
		Statement sqlStatement = null;
		int result = Statement.EXECUTE_FAILED;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "DELETE FROM PAYMENTS WHERE CLIENTID = " + idNum;
			result = sqlStatement.executeUpdate(sqlString);
			sqlString = "DELETE FROM CLIENTS WHERE ID = " + idNum;
			result = sqlStatement.executeUpdate(sqlString);
			sqlStatement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (result == Statement.EXECUTE_FAILED) {
			return false;
		}
		return true;
	}

	public int getID(String lastName, String firstName) {
		Statement sqlStatement = null;
		ResultSet result = null;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "SELECT ID FROM CLIENTS WHERE LASTNAME = '" + lastName + "' AND FIRSTNAME = '"
					+ firstName + "'";
			result = sqlStatement.executeQuery(sqlString);
			sqlStatement.close();
			return result.getInt("ID");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	public void initDatabase() {
		Statement sqlStatement = null;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "CREATE TABLE CLIENTS " + "(ID INT PRIMARY KEY NOT NULL, " + "LASTNAME TEXT NOT NULL, "
					+ "FIRSTNAME TEXT NOT NULL)";
			sqlStatement.executeUpdate(sqlString);
			sqlString = "CREATE TABLE PAYMENTS " + "(ID INT PRIMARY KEY NOT NULL, " + "CLIENTID INT NOT NULL, "
					+ "MONTH INT NOT NULL, " + "UNPAID INT NOT NULL, " + "PENDING INT NOT NULL, "
					+ "PAID INT NOT NULL)";
			sqlStatement.executeUpdate(sqlString);
			sqlStatement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean existsDatabase(String dbName) {
		File dbFile = new File(dbName);
		if (dbFile.exists()) {
			return true;
		}
		return false;
	}
}

package application;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	private Connection dbConnection = null;

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

	public void openConnection(String dbName) {
		try {
			Class.forName("org.sqlite.JDBC");
			dbConnection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			dbConnection.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void addClient(String lastName, String firstName) {
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
	}

	public void removeClient(int idNum) {
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
	}

	public int getID(String lastName, String firstName) {
		Statement sqlStatement = null;
		int idNum = -1;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "SELECT ID FROM CLIENTS WHERE LASTNAME = '" + lastName + "' AND FIRSTNAME = '"
					+ firstName + "'";
			ResultSet result = sqlStatement.executeQuery(sqlString);
			sqlStatement.close();
			idNum = result.getInt("ID");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return idNum;
	}

	public int[] getPayments(int clientID, String date) {
		Statement sqlStatement = null;
		int[] paymentsArray = new int[3];
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "SELECT UNPAID, PENDING, PAID FROM PAYMENTS WHERE CLIENTID = "
					+ Integer.toString(clientID) + " AND DATE = " + date;
			ResultSet result = sqlStatement.executeQuery(sqlString);
			sqlStatement.close();
			for (int idx = 0; idx < 3; idx++) {
				paymentsArray[idx] = result.getInt(idx + 1);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return paymentsArray;
	}

	public void initDatabase() {
		Statement sqlStatement = null;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "CREATE TABLE CLIENTS " + "(ID INT PRIMARY KEY NOT NULL, " + "LASTNAME TEXT NOT NULL, "
					+ "FIRSTNAME TEXT NOT NULL)";
			sqlStatement.executeUpdate(sqlString);
			sqlString = "CREATE TABLE PAYMENTS " + "(ID INT PRIMARY KEY NOT NULL, " + "CLIENTID INT NOT NULL, "
					+ "DATE TEXT NOT NULL, " + "UNPAID INT NOT NULL, " + "PENDING INT NOT NULL, "
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

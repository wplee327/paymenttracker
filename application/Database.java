package application;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import application.Database.Clients;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Database {
	private Connection dbConnection = null;

	public class Clients {
		private IntegerProperty key;
		private StringProperty lastName, firstName;

		public void setKey(int key) {
			KeyProperty().set(key);
		}

		public int getKey() {
			return KeyProperty().get();
		}

		public IntegerProperty KeyProperty() {
			if (key == null) {
				key = new SimpleIntegerProperty(this, "key");
			}
			return key;
		}

		public void setLastName(String lastName) {
			LastNameProperty().set(lastName);
		}

		public String getLastName() {
			return LastNameProperty().get();
		}

		public StringProperty LastNameProperty() {
			if (lastName == null) {
				lastName = new SimpleStringProperty(this, "lastName");
			}
			return lastName;
		}

		public void setFirstName(String firstName) {
			FirstNameProperty().set(firstName);
		}

		public String getFirstName() {
			return FirstNameProperty().get();
		}

		public StringProperty FirstNameProperty() {
			if (firstName == null) {
				firstName = new SimpleStringProperty(this, "firstName");
			}
			return firstName;
		}
	}

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

	public List<Clients> getAllClients() {
		Statement sqlStatement = null;
		List<Clients> clientsList = new LinkedList<Clients>();
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "SELECT * FROM CLIENTS";
			ResultSet tempResult = sqlStatement.executeQuery(sqlString);
			while (tempResult.next()) {
				int key = tempResult.getInt(1);
				String lastName = tempResult.getString(2);
				String firstName = tempResult.getString(3);
				Clients client = new Clients();
				client.setKey(key);
				client.setLastName(lastName);
				client.setFirstName(firstName);
				clientsList.add(client);
			}
			sqlStatement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return clientsList;
	}

	public void addClient(String lastName, String firstName) {
		Statement sqlStatement = null;
		int result = Statement.EXECUTE_FAILED;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "SELECT * FROM CLIENTS WHERE LASTNAME = '" + lastName + "' AND FIRSTNAME = '" + firstName
					+ "'";
			ResultSet tempResult = sqlStatement.executeQuery(sqlString);
			if (!tempResult.next()) {
				sqlString = "INSERT INTO CLIENTS (ID, LASTNAME, FIRSTNAME) VALUES (NULL, '" + lastName + "', '"
						+ firstName + "')";
				result = sqlStatement.executeUpdate(sqlString);
			}
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
			idNum = result.getInt("ID");
			sqlStatement.close();
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
			for (int idx = 0; idx < 3; idx++) {
				paymentsArray[idx] = result.getInt(idx + 1);
			}
			sqlStatement.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return paymentsArray;
	}

	public void initDatabase() {
		Statement sqlStatement = null;
		try {
			sqlStatement = dbConnection.createStatement();
			String sqlString = "CREATE TABLE CLIENTS " + "(ID INTEGER PRIMARY KEY, " + "LASTNAME TEXT NOT NULL, "
					+ "FIRSTNAME TEXT NOT NULL)";
			sqlStatement.executeUpdate(sqlString);
			sqlString = "CREATE TABLE PAYMENTS " + "(ID INTEGER PRIMARY KEY, " + "CLIENTID INTEGER NOT NULL, "
					+ "DATE TEXT NOT NULL, " + "UNPAID INTEGER NOT NULL, " + "PENDING INTEGER NOT NULL, "
					+ "PAID INTEGER NOT NULL)";
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

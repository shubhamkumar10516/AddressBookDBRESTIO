package AddressBookProblem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddressbookDBService {
	Connection connection = null;
	String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
	String user = "root";
	String password = "password";

	public Connection getConnection() throws SQLException {
		connection = DriverManager.getConnection(jdbcURL, user, password);
		return connection;
	}

	public int retriveDataFromDB() {
		int count = 0;
		String sql = "select * from address_Book";
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				count++;
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int UpdateDataToDB(String lastName, String changedName) {
		int result = 0;
		String sql = "update Contact set last_name = ? where last_name = ?";
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setString(1, changedName);
			pstmt.setString(2, lastName);
			result = pstmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int retrieveasDateFromDB(LocalDate start, LocalDate end) {

		int result = 0;
		String sql = "select * from Contact where start_date between ? and ?";
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setDate(1, Date.valueOf(start));
			pstmt.setDate(2, Date.valueOf(end));
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				result++;
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int retrieveDataByStateOrCity(String city, String state) {
		int result = 0;
		String sql = "select count(email) from City_State_Contact where city = ? or state = ?";
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(sql);
			pstmt.setString(1, city);
			pstmt.setString(2, state);
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getInt(1);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

//	Add Contact to DB	
	// first_name, last_name , zip, email, startDate, name, phone, type
	public int addNewContact(String f_name, String l_name, int zip, String email, LocalDate date, String name,
			int phone, String type) throws AddressBookException {
		String sql = "Insert into Contact Values (?, ?, ?, ?, ?);";
		try {
			PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, f_name);
			preparedStatement.setString(3, l_name);
			preparedStatement.setInt(4, zip);
			preparedStatement.setDate(5, Date.valueOf(date));
			int row = preparedStatement.executeUpdate();
			sql = "Insert into Name_Contact Values (?, ?);";
			preparedStatement = getConnection().prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, name);
			row += preparedStatement.executeUpdate();
			sql = "Insert into Phone_Contact Values (?, ?);";
			preparedStatement = getConnection().prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setInt(2, phone);
			row += preparedStatement.executeUpdate();
			sql = "Insert into Type_Contact Values (?, ?);";
			preparedStatement = getConnection().prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, type);
			row += preparedStatement.executeUpdate();
			return row;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Contact addNewContact(String firstName, String lastName, int zip, String email, LocalDate date) {
		String sql = "Insert into Contact Values (?, ?, ?, ?, ?);";
		try {
			PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setInt(4, zip);
			preparedStatement.setDate(5, Date.valueOf(date));
			int row = preparedStatement.executeUpdate();
			return new Contact(email, firstName, lastName, zip, date);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

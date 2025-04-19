package ru.itmo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ComputerDao {

	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "test";
	private static final String PASSWORD = "test";

	private static final String SELECT_SQL = "SELECT * FROM computer";

	public List<Computer> getAllBooks() {
		List<Computer> books = new ArrayList<>();

		try (Connection conn = getConnection()) {
			ResultSet rs = conn.createStatement().executeQuery(SELECT_SQL);
			while (rs.next()) {
				books.add(new Computer(
					rs.getInt("ID"),
					rs.getString("CPU")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return books;
	}


	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static void main(String[] args) {
		ComputerDao dao = new ComputerDao();
		List<Computer> books = dao.getAllBooks();
	}
}

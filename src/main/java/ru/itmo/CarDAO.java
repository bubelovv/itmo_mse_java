package ru.itmo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private static final Logger logger = LoggerFactory.getLogger(CarDAO.class);

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS cars";
    private static final String INSERT_CAR_SQL = "INSERT INTO cars (make, model, color, manufacture_date, price) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CARS_SQL = "SELECT * FROM cars";
    private static final String SELECT_CAR_BY_ID_SQL = "SELECT * FROM cars WHERE id = ?";
    private static final String UPDATE_CAR_SQL = "UPDATE cars SET make = ?, model = ?, color = ?, manufacture_date = ?, price = ? WHERE id = ?";
    private static final String DELETE_CAR_SQL = "DELETE FROM cars WHERE id = ?";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE cars (" + "id SERIAL PRIMARY KEY, " + "make VARCHAR(255) NOT NULL, " + "model VARCHAR(255) NOT NULL, " + "color VARCHAR(255), " + "manufacture_date DATE, " + "price DOUBLE PRECISION" + ")";

    public CarDAO() {
        setupTable();
    }

    public void setupTable() {
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement()) {

            logger.info("Setting up cars table");

            stmt.execute(DROP_TABLE_SQL);
            logger.info("Dropped cars table");

            stmt.execute(CREATE_TABLE_SQL);
            logger.info("Created cars table");

        } catch (SQLException e) {
            logger.error("Error setting up cars table", e);
            throw new RuntimeException("Failed to set up cars table", e);
        }
    }

    public Car add(Car car) {
        logger.debug("Adding new car: {}", car);

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_CAR_SQL, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(car, stmt);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                logger.error("Failed to add car, no rows affected");
                throw new SQLException("Failed to add car, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    car.setId(id);
                    logger.debug("Added car with ID: {}", id);
                    return car;
                } else {
                    logger.error("Failed to add car, no ID obtained");
                    throw new SQLException("Failed to add car, no ID obtained");
                }
            }

        } catch (SQLException e) {
            logger.error("Error adding car", e);
            throw new RuntimeException("Failed to add car", e);
        }
    }

    private void fillStatement(Car car, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, car.getMake());
        stmt.setString(2, car.getModel());
        stmt.setString(3, car.getColor());

        if (car.getManufactureDate() != null) {
            stmt.setDate(4, new Date(car.getManufactureDate().getTime()));
        } else {
            stmt.setNull(4, Types.DATE);
        }

        if (car.getPrice() != null) {
            stmt.setDouble(5, car.getPrice());
        } else {
            stmt.setNull(5, Types.DOUBLE);
        }
    }

    public Car getById(long id) {
        logger.debug("Getting car by ID: {}", id);

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_CAR_BY_ID_SQL)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Car car = extractCarFromResultSet(rs);
                    logger.debug("Found car: {}", car);
                    return car;
                } else {
                    logger.debug("No car found with ID: {}", id);
                    return null;
                }
            }

        } catch (SQLException e) {
            logger.error("Error getting car by ID: {}", id, e);
            throw new RuntimeException("Failed to get car by ID", e);
        }
    }

    public List<Car> getAll() {
        logger.debug("Getting all cars");

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(SELECT_ALL_CARS_SQL)) {

            List<Car> cars = new ArrayList<>();

            while (rs.next()) {
                Car car = extractCarFromResultSet(rs);
                cars.add(car);
            }

            logger.debug("Found {} cars", cars.size());
            return cars;

        } catch (SQLException e) {
            logger.error("Error getting all cars", e);
            throw new RuntimeException("Failed to get all cars", e);
        }
    }

    public boolean update(Car car) {
        logger.debug("Updating car: {}", car);

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE_CAR_SQL)) {

            fillStatement(car, stmt);

            stmt.setLong(6, car.getId());

            int affectedRows = stmt.executeUpdate();

            logger.debug("Updated car, affected rows: {}", affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            logger.error("Error updating car", e);
            throw new RuntimeException("Failed to update car", e);
        }
    }

    public boolean delete(long id) {
        logger.debug("Deleting car with ID: {}", id);

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(DELETE_CAR_SQL)) {

            stmt.setLong(1, id);

            int affectedRows = stmt.executeUpdate();

            logger.debug("Deleted car, affected rows: {}", affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            logger.error("Error deleting car with ID: {}", id, e);
            throw new RuntimeException("Failed to delete car", e);
        }
    }

    private Car extractCarFromResultSet(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("id"));
        car.setMake(rs.getString("make"));
        car.setModel(rs.getString("model"));
        car.setColor(rs.getString("color"));

        Date manufactureDate = rs.getDate("manufacture_date");
        if (manufactureDate != null) {
            car.setManufactureDate(new java.util.Date(manufactureDate.getTime()));
        }

        car.setPrice(rs.getDouble("price"));
        if (rs.wasNull()) {
            car.setPrice(null);
        }

        return car;
    }
}

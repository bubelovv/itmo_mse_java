package ru.itmo.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.CarDAO;
import ru.itmo.Car;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/cars/edit")
public class EditCarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EditCarServlet.class);
    private CarDAO carDAO;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        logger.info("EditCarServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            logger.debug("Displaying edit form for car with ID: {}", id);

            Car car = carDAO.getById(id);
            if (car != null) {
                req.setAttribute("car", car);
                req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req, resp);
            } else {
                logger.warn("Car with ID {} not found", id);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid car ID format", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Processing edit car form submission");

        try {
            Car car = new Car();

            car.setId(Long.parseLong(req.getParameter("id")));
            car.setMake(req.getParameter("make"));
            car.setModel(req.getParameter("model"));
            car.setColor(req.getParameter("color"));

            String dateStr = req.getParameter("manufactureDate");
            if (dateStr != null && !dateStr.isEmpty()) {
                try {
                    car.setManufactureDate(dateFormat.parse(dateStr));
                } catch (ParseException e) {
                    logger.warn("Invalid date format: {}", dateStr);
                }
            }

            String priceStr = req.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty()) {
                try {
                    car.setPrice(Double.parseDouble(priceStr));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid price format: {}", priceStr);
                }
            }

            boolean updated = carDAO.update(car);
            if (updated) {
                logger.info("Car updated successfully: {}", car);
                resp.sendRedirect(req.getContextPath() + "/cars");
            } else {
                logger.warn("Car with ID {} not found for update", car.getId());
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found for update");
            }
        } catch (Exception e) {
            logger.error("Error updating car", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing car data");
        }
    }
}

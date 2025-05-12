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

@WebServlet("/cars/new")
public class AddCarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddCarServlet.class);
    private CarDAO carDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        logger.info("AddCarServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Displaying add car form");
        req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Processing add car form submission");

        try {
            Car car = new Car();

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

            carDAO.add(car);

            logger.info("Car added successfully: {}", car);
            resp.sendRedirect(req.getContextPath() + "/cars");
        } catch (Exception e) {
            logger.error("Error adding car", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing car data");
        }
    }
}

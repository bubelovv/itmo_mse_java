package ru.itmo.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.CarDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cars/delete")
public class DeleteCarServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteCarServlet.class);
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        logger.info("DeleteCarServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            logger.debug("Deleting car with ID: {}", id);

            if (carDAO.delete(id)) {
                logger.info("Car with ID {} deleted successfully", id);
            } else {
                logger.warn("Car with ID {} not found for deletion", id);
            }

            resp.sendRedirect(req.getContextPath() + "/cars");
        } catch (NumberFormatException e) {
            logger.error("Invalid car ID format", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID format");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.warn("GET request to delete endpoint - redirecting to car list");
        resp.sendRedirect(req.getContextPath() + "/cars");
    }
}

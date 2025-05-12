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
import java.util.List;

@WebServlet(urlPatterns = {"/cars", "/cars/"})
public class ListCarsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ListCarsServlet.class);
    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
        logger.info("ListCarsServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Handling request to list all cars");
        List<Car> cars = carDAO.getAll();
        req.setAttribute("cars", cars);
        req.getRequestDispatcher("/WEB-INF/views/cars.jsp").forward(req, resp);
    }
}

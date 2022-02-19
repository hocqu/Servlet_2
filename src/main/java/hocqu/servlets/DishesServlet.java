package hocqu.servlets;

import com.google.gson.Gson;
import hocqu.entities.Dish;
import hocqu.entities.Product;
import hocqu.repo.DishRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DishesServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        PrintWriter pw = resp.getWriter();
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        String[] enteredIngredients = req.getParameterValues("ingr");
        String[] productParameters;
        String delimiter = ",";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        ArrayList<Product> allProducts = new ArrayList<>();
        for (String ingredient : enteredIngredients) {
            productParameters = ingredient.split(delimiter);
            Date formatedDate = null;
            if (productParameters.length == 1) {
                formatedDate = new Date();
            } else {
                try {
                    formatedDate = format.parse(productParameters[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (formatedDate != null) {
                Calendar formatedCalendar = Calendar.getInstance();
                formatedCalendar.setTime(formatedDate);
                Product product = new Product(productParameters[0], formatedCalendar);
                allProducts.add(product);
            }
        }
        DishRepo dishRepo = null;
        try {
            dishRepo = new DishRepo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Dish> dishes = dishRepo.findAll();
        Set<Dish> avaibleDishes = new HashSet<>();
        String[] usedProducts;
        for (Dish dish : dishes) {
            boolean avaible = true;
            usedProducts = dish.getComposition().split(delimiter);
            int countOfMatches = 0;
            for (String usedProduct : usedProducts) {
                for (Product personalProduct : allProducts) {
                    if (usedProduct.toLowerCase().equals(personalProduct.getName().toLowerCase()))
                        countOfMatches++;
                }
            }
            if (countOfMatches == usedProducts.length) {
                avaibleDishes.add(dish);
            }
        }

        if (pathInfo.equals("/avaible")) {
            for (Dish dish : avaibleDishes) {
                pw.println(gson.toJson(dish));
            }
        } else {
            if (pathInfo.equals("/expired")) {
                Calendar expireTime = Calendar.getInstance();
                expireTime.add(Calendar.DAY_OF_MONTH, 3);
                Map<Dish, Integer> countOfExpiredProduct = new HashMap<>();
                for (Product product : allProducts) {
                    if (product.getExpirationDate().before(expireTime) && product.getExpirationDate().after(Calendar.getInstance())) {
                        for (Dish dish : avaibleDishes) {
                            int count = 1;
                            usedProducts = dish.getComposition().split(delimiter);
                            for (String name : usedProducts) {
                                if (name.toLowerCase().equals(product.getName())) {
                                    if (countOfExpiredProduct.get(dish) != null) {
                                        count = countOfExpiredProduct.get(dish);
                                        count++;
                                    }
                                    countOfExpiredProduct.put(dish, count);
                                }
                            }
                        }
                    }
                }
                int maxCountOfExpiredProduct = 0;
                for (Map.Entry<Dish, Integer> dishesWithCount : countOfExpiredProduct.entrySet()) {
                    if (dishesWithCount.getValue() > maxCountOfExpiredProduct) {
                        maxCountOfExpiredProduct = dishesWithCount.getValue();
                    }
                }
                for (Map.Entry<Dish, Integer> dishesWithCount : countOfExpiredProduct.entrySet()) {
                    if (dishesWithCount.getValue() == maxCountOfExpiredProduct) {
                        pw.println(gson.toJson(dishesWithCount.getKey()));
                    }
                }
            }
        }
    }
}

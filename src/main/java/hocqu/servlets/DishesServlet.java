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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        PrintWriter pw = resp.getWriter();
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        String[] enteredIngredients = req.getParameterValues("ingr");
        ArrayList<Product> listOfAllProducts=createDishList(enteredIngredients);
        DishRepo dishRepo = null;
        try {
            dishRepo = new DishRepo();
        } catch (SQLException e) {
            log("Database isn't available");
        }
        List<Dish> avaibleDishes = dishRepo.findAvaibleDishes(enteredIngredients);
        String[] usedProducts;
        if (pathInfo.equals("/avaible")) {
            for (Dish dish : avaibleDishes) {
                pw.println(gson.toJson(dish));
            }
        } else {
            if (pathInfo.equals("/expired")) {
                Map<Dish, Integer> countOfExpiredProduct=findCountOfExpiredProductInDishes(listOfAllProducts,avaibleDishes);
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

    private Map<Dish, Integer> findCountOfExpiredProductInDishes(ArrayList<Product> allProducts, List<Dish> dishesAvailableToCooking) {
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.DAY_OF_MONTH, 3);
        Map<Dish, Integer> countOfExpiredProduct = new HashMap<>();
        for (Product product : allProducts) {
            if (product.getExpirationDate().before(expireTime) && product.getExpirationDate().after(Calendar.getInstance())) {
                for (Dish dish : dishesAvailableToCooking) {
                    int count = 1;
                    String[] usedProducts = dish.getComposition().split(",");
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
        return countOfExpiredProduct;
    }

    private ArrayList<Product> createDishList(String[] ingredients) {
        ArrayList<Product> allProducts = new ArrayList<>();
        String[] productParameters;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for (String ingredient : ingredients) {
            productParameters = ingredient.split(",");
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
        return allProducts;
    }
}

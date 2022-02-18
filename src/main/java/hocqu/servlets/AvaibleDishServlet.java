package hocqu.servlets;

import com.google.gson.Gson;
import hocqu.entities.Dish;
import hocqu.entities.Model;
import hocqu.repo.DishRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

public class AvaibleDishServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        String[] enteredIngridients = req.getParameterValues("ingr");
        PrintWriter pw=resp.getWriter();
        DishRepo dishRepo = null;
        try {
            dishRepo=new DishRepo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Dish> dishes = dishRepo.findAll();
        Set<Dish> avaibleDishes=new HashSet<>();
        String[] subStr;
        String delimiter = ",";
        for (int i = 0; i < enteredIngridients.length; i++) {
            for (Dish dish : dishes) {
                subStr=dish.getComposition().split(delimiter);
                for (String name : subStr) {
                    if (name.toLowerCase().equals(enteredIngridients[i])) {
                        avaibleDishes.add(dish);
                    }
                }
            }
        }
        for(Dish dish : avaibleDishes){
            pw.println(gson.toJson(dish));
        }
    }
}

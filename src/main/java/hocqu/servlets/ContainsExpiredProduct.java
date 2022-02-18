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

public class ContainsExpiredProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        resp.setContentType("application/json");
        String[] enteredIngredients = req.getParameterValues("ingr");
        String[] productParameters;
        String delimiter = ",";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        ArrayList<Product> allProducts=new ArrayList<>();
        for(String ingredient : enteredIngredients) {
            productParameters = ingredient.split(delimiter);
            Date dDate = null;
            try {
                dDate = format.parse(productParameters[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(!(dDate==null)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dDate );
                Product product = new Product(productParameters[0], calendar);
                allProducts.add(product);
            }
        }
        PrintWriter pw=resp.getWriter();
        DishRepo dishRepo = null;
        try {
            dishRepo=new DishRepo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] ingredientsInDish;
        List<Dish> dishes = dishRepo.findAll();
       Calendar expireTime=Calendar.getInstance();
       expireTime.add(Calendar.DAY_OF_MONTH,3);
        Map<Dish,Integer> countOfExpiredProduct=new HashMap<>();
       for (Product product : allProducts){
           if(product.getExpirationDate().before(expireTime) && product.getExpirationDate().after(Calendar.getInstance())){
               for(Dish dish : dishes){
                   int count=1;
                   ingredientsInDish=dish.getComposition().split(delimiter);
                   for (String name : ingredientsInDish) {
                   if(name.toLowerCase().equals(product.getName())){
                       if(!(countOfExpiredProduct.get(dish)==null)){
                       count=countOfExpiredProduct.get(dish);
                       count++;}
                       countOfExpiredProduct.put(dish,count);
                   }
                   }
               }
           }
       }
       int maxCountOfExpiredProduct=0;
       for(Map.Entry<Dish,Integer> dishesWithCount : countOfExpiredProduct.entrySet()){
           if(dishesWithCount.getValue()>maxCountOfExpiredProduct){
               maxCountOfExpiredProduct=dishesWithCount.getValue();
           }
       }
        for(Map.Entry<Dish,Integer> dishesWithCount : countOfExpiredProduct.entrySet()){
            if(dishesWithCount.getValue() == maxCountOfExpiredProduct){
                pw.println(gson.toJson(dishesWithCount.getKey()));
            }
        }
    }

}

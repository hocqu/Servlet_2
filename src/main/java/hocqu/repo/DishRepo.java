package hocqu.repo;

import hocqu.entities.Dish;
import hocqu.entities.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DishRepo extends BaseRepo {
    public DishRepo() throws SQLException {
    }

    public List<Dish> findAll() {
        List<Dish> dishes = new ArrayList<>();
        Dish dish = null;
        try {
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM dish");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dish = new Dish(rs.getString(2), rs.getString(3), rs.getString(4));
                dishes.add(dish);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return dishes;
    }

    public List<Dish> findAvaibleDishes(String[] productsWithExpiredDate) {
        List<Dish> dishes = new ArrayList<>();
        Dish dish = null;
        String request = "select * from dish where";
        List<String> products = new ArrayList<String>();
        for (String productWithDate : productsWithExpiredDate) {
            products.add(productWithDate.split(",")[0]);
        }
        for (int i = 0; i < productsWithExpiredDate.length; i++) {
            if (i == 0) {
                request = request + " (ingridients like '%" + products.get(i) + "%')";
            } else {
                request = request + " or (ingridients like '%" + products.get(i) + "%')";
            }
        }
        try {
            PreparedStatement pr = connection.prepareStatement(request);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                if (products.containsAll(Arrays.asList(rs.getString(3).split(",")))) {
                    dishes.add(new Dish(rs.getString(2), rs.getString(3), rs.getString(4)));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return dishes;
    }
}

package hocqu.repo;

import hocqu.entities.Dish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishRepo extends BaseRepo{
    public DishRepo() throws SQLException {
    }
    public List<Dish> findAll() {
        List<Dish> dishes = new ArrayList<>();
        Dish dish = null;
        try {
            PreparedStatement pr = connection.prepareStatement("SELECT * FROM dish");
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                dish = new Dish(rs.getString(2),rs.getString(3),rs.getString(4));
                dishes.add(dish);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return dishes;
    }
}

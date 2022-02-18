package hocqu.servlets;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        DishRepo dishRepo = null;
        try {
            dishRepo=new DishRepo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Dish> dishes = dishRepo.findAll();
        Model m1 = (Model) req.getServletContext().getAttribute("models");
        PrintWriter pw = resp.getWriter();
        HashMap<String,String> receipts=new HashMap<>();
        String[] subStr;
        String delimeter = ",";
        for (int i = 0; i < m1.size(); i++) {
            String prod = m1.get(i);
            Boolean printDish = false;
            // pw.println("With "+prod+":");
            for (Dish dish : dishes) {
                subStr=dish.getComposition().split(delimeter);
                String dishName = "empty";
                for (String name : subStr) {
                    if (name.toLowerCase().equals(prod)) {
                        String contain=receipts.get(dish.getName());
                        if(contain==null){
                            contain="";
                        }
                        contain=contain+" "+prod;
                        receipts.put(dish.getName(),contain);
                    }
                }
            }
        }
        pw.println("<HTML><HEAD><TITLE>");
        pw.println("</TITLE></HEAD><BODY>");
        pw.println("<table>");
        pw.println("<tr>");
        pw.println("<td>Ingredients</td>");
        pw.println("<td>Name</td>");
        pw.println("<td>Receipt</td>");
        pw.println("</tr>");
        for (Map.Entry<String, String> pair: receipts.entrySet())
        {
            String receipt="empty";
            for (int i = 0; i < dishes.size(); i++) {
                if(dishes.get(i).getName().equals(pair.getKey())){
        receipt=dishes.get(i).getRecipe();}
            }
            pw.println("<tr>");
            pw.println("<td>"+pair.getValue()+"</td>");
            pw.println("<td>"+pair.getKey()+"</td>");
            pw.println("<td>"+receipt+"<br></td>");
            pw.println("</tr>");
        }
        pw.println("</table>");
        pw.println("</BODY></HTML>");
    }
}
package hocqu.entities;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private static Model instance = new Model();

    private List<String> model;

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        model = new ArrayList<>();
    }

    public void add(String name) {
        model.add(name);
    }

    public String get(int i) {
        return model.get(i);
    }

    public int size() {
        return model.size();
    }

    @Override
    public String toString() {
        return "Model{" +
                "model=" + model +
                '}';
    }

    public void delete(int id) {
        model.remove(id);
    }

    public void clear() {
        model.clear();
    }

    public boolean find(String name) {
        return model.contains(name.toLowerCase());
    }
}
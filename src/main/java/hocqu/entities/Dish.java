package hocqu.entities;

import java.util.Objects;

public class Dish {
    private String name;
    private String composition;
    private String recipe;

    public Dish(String name,String composition,String recipe) {
        this.name = name;
        this.composition = composition;
        this.recipe = recipe;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}

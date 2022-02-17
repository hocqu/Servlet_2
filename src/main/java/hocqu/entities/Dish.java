package hocqu.entities;

public class Dish {
    private String composition;
    private String name;
    private String receipt;

    public Dish(String name,String composition,String receipt) {
        this.name = name;
        this.composition = composition;
        this.receipt = receipt;
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

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}

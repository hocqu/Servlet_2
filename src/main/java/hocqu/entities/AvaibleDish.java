package hocqu.entities;

public class AvaibleDish extends Dish {
    private String avaibleProducts;

    public AvaibleDish(String avaibleProducts, String name, String composition, String receipt) {
        super(name, composition, receipt);
        this.avaibleProducts = avaibleProducts;
    }

    public String getAvaibleProducts() {
        return avaibleProducts;
    }
}

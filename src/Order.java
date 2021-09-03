import java.util.ArrayList;
import java.util.List;

public class Order {
    List<Purchase> purchases = new ArrayList<>();
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

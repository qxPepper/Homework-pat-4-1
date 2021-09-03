public class CreateOrder extends AddPosition {
    private Order order;

    public CreateOrder(Order order) {
        super(order);
        order = new Order();
        order.setTotal(0);
    }
}


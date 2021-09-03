public class AddPosition implements OperateOrder {
    private Order order;

    public AddPosition(Order order) {
        this.order = order;
    }

    @Override
    public Order operate(String name, int price, int quantity) {
        int sumPrice = price * quantity;

        order.purchases.add(new Purchase(name, price, quantity, sumPrice));
        order.setTotal(order.getTotal() + sumPrice);

        return order;
    }
}
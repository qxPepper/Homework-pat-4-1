public class ChangeQuantity extends AddPosition {
    private Order order;

    public ChangeQuantity(Order order) {
        super(order);
        this.order = order;
    }

    @Override
    public Order operate(String name, int price, int quantity) {
        OperatePosition operatePosition = new DeletePosition(order);
        operatePosition.operatePosition(name);

        super.operate(name, price, quantity);

        return order;
    }
}

public class DeletePosition implements OperatePosition {
    private Order order;

    public DeletePosition(Order order) {
        this.order = order;
    }

    @Override
    public Order operatePosition(String name) {
        int index = Main.getIndex(order.purchases, name);
        if (index >= 0) {
            order.setTotal(order.getTotal() - order.purchases.get(index).sumPrice());
            order.purchases.remove(index);
        }
        return order;
    }
}

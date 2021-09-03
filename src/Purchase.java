public record Purchase(
        String name,
        int price,
        int quantity,
        int sumPrice) {

    @Override
    public String toString() {
        return String.format("%-20s | %-20s | %-20s | %-20s",
                name, price, quantity, sumPrice);
    }
}

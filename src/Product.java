public record Product(
        String name,
        int price,
        String manufacturer,
        int rating) {

    @Override
    public String toString() {
        return String.format("%-20s | %-20s | %-20s | %-20s",
                name, price, manufacturer, rating);
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) {
        String input;
        String value;
        String[] data;
        Predicate<? super Product> predicate = p -> p.name().equals("Продукт");

        Scanner scanner = new Scanner(System.in);
        Order order = new Order();
        List<Product> products = new ArrayList<>();

        final int NUMBER_OF_FIELDS = 4;
        final int NUMBER_OF_RECOMMENDATIONS = 3;

        products.add(new Product("Хлеб", 65, "совхоз Ленина", 5));
        products.add(new Product("Яблоки", 120, "совхоз Ленина", 4));
        products.add(new Product("Молоко", 90, "Простоквашино", 4));
        products.add(new Product("Мясо", 350, "совхоз Ленина", 3));
        products.add(new Product("Масло", 240, "Простоквашино", 5));
        products.add(new Product("Огурцы", 180, "Луховицы", 3));
        products.add(new Product("Творог", 250, "Простоквашино", 3));
        products.add(new Product("Горох", 89, "Луховицы", 4));

        System.out.println("""
                1 - Показать доступные товары.
                2 - Простая рекомендательная система для покупок.
                3 - Сформировать заказ.
                4 - Изменить заказ - добавить новую позицию.
                5 - Изменить заказ - удалить одну позицию.
                6 - Изменить заказ - изменить количество одного товара в заказе.
                0 - Выход.
                """);

        while (true) {
            input = scanner.nextLine();
            if (input.matches("(\\d|\\d{2})")) {

                if (Integer.parseInt(input) == 0) {
                    break;
                }

                switch (Integer.parseInt(input)) {
                    case 1 -> {
                        System.out.println("Ассортимент товаров.");
                        showGoods(products, "Производитель", "Рейтинг товаров");
                        System.out.println("Может примеры фильтров? - да/нет");
                        value = scanner.nextLine();
                        if (value.equals("да")) {
                            System.out.println("""
                                    Выберите:
                                    1 - по названию, к примеру 'Масло'
                                    2 - по цене, к примеру 'всё меньше 200'
                                    3 - по производителю, к примеру 'Простоквашино'
                                    4 - по рейтингу, к примеру 'равно 4'
                                    """);

                            value = scanner.nextLine();
                            if (value.matches("(\\d)")) {
// NOT MAGIC
                                if ((Integer.parseInt(value) >= 1) &&
                                        (Integer.parseInt(value) <= NUMBER_OF_FIELDS)) {
                                    switch (Integer.parseInt(value)) {
                                        case 1 -> predicate = p -> p.name().equals("Масло");
                                        case 2 -> predicate = p -> p.price() < 200;
                                        case 3 -> predicate = p -> p.manufacturer().equals("Простоквашино");
                                        case 4 -> predicate = p -> p.rating() == 4;
                                    }
                                    products.stream().filter(predicate).forEach(System.out::println);
                                }
                            } else {
                                System.out.println("Попробуйте ещё раз!");
                            }
                        }
                        System.out.println("...вышли в главное меню.");
                    }
                    case 2 -> {
                        System.out.println("Рекомендации.");
                        System.out.println("""
                                Выберите пожелания к качеству продуктов:
                                1 - Рекомендуется.
                                2 - В принципе можно.
                                3 - Не желательно.
                                """);

                        value = scanner.nextLine();
                        if (value.matches("(\\d)")) {
                            if ((Integer.parseInt(value) >= 1) &&
                                    (Integer.parseInt(value) <= NUMBER_OF_RECOMMENDATIONS)) {
                                predicate = switch (Integer.parseInt(value)) {
                                    case 1 -> p -> p.rating() == 5;
                                    case 2 -> p -> p.rating() == 4;
                                    case 3 -> p -> p.rating() <= 3;
                                    default -> predicate;
                                };
                                products.stream().filter(predicate).forEach(System.out::println);
                            }
                        }
                        System.out.println("...вышли в главное меню.");
                    }
                    case 3 -> {
//SOLID - 'D' зависим от абстракции, интерфейса OperateOrder, а реализацию CreateOrder можно менять
                        OperateOrder createOrder = new CreateOrder(order);
                        System.out.println("Сформируем заказ.");

                        while (true) {
// Don’t Repeat Yourself
                            data = dataInput(products, scanner);
                            order = createOrder.operate(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]));

                            System.out.println("Закончить формирование заказа?");
                            value = scanner.nextLine();
                            if (value.equals("да")) {
                                break;
                            }
                        }
                        printPurchases(order);
                    }
                    case 4 -> {
//SOLID - 'S' класс AddPosition только для добавления позиции
//SOLID - 'I' есть 2 интерфейса OperateOrder и OperatePosition для разных целей, а не 1
                        OperateOrder addPosition = new AddPosition(order);
                        System.out.println("Добавим новую позицию.");

                        data = dataInput(products, scanner);
                        order = addPosition.operate(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                        printPurchases(order);
                    }
                    case 5 -> {
//SOLID - 'S' класс DeletePosition только для удаления позиции
//SOLID - 'I' есть 2 интерфейса OperateOrder и OperatePosition для разных целей, а не 1
                        OperatePosition deletePosition = new DeletePosition(order);
                        System.out.println("Удалим одну позицию.");
                        System.out.println("Введите наименование продукта:");

                        String name = scanner.nextLine();
                        order = deletePosition.operatePosition(name);
                        printPurchases(order);
                    }
                    case 6 -> {
//SOLID - 'O' класс AddPosition не изменяем, а расширяем наследником ChangeQuantity
//SOLID - 'L' класс ChangeQuantity в принципе может сыграть роль предка AddPosition
                        OperateOrder changeQuantity = new ChangeQuantity(order);
                        System.out.println("Изменим количество одного товара в заказе.");

                        data = dataInput(products, scanner);
                        order = changeQuantity.operate(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                        printPurchases(order);
                    }
                    default -> System.out.println("Некорректный ввод. Попробуйте ещё раз!");
                }
            } else {
                System.out.println("Некорректный ввод. Попробуйте ещё раз!");
            }
        }
    }

    public static int defPrice(List<Product> products, String name) {
        int price = 0;
        for (Product product : products) {
            if (product.name().equals(name)) {
                price = product.price();
                break;
            }
        }
        return price;
    }

    static String[] dataInput(List<Product> products, Scanner scanner) {
        String[] str = new String[3];

        System.out.println("Введите наименование продукта:");
        str[0] = scanner.nextLine();

        str[1] = String.valueOf(defPrice(products, str[0]));

        System.out.println("Количество выбранного продукта:");

        String input = "";
        while (true) {
            input = scanner.nextLine();
            if (input.matches("(\\d{1,9})")) {
                break;
            } else {
                System.out.println("Неправильный ввод. Должно быть положительное число не более 9 цифр длиной.");
            }
        }
        str[2] = input;

        return str;
    }

    static int getIndex(List<Purchase> list, String name) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name().equals(name)) {
                index = i;
                break;
            }
        }
        return index;
    }

    static void showGoods(List<?> showList, String tail1, String tail2) {
        String Headlines = String.format("%-20s | %-20s | %-20s | %-20s",
                "Наименование", "Цена за ед.", tail1, tail2);
        System.out.println(Headlines);
        System.out.println("---------------------+----------------------+" +
                "----------------------+----------------");
        showList.forEach(System.out::println);
    }

    static void printPurchases(Order order) {
        System.out.println();
        System.out.println("Заказ.");
        showGoods(order.purchases, "Количество", "Суммарная цена");
        System.out.println("ИТОГО к оплате: " + order.getTotal());
    }
}


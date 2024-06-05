package Uebung9.Food;

public class IceBox {
    private Food[] table;
    private int size;
    private int capacity;

    public IceBox(int capacity) {
        this.capacity = capacity;
        this.table = new Food[capacity];
        this.size = 0;
    }

    public void add(Food obj) {
        int index = Math.floorMod(obj.hashCode(), capacity);
        while (table[index] != null) {
            index = Math.floorMod(index + 1, capacity);
        }
        table[index] = obj;
        size++;
    }

    public boolean contains(Food obj) {
        int index = Math.floorMod(obj.hashCode(), capacity);
        while (table[index] != null) {
            if (table[index].equals(obj)) {
                return true;
            }
            index = Math.floorMod(index + 1, capacity);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IceBox{");
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                sb.append(table[i].toString());
                if (i < capacity - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        IceBox iceBox = new IceBox(20); // Setting capacity to 20 for simplicity

        Food[] foods = {
                new Food("Fruit", "Apple"),
                new Food("Fruit", "Banana"),
                new Food("Vegetable", "Carrot"),
                new Food("Dairy", "Milk"),
                new Food("Meat", "Chicken"),
                new Food("Fish", "Salmon"),
                new Food("Grain", "Rice"),
                new Food("Drink", "Juice"),
                new Food("Snack", "Chips"),
                new Food("Dessert", "Cake")
        };

        for (Food food : foods) {
            iceBox.add(food);
        }

        System.out.println("Contents of the IceBox:");
        System.out.println(iceBox);

        System.out.println("Does IceBox contain Apple? " + iceBox.contains(new Food("Fruit", "Apple")));
        System.out.println("Does IceBox contain Banana? " + iceBox.contains(new Food("Fruit", "Banana")));
        System.out.println("Does IceBox contain Orange? " + iceBox.contains(new Food("Fruit", "Orange")));
    }
}

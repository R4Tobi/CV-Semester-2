package Uebung9.Food;

import java.util.Objects;

/**
 * The Food class represents a food item with a category and a name.
 */
public class Food {
    private String category;
    private String name;

    /**
     * Constructs a new Food object with the specified category and name.
     *
     * @param category the category of the food
     * @param name     the name of the food
     */
    public Food(String category, String name) {
        this.category = category;
        this.name = name;
    }

    /**
     * Returns the hash code value for this Food object.
     *
     * @return the hash code value for this Food object
     */
    @Override
    public int hashCode() {
        // Start with a non-zero constant prime number
        int result = 17;

        // Multiply result by 31 (another prime) and add the hash code of each significant field
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this Food object.
     *
     * @return a string representation of this Food object
     */
    @Override
    public String toString() {
        return "Food{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Food food = (Food) obj;
        return (Objects.equals(category, food.category)) && (Objects.equals(name, food.name));
    }
}

package it.polito.mad_lab4.data.restaurant;

/**
 * Created by Giovanna on 09/05/2016.
 */
public class ReviewFood {

    private Object food;
    private int position;
    private int section;
    private float rating;


    public ReviewFood(Object food, int position, int section, float rating) {
        this.food = food;
        this.position = position;
        this.section = section;
        this.rating = rating;
    }

    public Object getFood() {
        return food;
    }

    public void setFood(Object food) {
        this.food = food;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

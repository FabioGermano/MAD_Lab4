package it.polito.mad_lab3.data.restaurant;

import java.util.ArrayList;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Restaurant {

    public Restaurant(int restaurantId, String restaurantName){
        this.userPhotos = new ArrayList<UserPhoto>();
        this.dishes = new ArrayList<Dish>();
        this.offers = new ArrayList<Offer>();
        this.reviews = new ArrayList<Review>();
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    private BasicInfo basicInfo;

    private ArrayList<UserPhoto> userPhotos;

    private ArrayList<Dish> dishes;

    private ArrayList<Offer> offers;

    private ArrayList<Review> reviews;

    private int restaurantId;

    private String restaurantName;

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    private int numReviews;

    public void addUserPhoto(UserPhoto userPhoto) {
        this.userPhotos.add(userPhoto);
    }

    public void addDishe(Dish dish) {
        this.dishes.add(dish);
    }

    public void addOffer(Offer offer) {
        this.offers.add(offer);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }
    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public ArrayList<UserPhoto> getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(ArrayList<UserPhoto> userPhotos) {
        this.userPhotos = userPhotos;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public ArrayList<Dish> getDishesOfCategory(DishType dishType){
        ArrayList<Dish> filtered = new ArrayList<Dish>();
        for(Dish d : this.dishes){
            if(d.getEnumType() == dishType){
                filtered.add(d);
            }
        }

        return filtered;
    }

    public int getAvgPrice(){
        int totPrice = 0;

        for(Dish d: this.dishes){
            totPrice += d.getPrice();
        }

        return totPrice / dishes.size();
    }

    public float getAvgReview(){
        float totReview = 0;

        for (Review r: this.reviews){
            totReview += r.getRank();
        }

        return totReview / reviews.size();
    }

    public int getNumReviews() {
        return this.numReviews;
    }
}

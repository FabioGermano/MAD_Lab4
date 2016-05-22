package it.polito.mad_lab4.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");

        /*Restaurant restaurant = new Restaurant();
        restaurant.setAddress("Via Germasscsa, 32");
        restaurant.setBancomat(true);
        restaurant.setCity("Torino");
        restaurant.setDescription("sdfdfsfsdfsdfdf sd fsd f");
        restaurant.setDistance(54);
        restaurant.setEmail("gefasio@gmail.com");
        restaurant.setNumDishesAndOffers(0);
        restaurant.setNumReviews(0);
        restaurant.setPhone("346-887545");
        restaurant.setReservations(true);
        restaurant.setRestaurantName("Peruviano");
        restaurant.setTotRanking(0);
        ArrayList<String> tmes = new ArrayList<String>();
        tmes.add("10:20 - 14:30");
        tmes.add("10:20 - 14:30");
        tmes.add("10:20 - 14:30");
        tmes.add("10:20 - 14:30");
        tmes.add("10:20 - 14:30");
        tmes.add("10:20 - 14:30");
        tmes.add("10:20 - 14:30");
        restaurant.setTimeTable(tmes);

        String key = myRef.push().getKey();
        restaurant.setRestaurantId(key);
        myRef.child(key).setValue(restaurant);*/

        myRef = database.getReference("restaurants/-KIInPY8YJc4zpP8UM0m");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant r = dataSnapshot.getValue(Restaurant.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

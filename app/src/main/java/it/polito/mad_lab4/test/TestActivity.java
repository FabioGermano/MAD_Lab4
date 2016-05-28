package it.polito.mad_lab4.test;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
/*
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://mad-lab4-2ef30.appspot.com");
                StorageReference thumbname = storageRef.child("prova.jpg");


                    ImageView imageView = (ImageView) findViewById(R.id.iv);
                    // Get the data from an ImageView as bytes
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = thumbname.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });
*/
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("menu/-KIcTNUVIT-BIqARHq3P");
        Dish d = new Dish();
        d.setType(DishTypeConverter.fromEnumToString(DishType.MainCourses));
        d.setTodayAvailable(true);
        d.setSumRank(0);
        d.setNumRanks(0);
        d.setPrice((float)6.0);
        d.setDishName("Spaghetti alla carbonara");
        String key = myRef.push().getKey();
        d.setDishId(key);
        myRef.child(key).setValue(d);*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants");

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress("Via Monginevro, 32");
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
        myRef.child(key).setValue(restaurant);

        /*myRef = database.getReference("restaurants/-KIInPY8YJc4zpP8UM0m");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant r = dataSnapshot.getValue(Restaurant.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}

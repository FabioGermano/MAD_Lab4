package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveAvailabilityManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by Giovanna on 11/04/2016.
 */
public class EditAvailability extends EditableBaseActivity {
    private Oggetto_menu lista_menu = null;
    private ArrayList<Offer> lista_offerte = null;
    private boolean availability_mode=true;

    private BlankOfferFragment blankOfferFragment;
    private BlankMenuFragment[] fragments = new BlankMenuFragment[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_gestione_menu);
        setToolbarColor();
        hideToolbarShadow(true);
        setActivityTitle(getResources().getString(R.string.title_activity_edit_availability));
        setVisibilitySave(true);
        invalidateOptionsMenu();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        readMenu();
        readOffers();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        MyPageAdapter pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), EditAvailability.this);

        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

    }

    @Override
    protected User controlloLogin() {
        return null;
    }

    private void readOffers() {

        lista_offerte = new ArrayList<Offer>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("offers/" + "-KIrgaSxr9VhHllAjqmp");
        myRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef = database.getReference("offers/" + "-KIrgaSxr9VhHllAjqmp");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Offer o = dataSnapshot.getValue(Offer.class);
                lista_offerte.add(o);
                if(blankOfferFragment != null) {
                    blankOfferFragment.getAdapter().notifyItemInserted(lista_offerte.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readMenu() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("menu/" + "-KIrgaSxr9VhHllAjqmp");
        myRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.lista_menu = new Oggetto_menu();
        myRef = database.getReference("menu/" + "-KIrgaSxr9VhHllAjqmp");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Dish d = dataSnapshot.getValue(Dish.class);
                int index = DishTypeConverter.fromEnumToIndex(DishTypeConverter.fromStringToEnum(d.getType()));
                lista_menu.getDishListByIndex(index).add(d);
                if(fragments[index] != null){
                    fragments[index].getAdapter().notifyItemInserted(lista_menu.getDishListByIndex(index).size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int findPositionOnList(ArrayList<Dish> list, String dishId){
        int i = 0;
        for(Dish d : list){
            if(d.getDishId().equals(dishId)){
                return i;
            }
            i++;
        }

        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_save:
                saveInfo();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    void saveInfo() {
        new Thread()
        {
            public void run() {
                FirebaseSaveAvailabilityManager firebaseSaveAvailabilityManager = new FirebaseSaveAvailabilityManager();
                firebaseSaveAvailabilityManager.saveAvailability(lista_menu, lista_offerte);

                boolean res = firebaseSaveAvailabilityManager.waitForResult();

                if(!res){
                    Log.e("Error saving the avail.", "Error saving the avail.");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(getApplicationContext(), MainActivityManager.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        }.start();
    }

    private void printAlert(String msg){
        System.out.println(msg);
        AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
        miaAlert.setMessage(msg);

        //titolo personalizzato
        TextView title = new TextView(this);
        title.setText(getResources().getString(R.string.attenzione));
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        miaAlert.setCustomTitle(title);

        AlertDialog alert = miaAlert.create();
        alert.show();

        //centrare il messaggio
        TextView messageView = (TextView)alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    @Override
    protected void OnDeleteButtonPressed() {

    }

    @Override
    protected void OnEditButtonPressed() {

    }

    @Override
    protected void OnAddButtonPressed() {

    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private int NumOfPage = 5;

        String tabTitles[] = new String[] {getResources().getString(R.string.offers), getResources().getString(R.string.first), getResources().getString(R.string.second), getResources().getString(R.string.dessert),
                getResources().getString(R.string.other)};

        Context context;

        public MyPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }


        @Override
        public Fragment getItem(int position) {
            BlankMenuFragment menuFragment;
            Bundle bundle = new Bundle();
            bundle.putBoolean("availability", availability_mode);
            switch (position) {
                case 1:
                    // sono nei primi
                    fragments[0] = new BlankMenuFragment();
                    fragments[0].setArguments(bundle);
                    fragments[0].setValue(lista_menu.getPrimi(), DishType.MainCourses, this.context);
                    return fragments[0];
                case 2:
                    // sono nei secondi
                    fragments[1] = new BlankMenuFragment();
                    fragments[1].setArguments(bundle);
                    fragments[1].setValue(lista_menu.getSecondi(), DishType.SecondCourses, this.context);
                    return fragments[1];
                case 3:
                    // sono nei contorni
                    fragments[2] = new BlankMenuFragment();
                    fragments[2].setArguments(bundle);
                    fragments[2].setValue(lista_menu.getDessert(), DishType.Dessert, this.context);
                    return fragments[2];
                case 4:
                    // sono in altro
                    fragments[3] = new BlankMenuFragment();
                    fragments[3].setArguments(bundle);
                    fragments[3].setValue(lista_menu.getAltro(), DishType.Other, this.context);
                    return fragments[3];
                case 0:
                    blankOfferFragment = new BlankOfferFragment();
                    blankOfferFragment.setArguments(bundle);
                    blankOfferFragment.setValue(lista_offerte, this.context);
                    return blankOfferFragment;

            }

            return null;
        }

        @Override
        public int getCount() {
            return NumOfPage;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return getResources().getString(R.string.offers);
                case 1:
                    return getResources().getString(R.string.first);
                case 2:
                    return getResources().getString(R.string.second);
                case 3:
                    return getResources().getString(R.string.dessert);
                case 4:
                    return getResources().getString(R.string.other);
            }
            return null;
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(EditAvailability.this).inflate(R.layout.titolo_tab_pageradapter, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }

    }

    private void checkStoragePermission(){
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    finish();
                    startActivity(getIntent());

                } else {
                    printAlert("Negando i permessi l'app non funzionerà correttamente");

                }
                return;
            }
        }
    }
}

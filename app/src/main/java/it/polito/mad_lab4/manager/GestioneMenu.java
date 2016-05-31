package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseMenuListManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;

public class  GestioneMenu extends EditableBaseActivity {

    //private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private Oggetto_menu lista_menu = new Oggetto_menu();
    private boolean availability_mode=false;
    private BlankMenuFragment[] fragments = new BlankMenuFragment[4];
    private FirebaseMenuListManager firebaseMenuListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_menu);

        setToolbarColor();
        setActivityTitle(getResources().getString(R.string.title_activity_edit_menu));
        hideToolbarShadow(true);
        InitializeFABButtons(false, false, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        try {
            //readMenu();

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
            MyPageAdapter pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), GestioneMenu.this);
            if (viewPager != null) {
                viewPager.setAdapter(pagerAdapter);
            }

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);

            }
        } catch (NullPointerException e_null){
            System.out.println("Eccezione: " + e_null.getMessage());
        } catch (Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        readMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseMenuListManager.detachListeners();
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void readMenu() {
        lista_menu.clearAll();
        for(int i = 0; i<4; i++) {
            if(fragments[i] != null) {
                fragments[i].getAdapter().notifyDataSetChanged();
            }
        }

        showProgressBar();

        new Thread() {
            public void run() {

                firebaseMenuListManager = new FirebaseMenuListManager();
                firebaseMenuListManager.setFragments(fragments);
                firebaseMenuListManager.startGetList(lista_menu, "-KIrgaSxr9VhHllAjqmp");
                final boolean timeout = firebaseMenuListManager.waitForResult();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(timeout){
                            Snackbar.make(findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                                    .show();
                        }

                        dismissProgressDialog();
                    }
                });
            }
        }.start();
    }


    private void printAlertEmpty(){
        AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
        miaAlert.setMessage(getResources().getString(R.string.msgListaVuotaPiatti));

        //titolo personalizzato
        TextView title = new TextView(this);
        title.setText(getResources().getString(R.string.listaVuota));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivityManager.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void OnDeleteButtonPressed() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void OnEditButtonPressed() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void OnAddButtonPressed() {

        Bundle b = new Bundle();
        b.putString("restaurantId", "-KIrgaSxr9VhHllAjqmp");
        b.putBoolean("isEditing", false);
        Intent intent = new Intent(getApplicationContext(), ModifyMenuDish.class);
        intent.putExtras(b);
        startActivity(intent);
    }


    private class MyPageAdapter extends FragmentPagerAdapter {
        private int NumOfPage = 4;
        String tabTitles[] = new String[] { getResources().getString(R.string.first), getResources().getString(R.string.second), getResources().getString(R.string.dessert), getResources().getString(R.string.other)};
        Context context;

        public MyPageAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }


        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("availability", availability_mode);
            switch (position) {
                case 0:
                    // sono nei primi
                    fragments[0] = new BlankMenuFragment();
                    fragments[0].setArguments(bundle);
                    fragments[0].setValue(lista_menu.getPrimi(), DishType.MainCourses, this.context);
                    return fragments[0];
                case 1:
                    // sono nei secondi
                    fragments[1] = new BlankMenuFragment();
                    fragments[1].setArguments(bundle);
                    fragments[1].setValue(lista_menu.getSecondi(), DishType.SecondCourses, this.context);
                    return fragments[1];
                case 2:
                    // sono nei dessert
                    fragments[2] = new BlankMenuFragment();
                    fragments[2].setArguments(bundle);
                    fragments[2].setValue(lista_menu.getDessert(), DishType.Dessert, this.context);
                    return fragments[2];
                case 3:
                    // sono in altro
                    fragments[3] = new BlankMenuFragment();
                    fragments[3].setArguments(bundle);
                    fragments[3].setValue(lista_menu.getAltro(), DishType.Other, this.context);
                    return fragments[3];
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
                    return getResources().getString(R.string.first);
                case 1:
                    return getResources().getString(R.string.second);
                case 2:
                    return getResources().getString(R.string.dessert);
                case 3:
                    return getResources().getString(R.string.other);
            }
            return null;
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(GestioneMenu.this).inflate(R.layout.titolo_tab_pageradapter, null);
            //TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            //tv.setText(tabTitles[position]);
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
                    printAlert(getString(R.string.permission_denied_message));

                }
                return;
            }
        }
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



}


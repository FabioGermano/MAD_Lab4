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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;

public class  GestioneMenu extends EditableBaseActivity {

    //private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private Oggetto_menu lista_menu = null;
    private String fileName = "database";
    private JSONObject  jsonRootObject;
    private boolean availability_mode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);
        hideShadow(true);

        setContentView(R.layout.activity_gestione_menu);
        setToolbarColor();
        setTitleTextView(getResources().getString(R.string.title_activity_edit_menu));
        InitializeFABButtons(false, false, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        try {
            /*
            //recupero eventuali modifiche apportate ad un piatto
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lista_menu = (Oggetto_menu) extras.getSerializable("dish_list");
                extras.clear();

            } else {
                //altrimenti carico info dal server (o da locale)
                boolean ris = readData();
            }
            */

            readMenu();

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_menu);
            MyPageAdapter pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), GestioneMenu.this);
            if (viewPager != null) {
                viewPager.setAdapter(pagerAdapter);

                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    public void onPageScrollStateChanged(int arg0) {
                    }

                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    public void onPageSelected(int currentPage) {
                        //currentPage is the position that is currently displayed.
                        switch(currentPage){
                            case 0:
                                // sono nei primi
                                modificaSchermata(lista_menu.getPrimi());

                                break;
                            case 1:
                                // sono nei secondi
                                modificaSchermata(lista_menu.getSecondi());
                                break;
                            case 2:
                                // sono nei dessert
                                modificaSchermata(lista_menu.getDessert());
                                break;
                            case 3:
                                // sono in altro
                                modificaSchermata(lista_menu.getAltro());
                                break;

                        }
                    }

                });
            }

            // Give the TabLayout the ViewPager
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);

                // Iterate over all tabs and set the custom view
                /*for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) {
                        tab.setCustomView(pagerAdapter.getTabView(i));
                    }
                }*/
            }
        } catch (NullPointerException e_null){
            System.out.println("Eccezione: " + e_null.getMessage());
        } catch (Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private void readMenu() {
        this.lista_menu = new Oggetto_menu();
        ArrayList<Dish> dishes = RestaurantBL.getRestaurantById(getApplicationContext(), 1).getDishes();
        for(Dish d : dishes){
            if(d.getEnumType() == DishType.MainCourses){
                this.lista_menu.addPrimo(d);
            }
            if(d.getEnumType() == DishType.SecondCourses){
                this.lista_menu.addSecondo(d);
            }
            if(d.getEnumType() == DishType.Dessert){
                this.lista_menu.addDessert(d);
            }
            if(d.getEnumType() == DishType.Other){
                this.lista_menu.addAltro(d);
            }
        }
    }

    private void modificaSchermata(ArrayList<Dish> list){
        if(list.size() == 0){
            //printAlertEmpty();
            //resetAddButton();

        } else if(list.size() > 7) {
            //moveAddButton();
        }
        else{
            //resetAddButton();
        }
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

    /*private boolean readData(){
        try{
            Oggetto_piatto obj;
            lista_menu = new Oggetto_menu();


            GestioneDB DB = new GestioneDB();
            String db = DB.leggiDB(this, "db_menu");

            jsonRootObject = new JSONObject(db);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray arrayDebug = jsonRootObject.optJSONArray("lista_piatti");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < arrayDebug.length(); i++){
                JSONObject jsonObject = arrayDebug.getJSONObject(i);

                String nome = jsonObject.optString("nome");
                int prezzo = Integer.parseInt(jsonObject.optString("prezzo"));
                String type = jsonObject.optString("tipo");
                String thumb = jsonObject.optString("foto_thumb");
                String large = jsonObject.optString("foto_large");

                if (thumb.compareTo("null")== 0 || thumb.compareTo("") == 0)
                    thumb = null;
                if (large.compareTo("null")==0 || large.compareTo("") == 0)
                    large = null;

                //creo le differenti liste
                switch(type){
                    case "Primo":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.PRIMI);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setPhoto(thumb, large);
                        lista_menu.addPrimo(obj);
                        System.out.println("Aggiunto primo");
                        break;
                    case "Secondo":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.SECONDI);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setPhoto(thumb, large);
                        lista_menu.addSecondo(obj);
                        System.out.println("Aggiunto secondo");
                        break;
                    case "Dessert":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.DESSERT);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setPhoto(thumb, large);
                        lista_menu.addDessert(obj);
                        System.out.println("Aggiunto dessert");
                        break;
                    case "Altro":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.ALTRO);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setPhoto(thumb, large);
                        lista_menu.addAltro(obj);
                        System.out.println("Aggiunto altro");
                        break;
                    default:
                        System.out.println("Typology unknown");
                        break;
                }

            }
            return true;
        } catch (JSONException e)
        {
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
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
        b.putString("restaurantId", "-KIcTNUVIT-BIqARHq3P");
        b.putBoolean("isEditing", false);
        Intent intent = new Intent(getApplicationContext(), ModifyMenuDish.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    protected void OnSaveButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////prova pageAdapter/////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
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
            BlankMenuFragment menuFragment;
            Bundle bundle = new Bundle();
            bundle.putBoolean("availability", availability_mode);
            switch (position) {
                case 0:
                    // sono nei primi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, DishType.MainCourses, this.context);
                    return menuFragment;
                case 1:
                    // sono nei secondi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, DishType.SecondCourses, this.context);
                    return menuFragment;
                case 2:
                    // sono nei dessert
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, DishType.Dessert, this.context);
                    return menuFragment;
                case 3:
                    // sono in altro
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu, DishType.Other, this.context);
                    return menuFragment;

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

    @Override
    protected void OnCalendarButtonPressed() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void OnBackButtonPressed() {

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


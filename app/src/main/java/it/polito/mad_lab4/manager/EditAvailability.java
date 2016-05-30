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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by Giovanna on 11/04/2016.
 */
public class EditAvailability extends EditableBaseActivity {

    //private ArrayList<Oggetto_piatto> list_piatti = new ArrayList<>();
    private Oggetto_menu lista_menu = null;
    private String fileName = "database";
    private JSONObject jsonRootObject;
    private ArrayList<Offer> lista_offerte = null;
    private boolean availability_mode=true;

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

        lista_offerte = new ArrayList<Offer>();//RestaurantBL.getRestaurantById(getApplicationContext(), 1).getOffers();
    }

    private void readMenu() {
        this.lista_menu = new Oggetto_menu();
        ArrayList<Dish> dishes = new ArrayList<Dish>();//RestaurantBL.getRestaurantById(getApplicationContext(), 1).getDishes();
        for(Dish d : dishes){
            if(DishTypeConverter.fromStringToEnum(d.getType()) == DishType.MainCourses){
                this.lista_menu.addPrimo(d);
            }
            if(DishTypeConverter.fromStringToEnum(d.getType()) == DishType.SecondCourses){
                this.lista_menu.addSecondo(d);
            }
            if(DishTypeConverter.fromStringToEnum(d.getType()) == DishType.Dessert){
                this.lista_menu.addDessert(d);
            }
            if(DishTypeConverter.fromStringToEnum(d.getType()) == DishType.Other){
                this.lista_menu.addAltro(d);
            }
        }
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
        RestaurantBL.saveChanges(getApplicationContext());

        Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(getApplicationContext(), MainActivityManager.class);
        startActivity(intent);
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
            BlankOfferFragment offerFragment;
            Bundle bundle = new Bundle();
            bundle.putBoolean("availability", availability_mode);
            switch (position) {
                case 1:
                    // sono nei primi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu.getPrimi(), DishType.MainCourses, this.context);
                    return menuFragment;
                case 2:
                    // sono nei secondi
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu.getSecondi(), DishType.SecondCourses, this.context);
                    return menuFragment;
                case 3:
                    // sono nei contorni
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu.getDessert(), DishType.Dessert, this.context);
                    return menuFragment;
                case 4:
                    // sono in altro
                    menuFragment = new BlankMenuFragment();
                    menuFragment.setArguments(bundle);
                    menuFragment.setValue(lista_menu.getAltro(), DishType.Other, this.context);
                    return menuFragment;
                case 0:
                    offerFragment = new BlankOfferFragment();
                    offerFragment.setArguments(bundle);
                    offerFragment.setValue(lista_offerte, this.context);
                    return offerFragment;

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
    /*private boolean readMenuData(){
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

                String nome = jsonObject.optString("nome").toString();
                int prezzo = Integer.parseInt(jsonObject.optString("prezzo").toString());
                String type = jsonObject.optString("tipo").toString();
                boolean av = jsonObject.optBoolean("available");
                //creo le differenti liste
                switch(type){
                    case "Primo":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.PRIMI);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setAvailability(av);
                        obj.setTmpAv(av);
                        lista_menu.addPrimo(obj);
                        System.out.println("Aggiunto primo");
                        break;
                    case "Secondo":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.SECONDI);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setAvailability(av);
                        obj.setTmpAv(av);
                        lista_menu.addSecondo(obj);
                        System.out.println("Aggiunto secondo");
                        break;
                    case "Dessert":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.DESSERT);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setAvailability(av);
                        obj.setTmpAv(av);
                        lista_menu.addDessert(obj);
                        System.out.println("Aggiunto dessert");
                        break;
                    case "Altro":
                        obj = new Oggetto_piatto(nome, prezzo, Oggetto_piatto.type_enum.ALTRO);
                        obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                        obj.setAvailability(av);
                        obj.setTmpAv(av);
                        lista_menu.addAltro(obj);
                        System.out.println("Aggiunto altro");
                        break;
                    default:
                        System.out.println("Typology unknown");
                        break;
                }

                //lista_menu.setJson(jsonRootObject);

            }
            return true;
        } catch (JSONException e)
        {
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }
    private boolean readOfferData(){
        try{

            lista_offerte = new ArrayList<>();

            GestioneDB DB = new GestioneDB();
            String db = DB.leggiDB(this, "db_offerte");

            if (db != null){
                System.out.println("Leggo le offerte");
                jsonRootObject = new JSONObject(db);

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray arrayDebug = jsonRootObject.optJSONArray("lista_offerte");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < arrayDebug.length(); i++) {
                    JSONObject jsonObject = arrayDebug.getJSONObject(i);

                    String nome = jsonObject.optString("nome").toString();
                    int prezzo = Integer.parseInt(jsonObject.optString("prezzo").toString());
                    String note = jsonObject.optString("note".toString());
                    boolean av = jsonObject.optBoolean("available");

                    boolean[] days= new boolean[7];
                    days[0] = jsonObject.optBoolean("lun");
                    days[1] = jsonObject.optBoolean("mar");
                    days[2] = jsonObject.optBoolean("mer");
                    days[3] = jsonObject.optBoolean("gio");
                    days[4] = jsonObject.optBoolean("ven");
                    days[5] = jsonObject.optBoolean("sab");
                    days[6] = jsonObject.optBoolean("dom");


                    //creo la lista delle offerte

                    //<DA CONTROLLARE IN SEGUITO ALL'AGGIUNTA DEI GIORNI DELLE OFFERTE
                    Oggetto_offerta obj = new Oggetto_offerta(nome, prezzo, days);
                    obj.setId(Integer.parseInt(jsonObject.optString("id").toString()));
                    obj.setAvailability(av);
                    obj.setTmpAv(av);
                    obj.setNote(note);
                    lista_offerte.add(obj);
                    System.out.println("Offerta aggiunta");
                }
                if(lista_offerte.isEmpty())
                    System.out.println("La lista è vuota");
                return true;
            }
            else {
                return false;
            }

        }catch (JSONException e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        } catch (Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }*/

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

package it.polito.mad_lab4.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.user.User;


public class EditUserProfileActivity extends BaseActivity implements UniversityPickerFragment.OnSelectionListener{

    private EditText universityText, typeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        LinearLayout universityLL = (LinearLayout) findViewById(R.id.universityLL);
        universityText = (EditText) universityLL.findViewById(R.id.universityText);
        universityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUniversity();
            }
        });
        typeText = (EditText) findViewById(R.id.typeText);
        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickType();
            }
        });

        setActivityTitle(getResources().getString(R.string.edit_user_profile_activity_title));
        setToolbarColor();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        toolbar.inflateMenu(R.menu.action_bar);
        final MenuItem filter = menu.findItem(R.id.menu_calendar);
        filter.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                saveData();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected User controlloLogin() {
        return null;
    }

    @Override
    protected void filterButton() {

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void saveData() {
    }

    private void pickUniversity(){

        UniversityPickerFragment universityPickerFragment = UniversityPickerFragment.newInstance(null, false);
        universityPickerFragment.show(this.getFragmentManager(), "dialog");
    }
    private void pickType() {

        UniversityPickerFragment universityPickerFragment = UniversityPickerFragment.newInstance(getResources().getStringArray(R.array.userType), true);
        universityPickerFragment.show(this.getFragmentManager(), "dialog");
    }


    @Override
    public void updateUniversity(int university, String name) {
        if(university!=-1)
        this.universityText.setText(name);
    }

    @Override
    public void updateType(int n, String type) {
        if(n!=-1)
        this.typeText.setText(type);
    }
}

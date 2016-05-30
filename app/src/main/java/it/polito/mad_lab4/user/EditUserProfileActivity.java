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
import android.widget.Toast;

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
                showDialogUniversity();
            }
        });
        typeText = (EditText) findViewById(R.id.typeText);
        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogType();
            }
        });

        setActivityTitle(getResources().getString(R.string.edit_user_profile_activity_title));
        setToolbarColor();
        setVisibilitySave(true);
        invalidateOptionsMenu();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_save:
                Toast toast = Toast.makeText(getApplicationContext(), "Save pressed", Toast.LENGTH_SHORT);
                toast.show();
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

    private void showDialogType(){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show
        final String[] items = getResources().getStringArray(R.array.userType);

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.pick_type)).setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                typeText.setText(items[item]);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.getListView().setDividerHeight(1);
        dialog.show();
    }

    private void showDialogUniversity(){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show
        final String[] items = getResources().getStringArray(R.array.Polito);

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.pick_your_university)).setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                universityText.setText(items[item]);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.getListView().setDividerHeight(1);
        dialog.show();
    }
}

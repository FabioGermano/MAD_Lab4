package it.polito.mad_lab4.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseGetClientInfoManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUniversitiesManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveClientInfoManager;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.newData.client.ClientPersonalInformation;
import it.polito.mad_lab4.newData.other.University;


public class EditUserProfileActivity extends BaseActivity implements UniversityPickerFragment.OnSelectionListener{

    private EditText universityText, typeText;
    private int typeChoice =-1, universityChoice=-1;
    private boolean newClient = false;
    private ClientPersonalInformation client;
    private String[] universitiesList = null;
    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        newClient = false;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            newClient = (boolean) extras.getBoolean("new");
            userId = (String) extras.getString("userId");

            if (newClient){
                //utente nuovo deve compilare tutto
                String email = (String) extras.getString("email");
                String name = (String)extras.getString("name");
                setBackButtonVisibility(false);

                TextView msg = (TextView) findViewById(R.id.registration_message);
                msg.setVisibility(View.VISIBLE);

                //TODO setto email e nome + caricamento dialog università
                loadDataNewClient(name, email);



            }
            else
                loadData(userId);
            extras.clear();
        }
        else {
            Toast.makeText(EditUserProfileActivity.this, getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        LinearLayout universityLL = (LinearLayout) findViewById(R.id.universityLL);
        universityText = (EditText) universityLL.findViewById(R.id.universityText);
        universityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(universityChoice!=-1)
                    showDialogUniversity(universityChoice);
                else
                    showDialogUniversity(0);
            }
        });
        typeText = (EditText) findViewById(R.id.typeText);
        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeChoice!=-1)
                    showDialogType(typeChoice);
                else
                    showDialogType(0);
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
                saveData();
                break;
            default:
                break;
        }
        return true;
    }

    private void loadDataNewClient(final String name, final String email){
        new Thread()        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                ArrayList<String> universities;
                FirebaseGetUniversitiesManager uniManager = new FirebaseGetUniversitiesManager();
                uniManager.getUniversities();
                uniManager.waitForResult();
                universities = uniManager.getResult();
                universitiesList = universities.toArray(new String[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fillDataNewClient(name, email);
                        dismissProgressDialog();
                    }
                });
            }
        }.start();
    }

    public void fillDataNewClient(String name, String email) {
        EditText nameField = (EditText)findViewById(R.id.edit_name);
        EditText emailField = (EditText)findViewById(R.id.emailText);

        if (nameField == null || emailField == null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        if(name != null)
            nameField.setText(name);

        if (email != null)
            emailField.setText(email);

        PhotoViewer fragmentAvatar = (PhotoViewer) getSupportFragmentManager().findFragmentById(R.id.logo_fragment);
        if (fragmentAvatar != null)
            fragmentAvatar.setImageFromLink(getResources().getString(R.string.default_avatar));

    }


        private void loadData(final String id){
        new Thread()        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                FirebaseGetClientInfoManager clientManager = new FirebaseGetClientInfoManager();
                clientManager.getClientInfo(id);
                clientManager.waitForResult();
                client = clientManager.getResult();

                ArrayList<String> universities;
                FirebaseGetUniversitiesManager uniManager = new FirebaseGetUniversitiesManager();
                uniManager.getUniversities();
                uniManager.waitForResult();
                universities = uniManager.getResult();
                universitiesList = universities.toArray(new String[0]);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fillData();
                        dismissProgressDialog();
                    }
                });
            }
        }.start();


    }

    public void fillData(){

        if (client == null){
            Bundle b = new Bundle();
            b.putString("userId", userId);
            b.putBoolean("new", true);

            Intent i = new Intent(getApplicationContext(), EditUserProfileActivity.class);
            i.putExtras(b);
            startActivity(i);
        }
        else {
            String name = client.getName();
            String phone = client.getPhoneNumber();
            String tipo = client.getTipoUser();
            String id = client.getUniversityId();
            String sesso = client.getGender();
            String email = client.getEmail();
            String bio = client.getBio();
            String avatarDownloadLink = client.getAvatarDownloadLink();

            EditText nameField = (EditText) findViewById(R.id.edit_name);
            EditText typeField = (EditText) findViewById(R.id.typeText);
            EditText universityField = (EditText) findViewById(R.id.universityText);
            EditText phoneField = (EditText) findViewById(R.id.phoneText);
            EditText emailField = (EditText) findViewById(R.id.emailText);
            EditText bioField = (EditText) findViewById(R.id.bioText);
            RadioButton btn;

            if (nameField == null || emailField == null || typeField == null || universityField == null || phoneField == null || bioField == null) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            if (id != null) {
                universityChoice = Integer.parseInt(id);

                if (universitiesList.length <= universityChoice)
                    universityChoice = 0;

                universityField.setText(universitiesList[universityChoice]);
            }

            if (name != null)
                nameField.setText(name);

            if (phone != null)
                phoneField.setText(phone);

            if (email != null)
                emailField.setText(email);


            if (bio != null) {
                bioField.setText(bio);
            }

            if (tipo != null) {
                if (tipo.equalsIgnoreCase("student")) {
                    typeField.setText(R.string.student);
                    typeChoice = 0;
                } else if (tipo.equalsIgnoreCase("professor")) {
                    typeField.setText(R.string.professor);
                    typeChoice = 1;
                } else if (tipo.equalsIgnoreCase("employee")) {
                    typeField.setText(R.string.adm_employee);
                    typeChoice = 2;
                }
            } else
                typeField.setText(R.string.student);


            if (sesso != null) {
                if (sesso.compareTo("male") == 0) {
                    btn = (RadioButton) findViewById(R.id.male);
                    if (btn != null)
                        btn.setChecked(true);
                } else if (sesso.compareTo("female") == 0) {
                    btn = (RadioButton) findViewById(R.id.female);
                    if (btn != null)
                        btn.setChecked(true);
                } else {
                    btn = (RadioButton) findViewById(R.id.other);
                    if (btn != null)
                        btn.setChecked(true);
                }
            } else {
                btn = (RadioButton) findViewById(R.id.male);
                if (btn != null)
                    btn.setChecked(true);
            }

            if (avatarDownloadLink != null) {
                PhotoViewer fragmentAvatar = (PhotoViewer) getSupportFragmentManager().findFragmentById(R.id.logo_fragment);
                if (fragmentAvatar != null)
                    fragmentAvatar.setImageFromLink(avatarDownloadLink);
            }
        }

    }

    private void saveData() {
        EditText nameField = (EditText)findViewById(R.id.edit_name);
        EditText typeField = (EditText)findViewById(R.id.typeText);
        EditText universityField = (EditText)findViewById(R.id.universityText);
        EditText phoneField = (EditText)findViewById(R.id.phoneText);
        EditText emailField = (EditText)findViewById(R.id.emailText);
        EditText bioField = (EditText) findViewById(R.id.bioText);
        RadioGroup genderRadioGroup = (RadioGroup) findViewById(R.id.gender_RadioGroup);

        if (nameField == null || emailField == null || typeField == null || universityField == null || phoneField == null || bioField==null || genderRadioGroup==null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        String name = nameField.getText().toString();
        String type = typeField.getText().toString();
        String university = universityField.getText().toString();
        String phone = phoneField.getText().toString();
        String email = emailField.getText().toString();
        String bio = bioField.getText().toString();
        int checkedId = genderRadioGroup.getCheckedRadioButtonId();

        if (name.isEmpty() || type.isEmpty() || university.isEmpty() || phone.isEmpty() || email.isEmpty() || checkedId == -1){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_complete), Toast.LENGTH_LONG).show();
            return;
        }

        if (!isEmailValid(email)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_email_format), Toast.LENGTH_LONG).show();
            return;
        }


        final ClientPersonalInformation client = new ClientPersonalInformation();
        client.setName(name);
        client.setPhoneNumber(phone);
        client.setBio(bio);
        client.setUniversityId(String.valueOf(universityChoice));
        client.setEmail(email);

        if (type.compareTo(getResources().getString(R.string.student)) == 0){
            client.setTipoUser("student");
        }
        else if (type.compareTo(getResources().getString(R.string.professor)) == 0){
            client.setTipoUser("professor");
        }
        else if (type.compareTo(getResources().getString(R.string.adm_employee)) == 0){
            client.setTipoUser("employee");
        }

        switch(checkedId){
            case R.id.male:
                client.setGender("male");
                break;
            case R.id.female:
                client.setGender("female");
                break;
            case R.id.other:
                client.setGender("other");
                break;
        }

        //settare avatar link
        client.setAvatarDownloadLink(getResources().getString(R.string.default_avatar));

        //qui dovrei avere l'oggetto Client riempito con i dati dei campi

        new Thread()        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                FirebaseSaveClientInfoManager saveClientManager = new FirebaseSaveClientInfoManager(getApplicationContext());
                saveClientManager.saveClientInfo(client, userId, null);
                //saveClientManager.waitForResult();

                //TODO aggiungere firebaseSaveUsersManager per tenere i dati allineati

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        Toast.makeText(EditUserProfileActivity.this, getResources().getString(R.string.dataSaved), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                });
            }
        }.start();

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

    private void showDialogType(int i){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show
        final String[] items = getResources().getStringArray(R.array.userType);

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.pick_type)).setSingleChoiceItems(items, i, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                typeText.setText(items[item]);
                typeChoice=item;
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.getListView().setDividerHeight(1);
        dialog.show();
    }

    private void showDialogUniversity(int i){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show
        // Creating and Building the Dialog
        if (universitiesList != null){
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.pick_your_university)).setSingleChoiceItems(universitiesList, i, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int item) {
                    universityText.setText(universitiesList[item]);
                    universityChoice=item;
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.getListView().setDividerHeight(1);
            dialog.show();
        }
        else{
            Toast.makeText(EditUserProfileActivity.this, getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT).show();
        }




    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if (newClient){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You must complete all mandatory fields");
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}

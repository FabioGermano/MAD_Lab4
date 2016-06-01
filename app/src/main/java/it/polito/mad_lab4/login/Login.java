package it.polito.mad_lab4.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUniversitiesManager;
import it.polito.mad_lab4.newData.other.University;
import it.polito.mad_lab4.newData.user.User;
import it.polito.mad_lab4.user.EditUserProfileActivity;

public class Login extends BaseActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CardView defaultLogin;
    private CardView newClient;
    private CardView newManager;

    private ProgressDialog progressDialog = null;

    private TextView msgLoginFallito;

    private boolean alreadyNotified;
    private boolean registration;

    private String[] universitiesList = null;
    private Context context;
    private String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        setContentView(R.layout.activity_login);

        alreadyNotified = false;
        registration = false;

        setActivityTitle(getResources().getString(R.string.login_title));
        setToolbarColor();
        try {
            mAuth = FirebaseAuth.getInstance();

            // controllo se l'utente è già collegato:

            // imposto il listener per il cambiamento nello stato di autenticazione
            // quando il login ha successo torno alla home corrispondente dell'utente
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null && !alreadyNotified) {
                        // User is signed in
                        alreadyNotified = true;
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        if (registration) {
                            //arrivo qua dalla fase di registrazione
                            registration = false;
                            insertClientInfo(user.getUid());
                            String email = user.getEmail();
                            Bundle b = new Bundle();
                            b.putString("email", email);
                            b.putString("userId", user.getUid());
                            b.putString("name", username);
                            b.putBoolean("new", true);

                            Intent i = new Intent(getApplicationContext(), EditUserProfileActivity.class);
                            i.putExtras(b);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "authentication succeed", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            mAuth.removeAuthStateListener(mAuthListener);
                            startActivity(i);
                        }
                    }
                }
            };

            mAuth.addAuthStateListener(mAuthListener);

            defaultLogin = (CardView) findViewById(R.id.card_login);
            newClient = (CardView) findViewById(R.id.card_newAccountUtente);
            newManager = (CardView) findViewById(R.id.card_newAccountManager);

            msgLoginFallito = (TextView) findViewById(R.id.login_failed_message);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    public void eseguiLogin(View view) {
        try {
            final EditText passwordET = (EditText) findViewById(R.id.passwordET);
            final EditText usernameET = (EditText) findViewById(R.id.usernameET);

            //Eseguire controlli su stringhe
            if (passwordET != null && usernameET != null) {
                String userN = usernameET.getText().toString();
                String pass = passwordET.getText().toString();

                if (!userN.isEmpty() && !pass.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(usernameET.getText().toString(), passwordET.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        if(progressDialog != null){
                                            progressDialog.dismiss();
                                            progressDialog = null;
                                        }
                                        passwordET.setText("");
                                        usernameET.setText("");
                                        msgLoginFallito.setVisibility(View.VISIBLE);
                                        Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_complete), Toast.LENGTH_LONG).show();
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    return;
                }
            } else {
                if(progressDialog != null){
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

            // Attendo l'esito dell'operazione
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            // imposto un timeout per ogni evenienza
            timerDelayRemoveDialog(6000);

        } catch (Exception e){
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }


    //timeout attesa risposta dal server
    public void timerDelayRemoveDialog(long time){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(progressDialog != null) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.timeout_expired), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        }, time);
    }

    public void nuovaRegistrazione(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getResources().getString(R.string.user_type_registrazione))
                .setPositiveButton(getResources().getString(R.string.client_registrazione), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Login.this, "Client", Toast.LENGTH_SHORT).show();
                        defaultLogin.setVisibility(View.GONE);
                        newClient.setVisibility(View.VISIBLE);
                        setActivityTitle("Registrazione Cliente");
                    }
                })
                .setNegativeButton(getResources().getString(R.string.manager_registrazione), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Login.this, "Manager", Toast.LENGTH_SHORT).show();
                        defaultLogin.setVisibility(View.GONE);
                        newManager.setVisibility(View.VISIBLE);
                        setActivityTitle("Registrazione Ristorante");

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void eseguiNuovaRegistrazioneUtente(View view) {
        final EditText nameField = (EditText) findViewById(R.id.nameNA_client);
        final EditText emailField = (EditText) findViewById(R.id.emailNA_client);
        final EditText pwdField = (EditText) findViewById(R.id.passwordNA_client);
        final EditText checkPwdField = (EditText) findViewById(R.id.passwordNA_client_repeat);

        if (nameField == null || emailField == null || pwdField == null || checkPwdField == null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String pwd = pwdField.getText().toString();
        String checkPwd = checkPwdField.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || checkPwd.isEmpty()){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_complete), Toast.LENGTH_LONG).show();
            return;
        }

        if (pwd.compareTo(checkPwd) != 0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.check_pwd), Toast.LENGTH_LONG).show();
            return;
        }

        if (!isEmailValid(email)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_email_format), Toast.LENGTH_LONG).show();
            return;
        }


        if (pwd.length() < 6){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pwd_too_short), Toast.LENGTH_LONG).show();
            return;
        }

        registration = true;
        username = name;

        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            nameField.setText("");
                            emailField.setText("");
                            pwdField.setText("");
                            checkPwdField.setText("");
                            Toast.makeText(context, "Registration failed." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                   }
                });

        // Attendo l'esito dell'operazione
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
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

    public void eseguiNuovaRegistrazioneRistoratore(View view) {
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("-------------> OnStart chiamato!!!!");
        if(mAuth!= null && mAuthListener!=null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public void tornaAlLogin(View view) {
        switch (view.getId()){
            case R.id.loginReturn_client:
                newClient.setVisibility(View.GONE);
                break;
            case R.id.loginReturn_manager:
                newManager.setVisibility(View.GONE);
                break;
        }
        defaultLogin.setVisibility(View.VISIBLE);
        setActivityTitle("Login");
    }

    public void insertClientInfo(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        Map<String, Object> userValues = new HashMap<>();
        userValues.put("name", username);
        userValues.put("userType", "C");
        userValues.put("avatarDownloadLink", getResources().getString(R.string.default_avatar));


        Map<String, Object> clientUpdates = new HashMap<>();
        clientUpdates.put("/users/" + id, userValues);

        mDatabase.updateChildren(clientUpdates);
    }
}

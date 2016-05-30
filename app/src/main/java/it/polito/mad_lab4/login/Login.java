package it.polito.mad_lab4.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;

public class Login extends BaseActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CardView defaultLogin;
    private CardView newClient;
    private CardView newManager;

    private ProgressDialog progressDialog = null;

    private TextView msgLoginFallito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    if (user != null) {
                        // User is signed in
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Toast.makeText(getApplicationContext(), "authentication succed", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
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


    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    public void eseguiLogin(View view) {
        try {
            final EditText passwordET = (EditText) findViewById(R.id.passwordET);
            final EditText usernameET = (EditText) findViewById(R.id.usernameET);

        /*User userInfo = UserBL.findUserByUsernamePassword(getApplicationContext(), usernameET.getText().toString(), passwordET.getText().toString());
        if(userInfo == null){
            Toast.makeText(getApplicationContext(), "Incorrect credentials", Toast.LENGTH_LONG).show();
            return;
        }*/

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

            /*
            // Scarica info base utente e in base al tipo di utente ritorna alla schermata appropriata

            UserSession.userId = userInfo.getUserId();

            Bundle b = new Bundle();
            b.putSerializable("userInfo", userInfo);

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtras(b);
            startActivity(i);*/
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
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.client_registrazione), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Login.this, "Client", Toast.LENGTH_SHORT).show();
                        defaultLogin.setVisibility(View.GONE);
                        newClient.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.manager_registrazione), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Login.this, "Manager", Toast.LENGTH_SHORT).show();
                        defaultLogin.setVisibility(View.GONE);
                        newManager.setVisibility(View.VISIBLE);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void eseguiNuovaRegistrazioneUtente(View view) {
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
                defaultLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.loginReturn_manager:
                newManager.setVisibility(View.GONE);
                defaultLogin.setVisibility(View.VISIBLE);
                break;

        }
    }
}

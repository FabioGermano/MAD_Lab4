package it.polito.mad_lab4.login_registrazione;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
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
import it.polito.mad_lab4.firebase_manager.FirebaseGetUserInfoManager;
import it.polito.mad_lab4.manager.MainActivityManager;
import it.polito.mad_lab4.newData.user.User;
import it.polito.mad_lab4.user.EditUserProfileActivity;

public class Login extends BaseActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog progressDialog = null;

    private TextView msgLoginFallito;
    private boolean alreadyNotified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        alreadyNotified = false;

        setActivityTitle(getResources().getString(R.string.login_title));
        setToolbarColor();
        setVisibilityAlert(false);

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

                        final String uid = user.getUid();
                        Toast.makeText(getApplicationContext(), "authentication succeed", Toast.LENGTH_SHORT).show();

                        new Thread()        {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showProgressBar();
                                    }
                                });

                                FirebaseGetUserInfoManager userInfoManager = new FirebaseGetUserInfoManager();
                                userInfoManager.getClientInfo(uid);
                                boolean timeout = userInfoManager.waitForResult();
                                final User infoUser = userInfoManager.getUserInfo();

                                if(timeout){
                                    System.out.println("TIMEOUT");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAuth.removeAuthStateListener(mAuthListener);
                                            Toast.makeText(Login.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                            dismissProgressDialog();
                                            finish();
                                        }
                                    });
                                    return;
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAuth.removeAuthStateListener(mAuthListener);

                                        if(infoUser.getUserType().compareTo("C") == 0) {
                                            System.out.println("E UN CLIENT");
                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                        } else if(infoUser.getUserType().compareTo("M") == 0){
                                            System.out.println("E UN MANAGER");
                                            Intent i = new Intent(getApplicationContext(), MainActivityManager.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                        }
                                        dismissProgressDialog();
                                        finish();
                                    }
                                });
                            }
                        }.start();

                        //TODO controllo coerenza dati nell due tabelle se è un client
                        }
                    }
            };

            mAuth.addAuthStateListener(mAuthListener);

            msgLoginFallito = (TextView) findViewById(R.id.login_failed_message);

        } catch (Exception e){
            System.out.println(e.getMessage());
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void eseguiLogin(View view) {
        try {
            final EditText passwordET = (EditText) findViewById(R.id.passwordET);
            final EditText usernameET = (EditText) findViewById(R.id.usernameET);

            if (!isNetworkAvailable()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                return;
            }

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
                                        if (progressDialog != null) {
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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    return;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

            // Attendo l'esito dell'operazione
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        } catch (Exception e){
            if(progressDialog != null){
                progressDialog.dismiss();
                progressDialog = null;
            }
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
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

}

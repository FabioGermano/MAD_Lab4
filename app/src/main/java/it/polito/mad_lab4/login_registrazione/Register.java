package it.polito.mad_lab4.login_registrazione;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.user.EditUserProfileActivity;

public class Register extends BaseActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog progressDialog = null;

    private String username = null;
    private boolean alreadyNotified;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = getApplicationContext();

        setToolbarColor();
        setActivityTitle(getResources().getString(R.string.titolo_registrazione_cliente));
        setVisibilityAlert(false);

        alreadyNotified = false;

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

                            //arrivo qua dalla fase di registrazione
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
                }
            };

            mAuth.addAuthStateListener(mAuthListener);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }



    }

    public void nuovaRegistrazioneClient(View view) {
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

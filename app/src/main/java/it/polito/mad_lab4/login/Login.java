package it.polito.mad_lab4.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.UserBL;
import it.polito.mad_lab4.data.user.UserSession;
import it.polito.mad_lab4.data.user.User;

public class Login extends BaseActivity {
    //private Firebase ref = new Firebase("https://<YOUR-FIREBASE-APP>.firebaseio.com");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setActivityTitle(getResources().getString(R.string.login_title));
        setToolbarColor();

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

    public void eseguiLogin(View view) {

        EditText passwordET = (EditText)findViewById(R.id.passwordET);
        EditText usernameET = (EditText)findViewById(R.id.usernameET);

        User userInfo = UserBL.findUserByUsernamePassword(getApplicationContext(), usernameET.getText().toString(), passwordET.getText().toString());
        if(userInfo == null){
            Toast.makeText(getApplicationContext(), "Incorrect credentials", Toast.LENGTH_LONG).show();
            return;
        }

        /*
        // Create a handler to handle the result of the authentication
        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                // Authenticated successfully with payload authData
                authData.getUid();
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Authenticated failed with error firebaseError
            }
        };
        //with an email/password combination
        ref.authWithPassword(usernameET.getText().toString(), passwordET.getText().toString(), authResultHandler);
        */

        // Scarica info base utente e in base al tipo di utente ritorna alla schermata appropriata

        UserSession.userId = userInfo.getUserId();

        Bundle b = new Bundle();
        b.putSerializable("userInfo", userInfo);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtras(b);
        startActivity(i);
    }
}

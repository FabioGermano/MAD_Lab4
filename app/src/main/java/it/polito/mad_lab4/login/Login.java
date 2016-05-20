package it.polito.mad_lab4.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.UserBL;
import it.polito.mad_lab4.common.UserSession;
import it.polito.mad_lab4.data.user.User;

public class Login extends BaseActivity {

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

        //User userInfo = UserBL.getUserById(getBaseContext(), 1);
        /*
        //if(userInfo != null) {
            if(userInfo.getUserLoginInfo() != null){
                userInfo.getUserLoginInfo().setLogin(true);
            }
        //}*/

        UserSession.userId = userInfo.getUserId();

        Bundle b = new Bundle();
        b.putSerializable("userInfo", userInfo);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtras(b);
        startActivity(i);
    }
}

package it.polito.mad_lab3.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import it.polito.mad_lab3.MainActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.UserBL;
import it.polito.mad_lab3.data.user.User;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void eseguiLogin(View view) {
        User userInfo = UserBL.getUserById(getBaseContext(), 1);

        if(userInfo != null) {
            if(userInfo.getUserLoginInfo() != null){
                userInfo.getUserLoginInfo().setLogin(true);
            }
        }

        Bundle b = new Bundle();
        b.putSerializable("userInfo", userInfo);

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        i.putExtras(b);
        startActivity(i);
    }
}

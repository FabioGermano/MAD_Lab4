package it.polito.mad_lab4.login_registrazione;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;

public class Register_restaurant extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);


        setToolbarColor();
        setActivityTitle(getResources().getString(R.string.titolo_registrazione_ristorante));
        setVisibilityAlert(false);
    }


    public void sendRequest(View view){

        if (!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            return;
        }


        if (!checkField()){
            return;
        }

        android.support.v7.app.AlertDialog dialog;
        // Strings to Show
        final String msg = getResources().getString(R.string.messaggio_richiesta_inviata);
        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("REQUEST RECEIVED");
        builder.setMessage(msg);
        builder.setCancelable(false);

        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                        dialog.dismiss();
                        finish();
                    }
                });

        dialog = builder.create();
        dialog.show();
    }

    private boolean checkField(){
        final EditText nameField = (EditText) findViewById(R.id.nameNA_manager);
        final EditText emailField = (EditText) findViewById(R.id.emailNA_manager);
        final EditText restaurantField = (EditText) findViewById(R.id.restaurantName_manager);

        if (nameField == null || emailField == null || restaurantField == null){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_LONG).show();
            finish();
        }

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String restaurantName = restaurantField.getText().toString();

        if (name.isEmpty() || email.isEmpty() || restaurantName.isEmpty()){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_complete), Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isEmailValid(email)){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_email_format), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private static boolean isEmailValid(String email) {
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

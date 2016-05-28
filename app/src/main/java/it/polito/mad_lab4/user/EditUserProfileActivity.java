package it.polito.mad_lab4.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.user.User;

public class EditUserProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        LinearLayout universityLL = (LinearLayout) findViewById(R.id.universityLL);
        ((TextView)universityLL.findViewById(R.id.universityText)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUniversity();
            }
        });
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

    public void pickUniversity(){


        UniversityPickerFragment universityPickerFragment = UniversityPickerFragment.newInstance(null);
        universityPickerFragment.show(this.getFragmentManager(), "dialog");
    }
}

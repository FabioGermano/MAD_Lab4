package it.polito.mad_lab3.elaborazioneRicerche;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.polito.mad_lab3.R;

/**
 * Created by Euge on 05/05/2016.
 */
public class fragment_ricercaAvanzata extends Fragment {
    private Context context;
    private View rootView;
    private OnButtonPressedListener mCallback;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getContext();
    }

    public interface OnButtonPressedListener {

        public void onButtonPressed();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnButtonPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDateSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ricerca_avanzata, container, false);
        rootView.setVisibility(View.GONE);
        Button button = (Button) rootView.findViewById(R.id.button_finder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ricercaAvanzata();
            }
        });

        return rootView;
    }

    public void setFragment(){
        rootView.setVisibility(View.VISIBLE);
        rootView.invalidate();
    }

    public void closeFragment(){
        rootView.setVisibility(View.GONE);
        rootView.invalidate();
    }

    private void ricercaAvanzata(){
        //leggo i vari parametri dei filtri e li passo alla funzione, deve essere definita in modo opportuno la funzione
        mCallback.onButtonPressed();
    }
}

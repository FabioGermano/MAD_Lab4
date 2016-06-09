package it.polito.mad_lab4.elaborazioneRicerche;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import it.polito.mad_lab4.R;

/**
 * Created by Euge on 05/05/2016.
 */
public class fragment_ricercaAvanzata extends Fragment {
    private Context context;
    private View rootView;
    private Button buttonR;
    private OnButtonPressedListener mCallback;
    private String selezioneTipoLocale = null;
    private String selezioneCostoLocale = null;
    private String selezioneValutazioneLocale = null;

    private Spinner valutazioneLocale;
    private Spinner tipoLocale;
    private Spinner costoLocale;
    private EditText nomePiatto;

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getContext();
    }

    public interface OnButtonPressedListener {

        public void onButtonPressed(String type, String cost, String rating, String nomePiatto);

        public void onResetPressed();

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

        Button buttonS = (Button) rootView.findViewById(R.id.search_finder);
        buttonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ricercaAvanzata();
            }
        });

        buttonR = (Button) rootView.findViewById(R.id.reset_finder);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRicercaAvanzata();
            }
        });
        buttonR.setVisibility(View.INVISIBLE);

        tipoLocale = (Spinner) rootView.findViewById(R.id.spinner_tipo_finder);
        if(tipoLocale != null) {
            tipoLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    selezioneTipoLocale = (String) adapter.getItemAtPosition(pos);
                    if (selezioneTipoLocale.compareTo("-") == 0)
                        selezioneTipoLocale = null;
                    else if(selezioneTipoLocale.compareTo(getResources().getString(R.string.takeAway))== 0)
                        selezioneTipoLocale = "TA";
                    else if(selezioneTipoLocale.compareTo(getResources().getString(R.string.restaurant)) == 0)
                        selezioneTipoLocale = "R";
                    else
                        selezioneTipoLocale = "ALL";
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }

        costoLocale = (Spinner) rootView.findViewById(R.id.spinner_costo_finder);
        if(costoLocale != null) {
            costoLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    selezioneCostoLocale = (String) adapter.getItemAtPosition(pos);
                    if (selezioneCostoLocale.compareTo("-") == 0)
                        selezioneCostoLocale = null;
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }

        valutazioneLocale = (Spinner) rootView.findViewById(R.id.spinner_valutazione_finder);
        if(valutazioneLocale != null) {
            valutazioneLocale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    selezioneValutazioneLocale = (String) adapter.getItemAtPosition(pos);
                    if (selezioneValutazioneLocale.compareTo("-") == 0)
                        selezioneValutazioneLocale = null;
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }

        nomePiatto = (EditText) rootView.findViewById(R.id.piatto_finder);

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
        mCallback.onButtonPressed(selezioneTipoLocale, selezioneCostoLocale, selezioneValutazioneLocale, nomePiatto.getText().toString());

        //resetto i valori dei campi dello spinner
        //imposto lo spinner al valore corretto del piatto
        if (valutazioneLocale != null)
            valutazioneLocale.setSelection(0);
        if (tipoLocale != null)
            tipoLocale.setSelection(0);
        if (costoLocale != null)
            costoLocale.setSelection(0);
        if(nomePiatto != null)
            nomePiatto.setText("");
    }

    public void setResetButton(){
        buttonR.setVisibility(View.VISIBLE);
    }

    private void resetRicercaAvanzata() {
        mCallback.onResetPressed();

        //resetto i valori dei campi dello spinner
        //imposto lo spinner al valore corretto del piatto
        if (valutazioneLocale != null)
            valutazioneLocale.setSelection(0);
        if (tipoLocale != null)
            tipoLocale.setSelection(0);
        if (costoLocale != null)
            costoLocale.setSelection(0);
        if (nomePiatto != null)
            nomePiatto.setText("");

        buttonR.setVisibility(View.INVISIBLE);
    }

}

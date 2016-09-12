package it.polito.mad_lab4.firebase_manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Euge on 03/06/2016.
 */
public class FirebaseGetAuthInformation implements  FirebaseAuth.AuthStateListener, OnCompleteListener<GetTokenResult>, OnFailureListener{

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();
    private boolean resultReturned = false;

    private boolean alreadyNotified;
    private FirebaseAuth mAuth;
    private boolean timeout;

    // info di ritorno
    FirebaseUser user = null;  // se è uguale a null c'è un qualche problema con l'autenticazione

    int errorType = -2;     //  0: utente non connesso
                            //  1: è necessario ri-autenticarsi
                            // -1: è fallito il task che avrebbe dovuto controllare lo stato dell'autenticazione

    public FirebaseGetAuthInformation(){
        alreadyNotified = false;
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                10000
        );
    }

    private void timeout() {
        lock.lock();
        timeout = true;
        this.cv.signal();
        lock.unlock();
    }
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        lock.lock();
        user = firebaseAuth.getCurrentUser();
        if (user != null && !alreadyNotified) {
            // User is signed in, controllo se è scaduta la sua autenticazione
            alreadyNotified = true;
            System.out.println("--------->>>>>> utente connesso, GESTITO NELL'APPOSITA CLASSE: " + user.getUid());
            user.getToken(false).addOnCompleteListener(this); // con true accetto la richiesta di ri-autenticazione

        } else if(!alreadyNotified) {
            // utente non autenticato
            System.out.println("--------->>>>>> utente NON connesso, GESTITO NELL'APPOSITA CLASSE");

            alreadyNotified = true;
            errorType = 0;
            resultReturned = true;
            this.cv.signal();
        }
        lock.unlock();
    }

    public boolean waitForResult() {
        lock.lock();
        try {if(!resultReturned || timeout)
            cv.await();
        } catch (InterruptedException e) {
            System.out.println("Eccezione: "+ e.getMessage());
            Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }
        return timeout;
    }


    @Override
    public void onComplete(@NonNull Task<GetTokenResult> task) {
        lock.lock();

        if(!task.isSuccessful()) {
            Exception e = task.getException();
            if (e != null) {
                // c'è stata una eccezione
                System.out.println("----->>>>>> Eccezione: " + e.getMessage());
                user = null;
                errorType = 1;
            }
        }else {
            System.out.println("----->>> Task eseguito correttamente: " + task.getResult().getToken());
        }

        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        // getToken ha fallito per qualche motivo
        lock.lock();
        System.out.println("----->>>> Task richiesta token fallito: " + e.getMessage());

        user = null;
        errorType = -1;

        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public FirebaseUser getUser(){
        return this.user;
    }

    public int getErrorType(){
        return this.errorType;
    }
}

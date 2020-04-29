package com.example.projasso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.projasso.utilities.Constants;
import com.example.projasso.utilities.Functions;
import com.example.projasso.utilities.Session;

import java.util.Collections;
import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AsyncCallWS asyncCallWS = new AsyncCallWS();
        asyncCallWS.execute();

    }

    //on crée une classe qui hérite d'AsyncTask
    private class AsyncCallWS extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            /*on simule un appel service Web
            try{
                Thread.sleep(5000L);
            }
            catch (InterruptedException e)
             {
            e.printStackTrace();
            }*/

            //Hasmap = dictionnaire java (ssystème de key values)
            HashMap<String, String> parameters = new HashMap<>();

            return Functions.callWebService(parameters, "getId");

        }



        @Override
        //On reçoit notre réponse web, qui est ici un id
        protected void onPostExecute(String SessionId) {
            super.onPostExecute(SessionId);
            //On ajoute cet id à notre session, d'où le nommage SessionId
            Session.setId(SessionId);


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //passage de données d'une activité à l'autre
            intent.putExtra("sessionId", SessionId);
            startActivity(intent);
            finish();
        }
    }
}
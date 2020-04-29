package com.example.projasso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projasso.entities.Adherent;
import com.example.projasso.utilities.Functions;
import com.example.projasso.utilities.Session;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;

import kotlin.Function;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText mEditTextLogin;
    EditText mEditTextPassword;
    ProgressBar mProgressBar;

    String sessionId;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //on récupère tous nos widgets présents dans notre layout activity_main
        mEditTextLogin = findViewById(R.id.editText_login);
        mEditTextPassword = findViewById(R.id.editText_password);
        mProgressBar = findViewById(R.id.progressBar_main);
        Button btnConnexion = findViewById(R.id.button_connexion);

        //on récupère l'intent créé depuis notre SplashActivity
        Intent intent = getIntent();
        //on récupère le sessionId qui se trouve dans notre intent créé depuis SplashActivity
        sessionId = intent.getStringExtra("sessionId");

        //si il n'y pas de sessionid contenu dans mon intent je finish mon application (violent)
        if(sessionId.isEmpty()){
            finish();
        }


        //on fait de notre btnConnexion un bouton cliquable et on lui indique la démarche à suivre au moment du clique
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //On recupere les valeurs qui ont été tapées dans nos editText Login et Password
                String login = mEditTextLogin.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();

                //si l'une des deux ou les deux sont vide => notification d'erreur (toast)
                if (login.isEmpty() || password.isEmpty()) {
                    Functions.getToast(mContext, "Veuillez saisir vos identifiants");
                    return;
                }

                //On fait une requete asynchrone
                AsyncCallWS asyncCallWS = new AsyncCallWS(login,password);
                asyncCallWS.execute();
            }
        });
    }

    public class AsyncCallWS extends AsyncTask<String, Integer, String> {

        private HashMap<String, String> parameters = new HashMap<>();

        public AsyncCallWS(String login, String password) {
            parameters.put("login", login);
            parameters.put("password", password);
            parameters.put("sessionId", Session.getId());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            return Functions.callWebService(parameters, "LoginAdherent");
        }

        @Override
        protected void onPostExecute(String responseWebService) {
            super.onPostExecute(responseWebService);

            if(!responseWebService.isEmpty() || !responseWebService.equals("\"\"")){
                try{
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Gson gson = new Gson();

                    //créé un adherent et on lui donne les attributs contenus dans notre retourWebService (qui est sous format json)
                    Adherent adherent = gson.fromJson(responseWebService, Adherent.class);

                    //Ajout de l'adherent à la Session
                    Session.setAdherent(adherent);
                    Session.setId(sessionId);

                    Intent intent = new Intent(mContext, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
                catch(Exception ex){
                    //Pour remonter l'erreur au dev
                    Log.e("Erreur service web", ex.getMessage());
                    //Pour informer le user d'une erreur
                    Functions.getToast(mContext, "il y a eu une exception");
                }
            }
        }
    }
}

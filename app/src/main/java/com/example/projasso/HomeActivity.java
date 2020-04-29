package com.example.projasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.projasso.entities.Adherent;
import com.example.projasso.entities.Associations;
import com.example.projasso.entities.Sorties;
import com.example.projasso.fragments.HomeFragment;
import com.example.projasso.fragments.SortiesFragment;
import com.example.projasso.utilities.Functions;
import com.example.projasso.utilities.Session;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    Context mContext;

    FragmentManager mFragmentManager;
    ArrayList<Fragment> mFragments;

    SortiesFragment mSortiesFragment;
    HomeFragment mHomeFragment;
    Adherent adherent;


    FragmentTransaction fragmentTransaction;

    int index;
    Associations associations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;

        //on récupère l'adherent que l'on a mis dans notre session
        adherent = Session.getAdherent();

        //si l'adhenrent est null fermeture violente de l'application
        if (adherent == null) {
            this.finish();
        } else
            {
            //on créé notre navBar via la méthode initToolbar()
            initToolbar();

            initFragments();

            LoadFragment(mHomeFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeFragment.setAdherent(adherent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //on associe un menu à notre activity
        getMenuInflater().inflate(R.menu.mnu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //On récupère chaque id pour les associer aux menuItems correspondants
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case R.id.action_compte:
                index = 0;
                fragment = mFragments.get(index); //ici mHomeFragment
                break;
            case R.id.action_sorties:
                index = 1;
                fragment = mFragments.get(index);

                AsyncCallWS asyncCallWS = new AsyncCallWS(adherent.getIdAssociation());
                asyncCallWS.execute();
                break;
            case R.id.action_quitter:
                finish();
                break;
        }

        if(fragment != null){
            LoadFragment(fragment);
        }
        return true;
    }

    private void initFragments() {

        mSortiesFragment = new SortiesFragment();
        mHomeFragment = new HomeFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mHomeFragment);
        mFragments.add(mSortiesFragment);

        mFragmentManager = getFragmentManager();
    }

    //initialisation de notre navbar
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        //on remplace l'actionbar par la toolbar (l'actionbar est l'ancienne version de toolnbar)
        setSupportActionBar(toolbar);

        //on supprime le titre par défault de ma toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //on ajoute un logo qui a été créé depuis Drawable
        toolbar.setLogo(R.drawable.ic_asso);
        //on définit le titre de la toolbar
        toolbar.setTitle("AfpaAsso");

        //on ajoute un évènement click sur la toolbar (tous ce qui n'est pas un item du menu, sera lié à ce clique)
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //j'instancie mon fragment par défaut

            }
        });
    }

    private void LoadFragment(Fragment fragment) {
        //Gestion des fragments
        mFragmentManager.beginTransaction()
                .replace(R.id.framelayout_home, fragment)
                .commit();
    }

    public class AsyncCallWS extends AsyncTask<String, Integer, String> {
        String methodWS = "";

        int _idAssociation = 0;

        String _email;
        String _mobile;
        String _password;

        public AsyncCallWS(int idAssociation) {
            this._idAssociation = idAssociation;
        }

        public AsyncCallWS(String email, String password, String mobile, String montant) {
            this._email = email;
            this._mobile = mobile;
            this._password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> parameters = new HashMap<>();

            if(this._idAssociation > 0){
                parameters.put("idAssociation", ""+ this._idAssociation);
                methodWS = "getSorties";
            }
            else
                {
                parameters.put("email", this._email);
                parameters.put("password", this._password);
                parameters.put("mobile", this._mobile);
                parameters.put("idSession", ""+ Session.getId());
                methodWS= "UpdateAdherent";
            }

            return Functions.callWebService(parameters, methodWS);
        }

        @Override
        protected void onPostExecute(String responseWebService) {
            super.onPostExecute(responseWebService);
            if (!responseWebService.isEmpty() || !responseWebService.equals("\"\"")) {
                try {
                    Gson gson = new Gson();

                    if(methodWS ==  "getSorties"){
                        //créer un adherent pour vériier les variables tranmisent par le WS
                        Sorties sorties = gson.fromJson(responseWebService, Sorties.class);
                        mSortiesFragment.loadRecyclerSortie(sorties, mContext);
                    }else if(methodWS == "UpdateAdherent") {
                        //On récupère l'update adherent qui vient dêtre sauvegardé en bdd (via le return callwebservice)
                        Adherent adherent = gson.fromJson(responseWebService, Adherent.class);
                        //On le MAJ en session thx
                        Session.setAdherent(adherent);
                    }
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void updateAdherent(String email, String password, String mobile, String montant){

        AsyncCallWS asyncCallWS = new AsyncCallWS(email,password,mobile, montant);
        asyncCallWS.execute();
    }
}
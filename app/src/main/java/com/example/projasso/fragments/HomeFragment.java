package com.example.projasso.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AlertDialog;

import com.example.projasso.HomeActivity;
import com.example.projasso.R;
import com.example.projasso.entities.Adherent;
import com.example.projasso.utilities.Session;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView txtEmail;
    TextView txtMobile;
    TextView txtSolde;
    TextView txtCompteName;

    EditText editTxtEmail;
    EditText editTxtPassword;
    EditText editTxtMobile;

    Button btnModifier;
    Button btnAnnuler;
    Button btnValider;
    Button btnCrediterCompte;

    ViewSwitcher viewSwitcher;

    Adherent mAdherent;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        initWidget(view);

        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showNext();
            }
        });

        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTxtEmail.getText().toString().trim();
                String password ="";
                String mobile = editTxtMobile.getText().toString().trim();

                //s'enregistre après updateAdherent
                /*mAdherent.setEmail(email);
                mAdherent.setTelephone(mobile);

                Session.setAdherent(mAdherent);*/


                ((HomeActivity)getActivity()).updateAdherent(email, password, mobile, "");
                setData();

                viewSwitcher.showPrevious();
            }
        });

        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.reset();
            }
        });

        btnCrediterCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomAlertDialog();
            }
        });

        return view;
    }

    public void setAdherent(Adherent adherent){

        this.mAdherent = adherent;

    }

    private void setData()
    {
        mAdherent = Session.getAdherent();
        txtCompteName.setText(mAdherent.getPrenom()+ " " + mAdherent.getNom());

        txtEmail.setText("Email : " +mAdherent.getEmail());
        txtMobile.setText("Telephone : " +mAdherent.getTelephone());
        txtSolde.setText("Solde : " +mAdherent.getSolde());

        editTxtEmail.setText(mAdherent.getEmail());
        //editTxtPassword.setText("");
        editTxtMobile.setText(mAdherent.getTelephone());
    }

    @Override
    public void onResume() {
        super.onResume();

        setData();
    }

    private void initWidget(View view){

        txtEmail = view.findViewById(R.id.txt_frgm_home_email);
        txtMobile = view.findViewById(R.id.txt_frgm_home_mobile);
        txtSolde = view.findViewById(R.id.txt_frgm_home_solde);
        txtCompteName = view.findViewById(R.id.txt_frgm_home_name);

        editTxtEmail = view.findViewById(R.id.edt_frgm_home_email);
        editTxtMobile = view.findViewById(R.id.edt_frgm_home_mobile);
        editTxtPassword = view.findViewById(R.id.editText_password);

        viewSwitcher = view.findViewById(R.id.viewswitcher_frgm_home);

        btnModifier = view.findViewById(R.id.btn_frgm_home_modifier);
        btnAnnuler = view.findViewById(R.id.btn_frgm_home_annuler_modification);
        btnValider = view.findViewById(R.id.btn_frgm_home_valider_modification);
        btnCrediterCompte = view.findViewById(R.id.btn_frgm_home_crediter);
    }

    private void openCustomAlertDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Créditer compte");

        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.custom_dialogue_crediter, null);

        final EditText txtMontant = view.findViewById(R.id.textViewMontant);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Valider",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // on récupère le montant
                        String montant = txtMontant.getText().toString().trim();
                        // maj de l'objet adhérent
                        mAdherent.setSolde(mAdherent.getSolde() + Double.parseDouble(montant));
                        // on enregistre en session
                        Session.setAdherent(mAdherent);

                        // maj en bdd
                        ((HomeActivity)getActivity()).updateAdherent("", "", "", montant);

                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
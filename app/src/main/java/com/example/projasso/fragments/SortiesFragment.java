package com.example.projasso.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projasso.HomeActivity;
import com.example.projasso.R;
import com.example.projasso.entities.Sortie;
import com.example.projasso.entities.Sorties;
import com.example.projasso.utilities.Session;


public class SortiesFragment extends Fragment {

    Context mContext;
    RecyclerView rcvSorties;


    public SortiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sorties, container, false);

        rcvSorties = view.findViewById(R.id.recyclerview_frgm_sorties);
        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<SortieHolder> {

        Sorties mSorties;

        public RecyclerViewAdapter(Sorties sorties) {
            this.mSorties = sorties;
        }

        @NonNull
        @Override
        public SortieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_sortie, parent, false);

            return new SortieHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SortieHolder sortieHolder, int position) {
            Sortie sortie = this.mSorties.get(position);
            sortieHolder.setSortie(sortie);
        }

        @Override
        public int getItemCount() {
            return mSorties.size();
        }
    }

    public class SortieHolder extends RecyclerView.ViewHolder {

        public final TextView txtNom;
        public final TextView txtPrix;
        public final TextView txtDate;
        public final ImageView imgPhoto;

        public final Button btnDetail;
        public final Button btnInscription;

        public SortieHolder(@NonNull View itemView) {
            super(itemView);

            txtNom = itemView.findViewById(R.id.txt_sortie_nom);
            txtPrix = itemView.findViewById(R.id.txt_sortie_prix);
            txtDate = itemView.findViewById(R.id.txt_sortie_date);
            imgPhoto = itemView.findViewById(R.id.img_sortie);

            btnDetail = itemView.findViewById(R.id.btn_sortie_detail);
            btnInscription = itemView.findViewById(R.id.btn_sortie_inscription);



        }

        public void setSortie(Sortie sortie) {

            txtNom.setText(sortie.getNom());
            txtPrix.setText(""+sortie.getPrix()+" €");
            txtDate.setText("");
            //imgPhoto.setText(association.getTelephone());

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            btnInscription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    //appelé depuis HomeActivité
    public void loadRecyclerSortie(Sorties sorties, Context context) {

        this.mContext = context;

        RecyclerViewAdapter recyclerViewAdapterSortie = new RecyclerViewAdapter(sorties);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        rcvSorties.setLayoutManager(layoutManager);

        rcvSorties.setAdapter(recyclerViewAdapterSortie);

        //Effet sur le recylcerview
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        rcvSorties.setItemAnimator(defaultItemAnimator);
    }

    private void openCustomAlertDialog(Sortie sortie){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Details sortie");

        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.custom_dialogue_detail_sortie, null);


        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Valider",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {


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
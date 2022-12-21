package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity {

    ListView lvFav;
    SearchView svFav;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<Texto> arrayListTextosObj = new ArrayList<>();
    ArrayList<Texto> arrayListTextosCopia = new ArrayList<>();
    ArrayAdapter<Texto> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        iniciarFirebase();
        svFav = findViewById(R.id.searchViewListaFav);
        lvFav= findViewById(R.id.listViewFav);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query;
        query = databaseReference.child(user.getUid()).child("Textos");
        arrayListTextosObj.clear();

        lvFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder janelaF = new AlertDialog.Builder(FavoritosActivity.this);
                janelaF.setTitle(arrayListTextosObj.get(i).getTitulo());
                janelaF.setMessage(arrayListTextosObj.get(i).getTexto());
                janelaF.setNegativeButton("Fechar", null);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot objDataSnapshot1 : snapshot.getChildren()) {
                    Texto texto = objDataSnapshot1.getValue(Texto.class);
                    arrayListTextosObj.add(texto);
                }
                arrayAdapter = new ArrayAdapter(
                        getApplicationContext(),
                        R.layout.minha_lista,
                        arrayListTextosObj
                );

                lvFav.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        svFav.setIconified(false);


        lvFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder janela = new AlertDialog.Builder(FavoritosActivity.this);
                janela.setTitle(arrayListTextosObj.get(i).getTitulo());
                janela.setMessage(arrayListTextosObj.get(i).getTexto());
                janela.setNeutralButton("Ok", null);
                janela.setNegativeButton("Cancelar", null);

                janela.show();
            }
        });

        svFav.clearFocus();

        svFav.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                FavoritosActivity.this.arrayAdapter.getFilter().filter(s);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }
    private void fazerUmaBusca(String s) {
        arrayListTextosObj.clear();

        if (s.isEmpty()) {
            arrayListTextosObj.addAll(arrayListTextosCopia);
        } else {
            s = s.toLowerCase();

            for (Texto item : arrayListTextosCopia) {
                //getId ou getTitulo?
                if (item.getTitulo().toLowerCase().contains(s) || item.getTexto().toLowerCase().contains(s))
                    arrayListTextosObj.add(item);
            }
        }
    }
    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}

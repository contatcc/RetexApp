package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListagemTextosActivity extends AppCompatActivity {

    SearchView searchView2;
    ListView listView2;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<Texto> arrayListTextosObj = new ArrayList<>();
    ArrayList<Texto> arrayListTextosCopia = new ArrayList<>();
    ArrayAdapter<Texto> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_textos);

        searchView2= findViewById(R.id.searchViewListagemEs);
        listView2 =  findViewById(R.id.listViewListagem);

        iniciarFirebase();

        for(Texto val: arrayListTextosObj){
            arrayListTextosCopia.add(val);
        }

        Query query;
        query = databaseReference.child("Textos");
        arrayListTextosObj.clear();

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

                listView2.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                AlertDialog.Builder janela1 = new AlertDialog.Builder(ListagemTextosActivity.this);
                janela1.setTitle(arrayListTextosObj.get(posicao).getTitulo());
                janela1.setMessage(arrayListTextosObj.get(posicao).getTexto());
                janela1.setNegativeButton("Não", null);

                janela1.setPositiveButton("Favoritar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Título salvo nos favoritos.", Toast.LENGTH_SHORT).show();
                        inserirEm(posicao);
                    }
                });
                janela1.show();
            }
        });

        searchView2.setIconified(false);
        searchView2.clearFocus();

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //fazerUmaBusca(s);
                ListagemTextosActivity.this.arrayAdapter.getFilter().filter(s);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void fazerUmaBusca(String s) {
        Log.e("Aqui", String.valueOf(arrayListTextosCopia.size()));
        arrayListTextosObj.clear();

        if (s.isEmpty()) {
            arrayListTextosObj.addAll(arrayListTextosCopia);
        } else {
            s = s.toLowerCase();

            for (Texto item : arrayListTextosCopia) {
                if (item.getTitulo().toLowerCase().contains(s) || item.getTexto().toLowerCase().contains(s))
                    arrayListTextosObj.add(item);
            }
        }
    }

    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void inserirEm(int posicao) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Texto texto = arrayListTextosObj.get(posicao);

        databaseReference.child(user.getUid()).
                child("Textos").
                child(texto.getTitulo()).
                setValue(texto);
    }

}
package com.example.esboco;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import java.util.List;

public class MainAcitivityRecyclerView extends AppCompatActivity {

    RecyclerView rv1;
    SearchView searchView;
    CheckBox cbFav;
    Query query;
    ProgressBar pb;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceFavoritos;

    List<Texto> textoList = new ArrayList<>();
    ArrayList<Texto> textoArrayList_ = new ArrayList<>();
    ArrayList<Texto> arrayListTextosCopia = new ArrayList<>();
    ArrayList<Texto> list;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acitivity_recycler_view);

        rv1 = findViewById(R.id.recyclerView01);
        searchView = findViewById(R.id.searchViewRecycler);
        cbFav = findViewById(R.id.checkBoxFav);
        pb = findViewById(R.id.progressBarC);
        recyclerView = findViewById(R.id.recyclerView01);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Textos");
        databaseReferenceFavoritos = FirebaseDatabase.getInstance().getReference().child("TextosFavoritos");

        textoArrayList_.clear();
        mostrarTextos();
        setAdapter(textoArrayList_);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        cbFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbFav.isChecked()){
                    query = databaseReferenceFavoritos.child(user.getUid());
                    textoArrayList_.clear();

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot != null) {

                                for (DataSnapshot objDataSnapshot1 : snapshot.getChildren()) {
                                    Texto texto = objDataSnapshot1.getValue(Texto.class);
                                    textoArrayList_.add(texto);
                                }
                                setAdapter(textoArrayList_);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Log.e("marcado", "marcado");
                } else {
                    textoArrayList_.clear();

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot != null) {

                                for (DataSnapshot objDataSnapshot1 : snapshot.getChildren()) {
                                    Texto texto = objDataSnapshot1.getValue(Texto.class);
                                    textoArrayList_.add(texto);
                                }
                                setAdapter(textoArrayList_);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Log.e("desmarcado", "desmarcado");
                }
            }
        });

        searchView.setIconified(false);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return true;
            }
        });
    }

    private void mostrarTextos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {

                    for (DataSnapshot objDataSnapshot1 : snapshot.getChildren()) {
                        Texto texto = objDataSnapshot1.getValue(Texto.class);
                        textoArrayList_.add(texto);
                    }
                    setAdapter(textoArrayList_);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list.add(ds.getValue(Texto.class));
                        }
                        recyclerAdapter = new RecyclerAdapter(list);
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainAcitivityRecyclerView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<Texto> myList = new ArrayList<>();
        for (Texto object : list) {
            if (object.getTitulo().toLowerCase().contains(str.toLowerCase()) ||
                    object.getGenero().toLowerCase().contains(str.toLowerCase())) {
                myList.add(object);
            }
        }
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(myList);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setAdapter(ArrayList<Texto> textoArrayList_) {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(textoArrayList_);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

//        Recycler View com grid ao invés de lista:
//        ***
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
//        ***

        //***

        rv1.setLayoutManager(layoutManager);
        rv1.setAdapter(recyclerAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail().contains("@ifms.edu.br")) {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    //remove o item da lista e atualiza o RecyclerView
                    int position = viewHolder.getAdapterPosition();
                    Texto texto = recyclerAdapter.textoArrayList.get(position);
                    databaseReference.child(texto.getId()).removeValue(); //todo testar com removeValue
                    textoArrayList_.remove(position);
                    recyclerAdapter.notifyDataSetChanged();

                }

                @Override
                public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                    return 1f;
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    setDeleteIcon(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);
                }

                private void setDeleteIcon(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    Paint mClearPaint = new Paint();
                    mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    ColorDrawable mBackground = new ColorDrawable();

                    int backgroundColor = Color.parseColor("#60C9EA");
                    Drawable deleteDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete);
                    int intrinsicWidth = deleteDrawable.getIntrinsicWidth();
                    int intrinsicHeight = deleteDrawable.getIntrinsicHeight();

                    View itemView = viewHolder.itemView;
                    int itemHeight = itemView.getHeight();

                    boolean isCancelled = dX == 0 && !isCurrentlyActive;

                    if (isCancelled) {
                        c.drawRect(itemView.getRight()+dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), mClearPaint);
                        return;
                    }

                    mBackground.setColor(backgroundColor);
                    mBackground.setBounds(itemView.getRight() + (int) dX,
                            itemView.getTop()+70,
                            itemView.getRight(),
                            itemView.getBottom()-50);
                    mBackground.draw(c);

                    int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                    int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
                    int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
                    int deleteIconRight = itemView.getRight() - deleteIconMargin;
                    int deleteIconBottom = deleteIconTop + intrinsicHeight;

                    deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                    deleteDrawable.draw(c);
                }
            };

            ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            touchHelper.attachToRecyclerView(recyclerView);

            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerAdapter);
        }

    }

    @Override
    public void onBackPressed() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getEmail().contains("@estudante.ifms.edu.br")) {
            startActivity(new Intent(MainAcitivityRecyclerView.this, TelaEstudanteActivity.class));
        } else {
            startActivity(new Intent(MainAcitivityRecyclerView.this, TelaProfessorActivity.class));
        }
        super.onBackPressed();
    }
}

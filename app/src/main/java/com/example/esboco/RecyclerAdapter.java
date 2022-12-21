package com.example.esboco;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Texto> textoArrayList = new ArrayList<>();

    //construtor
    public RecyclerAdapter(ArrayList<Texto> textoArrayList) {
        this.textoArrayList = textoArrayList;
    }

    //inner class do viewholder
    //funciona mais ou menos como a onCreate da MainActivity
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTextViewTitulo;
        TextView mTextViewGenero;
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            mTextViewGenero = itemView.findViewById(R.id.textViewGenero);
            mImageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //TODO pega o título, nome do autor e corpo do texto
            Intent i = new Intent(view.getContext(), ExibirTextoActivity.class);
            i.putExtra("Id", textoArrayList.get(getLayoutPosition()).getId());
            i.putExtra("Título", textoArrayList.get(getLayoutPosition()).getTitulo());
            i.putExtra("Autor", textoArrayList.get(getLayoutPosition()).getAutor());
            i.putExtra("Texto", textoArrayList.get(getLayoutPosition()).getTexto());
            i.putExtra("Tipologia", textoArrayList.get(getLayoutPosition()).getTipologia());
            i.putExtra("Genero", textoArrayList.get(getLayoutPosition()).getGenero());
            i.putExtra("TextoObj", textoArrayList.get(getLayoutPosition()));
            textoArrayList.clear();
            view.getContext().startActivity(i);
        }

        private void removeAt(int layoutPosition) {
            textoArrayList.remove(layoutPosition);
            notifyItemRemoved(layoutPosition);
            notifyItemRangeChanged(layoutPosition, textoArrayList.size());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //método para inflar o xml dos itens
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        //método para pegar o valor e jogar nas views da tela;

        String titulo = textoArrayList.get(position).getTitulo();
        String genero = textoArrayList.get(position).getGenero();
        String tipologia = textoArrayList.get(position).getTipologia().toLowerCase();

        holder.mTextViewTitulo.setText(titulo);
        holder.mTextViewGenero.setText(genero);

        int[] imagensTextos = {R.drawable.descritivo, R.drawable.narrativo, R.drawable.expositivo,
                                R.drawable.argumentativo, R.drawable.injuntivo};

        switch (tipologia) {
            case "descritivo":
                holder.mImageView.setImageResource(imagensTextos[0]);
                break;
            case "narrativo":
                holder.mImageView.setImageResource(imagensTextos[1]);
                break;
            case "expositivo":
                holder.mImageView.setImageResource(imagensTextos[2]);
                break;
            case "argumentativo":
                holder.mImageView.setImageResource(imagensTextos[3]);
                break;
            case "injuntivo":
                holder.mImageView.setImageResource(imagensTextos[4]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        //tamanho da lista de itens
        return textoArrayList.size();
    }
}

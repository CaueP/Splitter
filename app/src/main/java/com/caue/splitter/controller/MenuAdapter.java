package com.caue.splitter.controller;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caue.splitter.R;
import com.caue.splitter.model.Produto;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaueGarciaPolimanti on 4/30/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Holder> {
    private static final String MENU_ADAPTER_TAG = "MenuAdapter";
    Context mContext;

    OnListItemSelectedListener mListener = null;

    private ArrayList<Produto> mMenu;

    // constructor
    public MenuAdapter(ArrayList<Produto> lista){
        Log.d(MENU_ADAPTER_TAG, "Entered in MenuAdapter constructor");
        mMenu = lista;//new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_menu_item, null, false);

        // set the Listener
        try {
            //mListener = (OnListItemSelectedListener) mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString() +
                    " must implement OnFragmentInteractionListener");
        }

        return new MenuAdapter.Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        View view = holder.itemView;

        Produto produto = mMenu.get(position);

        //Log.d("JSON (" + position + "): \n", Imovel.imovelToJson(imovel));

        // set values
        String imagemURL = produto.getUrlImagem();
        if (imagemURL != null)
            Glide.with(view.getContext())
                    .load(imagemURL)
                    //.bitmapTransform(new VignetteFilterTransformation(mContext, new PointF(0.5f, 0.5f),new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f))
                    .dontAnimate()
                    //.placeholder(R.drawable.img_temp)
                    .crossFade()
                    .into(holder.foto);
//
//        holder.tipo.setText(imovel.getTipo());
//        DecimalFormat df = new DecimalFormat("###,###,###,###,###");
        holder.preco.setText("R$ " + produto.getValor());

        holder.descricao.setText(produto.getDescricao());
        //holder.endereco.setInputType(TYPE_TEXT_FLAG_CAP_WORDS);

//        holder.nome.setText(imovel.getQtdDormitorio() + " dorms, " +
//                imovel.getQtdVaga() + " vaga, " +
//                imovel.getAreaTotal() + "m\u00B2");

        //preco.setText(Double.toString(imovel.getPrecoVenda()));

    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{


        ImageView foto;
        TextView nome;
        TextView descricao;
        TextView preco;

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //find views
            foto = (ImageView) itemView.findViewById(R.id.image_foto_produto);
            nome = (TextView) itemView.findViewById(R.id.txt_nome_produto);
            preco = (TextView) itemView.findViewById(R.id.txt_preco_produto);
            descricao = (TextView) itemView.findViewById(R.id.txt_descricao_produto);
        }

        @Override
        public void onClick(View view) {
            Produto produto = mMenu.get(getAdapterPosition());
            mListener.onListItemSelected(produto.getCodigo());

            Toast.makeText(view.getContext(),"Codigo do produto clicado" + produto.getCodigo(), Toast.LENGTH_SHORT)
                  .show();
        }


    }

    // Interface para consumir o clique no item e enviar para a MainActivity
    public interface OnListItemSelectedListener {
        public void onListItemSelected(int codImovel);
    }

}

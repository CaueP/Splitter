package com.caue.splitter.controller;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caue.splitter.R;
import com.caue.splitter.model.Produto;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by CaueGarciaPolimanti on 4/30/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Holder> {
    private static final String TAG = "MenuAdapter";
    Context mContext;

    OnItemClickListener mItemClickListener = null;

    private ArrayList<Produto> mMenu;

    // constructor
    public MenuAdapter(ArrayList<Produto> lista){
        Log.d(TAG, "Entered in MenuAdapter constructor");
        mMenu = lista;//new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_menu_item_2, parent, false);

        // set the Listener
        try {
            //mListener = (OnListItemSelectedListener) mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString() +
                    " must implement OnFragmentInteractionListener");
        }

        return new MenuAdapter.Holder(row);
    }


    public class Holder extends RecyclerView.ViewHolder{

        public ImageView foto;
        public TextView nome;
        public TextView descricao;
        public TextView preco;

        public Holder(View itemView) {
            super(itemView);

            //find views
            foto = (ImageView) itemView.findViewById(R.id.image_foto_produto);
            nome = (TextView) itemView.findViewById(R.id.txt_nome_produto);
            preco = (TextView) itemView.findViewById(R.id.txt_preco_produto);
            descricao = (TextView) itemView.findViewById(R.id.txt_descricao_produto);

            // configurando click listener para detectar ação de clique no card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "itemView.onClick (adapter)");
                    if(mItemClickListener != null){
                        mItemClickListener.onItemClick(getLayoutPosition());
                    }
//                    Toast.makeText(view.getContext(),"Item clicado" + getLayoutPosition(), Toast.LENGTH_SHORT)
//                            .show();
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        View view = holder.itemView;

        Produto produto = mMenu.get(position);

        // setting animation
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_recycler_item_show);
        view.startAnimation(animation);

        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.foto.startAnimation(aa1);

        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);


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

        // definindo um nome de transição para animação ao abrir os detalhes de um item do cardapio
        holder.foto.setTransitionName(String.valueOf(produto.getCodigo()));
//
//        holder.tipo.setText(imovel.getTipo());
//        DecimalFormat df = new DecimalFormat("###,###,###,###,###");
        holder.preco.setText("R$ " + String.format(Locale.US,"%.2f", produto.getValor()));
        holder.nome.setText(produto.getNome());

        String editDescription = produto.getDescricao();
        if(editDescription.length() > 30) {
            editDescription = editDescription.substring(0, 20);      // truncate the description
            editDescription = editDescription.concat("...");        // add reticences to the end
        }
        holder.descricao.setText(editDescription);  // set the description on the screen to the truncated description

        //precoPagar.setText(Double.toString(imovel.getPrecoVenda()));

    }

    @Override
    public int getItemCount() {
        return mMenu.size();
    }


    // Interface para consumir o clique no item e enviar para a MainActivity
    public interface OnItemClickListener {
        public void onItemClick(int itemPosition);
    }

    // hook the listener inside the ViewHolder to be used in the Fragment
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}

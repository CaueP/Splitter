package com.caue.splitter.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caue.splitter.R;
import com.caue.splitter.model.Pedido;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Caue Polimanti
 * @version 2.0
 * Created on 5/21/2017.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    private static final String TAG = "OrderAdapter";
    Context mContext;

    OrderAdapter.OnItemClickListener mItemClickListener = null;

    private ArrayList<Pedido> mPedidos;

    // constructor
    public OrderAdapter(ArrayList<Pedido> lista){
        Log.d(TAG, "Entered in OrderAdapter constructor");
        mPedidos = lista;//new ArrayList<>();
    }

    @Override
    public OrderAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_order_item_2, parent, false);

        // set the Listener
        try {
            //mListener = (OnListItemSelectedListener) mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString() +
                    " must implement OnFragmentInteractionListener");
        }

        return new OrderAdapter.Holder(row);
    }


    public class Holder extends RecyclerView.ViewHolder{

        public ImageView foto;
        public TextView nome;
        public TextView quantidade;
        public TextView precoPagar;

        public Holder(View itemView) {
            super(itemView);

            //find views
            foto = (ImageView) itemView.findViewById(R.id.image_foto_produto);
            nome = (TextView) itemView.findViewById(R.id.txt_nome_produto);
            precoPagar = (TextView) itemView.findViewById(R.id.txt_preco_individual);
            quantidade = (TextView) itemView.findViewById(R.id.txt_quantidade);

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
    public void onBindViewHolder(OrderAdapter.Holder holder, int position) {

        View view = holder.itemView;

        Pedido pedido = mPedidos.get(position);

        //Log.d("JSON (" + position + "): \n", Imovel.imovelToJson(imovel));

        // set values
        String imagemURL = pedido.getUrlImagem();
        if (imagemURL != null)
            Glide.with(view.getContext())
                    .load(imagemURL)
                    //.bitmapTransform(new VignetteFilterTransformation(mContext, new PointF(0.5f, 0.5f),new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f))
                    .dontAnimate()
                    //.placeholder(R.drawable.img_temp)
                    .crossFade()
                    .into(holder.foto);

        // definindo um nome de transição para animação ao abrir os detalhes de um item dos pedidos
        holder.foto.setTransitionName(String.valueOf(pedido.getCodigo()));

        holder.precoPagar.setText("R$ " + String.format(Locale.US,"%.2f", pedido.getValorPagar()));
        holder.nome.setText(pedido.getNomeProduto());

        holder.quantidade.setText("x" + pedido.getQtdProduto());  // set the description on the screen to the truncated description

        //precoPagar.setText(Double.toString(imovel.getPrecoVenda()));

    }

    @Override
    public int getItemCount() {
        return mPedidos.size();
    }


    // Interface para consumir o clique no item e enviar para a MainActivity
    public interface OnItemClickListener {
        public void onItemClick(int itemPosition);
    }

    // hook the listener inside the ViewHolder to be used in the Fragment
    public void setOnItemClickListener(final OrderAdapter.OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}

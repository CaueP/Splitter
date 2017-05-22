package com.caue.splitter.controller;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.caue.splitter.R;
import com.caue.splitter.model.Pedido;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Caue Polimanti
 * @version 2.0
 * Created on 5/21/2017.
 */
public class BillAdapter  extends RecyclerView.Adapter<BillAdapter.Holder> {
    private static final String TAG = "BillAdapter";
    Context mContext;

    private ArrayList<Pedido> mPedidos;

    // constructor
    public BillAdapter(ArrayList<Pedido> lista){
        Log.d(TAG, "Entered in BillAdapter constructor");
        mPedidos = lista;//new ArrayList<>();
    }

    @Override
    public BillAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_bill_order_item, null, false);

        // set the Listener
        try {
            //mListener = (OnListItemSelectedListener) mContext;
        }catch (ClassCastException e) {
            throw new ClassCastException(mContext.toString() +
                    " must implement OnFragmentInteractionListener");
        }

        return new BillAdapter.Holder(row);
    }


    public class Holder extends RecyclerView.ViewHolder{

        public ImageView foto;
        public TextView nome;
        public TextView quantidade;
        public TextView preco_total;
        public TextView preco_individual;

        public Holder(View itemView) {
            super(itemView);

            nome = (TextView) itemView.findViewById(R.id.txt_nome_produto);
            quantidade = (TextView) itemView.findViewById(R.id.txt_quantidade);
            preco_total = (TextView) itemView.findViewById(R.id.txt_preco_total);
            preco_individual = (TextView) itemView.findViewById(R.id.txt_preco_individual);
        }
    }

    @Override
    public void onBindViewHolder(BillAdapter.Holder holder, int position) {
        View view = holder.itemView;

        Pedido pedido = mPedidos.get(position);

        holder.nome.setText(pedido.getNomeProduto());
        holder.quantidade.setText("x" + pedido.getQtdProduto());  // set the description on the screen to the truncated description
        holder.preco_total.setText(String.format(Locale.US,"%.2f", pedido.getValorTotal()));
        holder.preco_individual.setText(String.format(Locale.US,"%.2f", pedido.getValorTotal()));   // substituir pelo valor individual
    }

    @Override
    public int getItemCount() {
        return mPedidos.size();
    }

}

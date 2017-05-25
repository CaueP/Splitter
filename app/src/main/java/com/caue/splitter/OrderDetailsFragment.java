package com.caue.splitter;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Pedido;

/**
 * Created by CaueGarciaPolimanti on 5/21/2017.
 */

public class OrderDetailsFragment extends Fragment {

    private static final String TAG = "OrderDetailsFragment";
    View rootView;

    ImageView vFoto;
    TextView vNome;
    TextView vDescricao;
    TextView vPreco;
    TextView vObservacao;
    TextView vQuantidade;
    TextView txtStatusPedido;
    Button btnCancelOrder;

    // Dado recebido da activity
    Pedido pedido = null;

    // Dados definidos pelo usuário
    int qtdPedido = 0;
    double totalPedido = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_order_details, container, false);

        // find views
        vFoto = (ImageView) rootView.findViewById(R.id.image_imagem_produto);
        vNome = (TextView) rootView.findViewById(R.id.txt_nome_produto);
        vDescricao = (TextView) rootView.findViewById(R.id.txt_descricao_produto);
        vPreco = (TextView) rootView.findViewById(R.id.txt_preco_individual);
        vQuantidade = (TextView) rootView.findViewById(R.id.txt_qtd_produto);
        vObservacao = (TextView) rootView.findViewById(R.id.txt_observacao_pedido);
        txtStatusPedido = (TextView) rootView.findViewById(R.id.txt_status_pedido);

        btnCancelOrder = (Button) rootView.findViewById(R.id.btn_cancelar);

        final Bundle bundle = getArguments();
        if(bundle != null) {
            pedido = (Pedido) bundle.getSerializable(Constants.KEY.PRODUTO_DATA);
            fillView();
        } else{
            Log.d(TAG, "Bundle com lista de pedidos vazia");
        }

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                } catch(Exception exc) {
                    Log.d(TAG, exc.toString());
                }
            }
        });

        return rootView;
    }

    /**
     * Popula a view com os dados do produto
     */
    private void fillView(){
        if (pedido.getUrlImagem()!= null)
            Glide.with(rootView.getContext())
                    .load(pedido.getUrlImagem())
                    //.bitmapTransform(new VignetteFilterTransformation(mContext, new PointF(0.5f, 0.5f),new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f))
                    .dontAnimate()
                    //.placeholder(R.drawable.img_temp)
                    .crossFade()
                    .into(vFoto);
        vNome.setText(pedido.getNomeProduto());
        //vDescricao.setText(pedido.getDescObservacao());

        switch (pedido.getCodStatusPedido()) {
            case Constants.STATUS_PEDIDO.AGUARDANDO:
                txtStatusPedido.setText(R.string.order_status_waiting);
                //txtStatusPedido.setTextColor(Color.YELLOW);
                break;
            case Constants.STATUS_PEDIDO.ENTREGUE:
                txtStatusPedido.setText(R.string.order_status_delivered);
               //txtStatusPedido.setTextColor(Color.GREEN);
                break;
            case Constants.STATUS_PEDIDO.CANCELADO:
                txtStatusPedido.setText(R.string.order_status_canceled);
                //txtStatusPedido.setTextColor(Color.RED);
                break;
            case Constants.STATUS_PEDIDO.FINALIZADO:
                txtStatusPedido.setText(R.string.order_status_done);
                //txtStatusPedido.setTextColor(Color.BLUE);
                break;
        }
        vDescricao.setText("");
        vQuantidade.setText("x" + pedido.getQtdProduto());
        vObservacao.setText(pedido.getDescObservacao());

        // using Resources to set values of a resource string
        Resources res = getResources();
        String priceText = res.getString(R.string.product_price, pedido.getValorPagar());
        vPreco.setText(priceText);
    }
}

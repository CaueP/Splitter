package com.caue.splitter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Pedido;
import com.caue.splitter.model.Produto;

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
    EditText vObservacao;
    Spinner vQuantidade;
    TextView vTotal;
    Button btnCancelOrder;

    // Dado recebido da activity
    Pedido pedido = null;

    // Dados definidos pelo usuário
    int qtdPedido = 0;
    double totalPedido = 0.0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_product_details, container, false);

        // find views
        vFoto = (ImageView) rootView.findViewById(R.id.img_foto_produto);
        vNome = (TextView) rootView.findViewById(R.id.txt_nome_produto);
        vDescricao = (TextView) rootView.findViewById(R.id.txt_descricao_produto);
        vPreco = (TextView) rootView.findViewById(R.id.txt_preco_produto);
        vObservacao = (EditText) rootView.findViewById(R.id.txt_input_observacao_produto);
        vQuantidade = (Spinner) rootView.findViewById(R.id.spn_qtd_produto);
        btnCancelOrder = (Button) rootView.findViewById(R.id.btn_send_order);
        vTotal = (TextView) rootView.findViewById(R.id.order_total);

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
                    // obter o valor selecionado
                    String qtdSelecionada  = (String) vQuantidade.getSelectedItem();
                    qtdPedido = Integer.valueOf(qtdSelecionada);
                    // definir string de observacao
                    String txtObservacao = "";
                    if (!vObservacao.getText().toString().isEmpty())
                        txtObservacao = vObservacao.getText().toString();
                } catch(Exception exc) {
                    Toast.makeText(getActivity(), R.string.msg_select_quantity, Toast.LENGTH_SHORT).show();
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
        vDescricao.setText(pedido.getDescObservacao());

        // using Resources to set values of a resource string
        Resources res = getResources();
        String priceText = res.getString(R.string.product_price, pedido.getValorTotal());
        vPreco.setText(priceText);
        String totalText = res.getString(R.string.order_total, 0.0);
        vTotal.setText(totalText);
    }
}

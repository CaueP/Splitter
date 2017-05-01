package com.caue.splitter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Produto;

import java.util.Locale;

/**
 * Created by CaueGarciaPolimanti on 5/1/2017.
 */

public class ProductDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ProductDetailsFragment";
    View rootView;

    ImageView vFoto;
    TextView vNome;
    TextView vDescricao;
    TextView vPreco;
    EditText vObservacao;
    Spinner vQuantidade;
    TextView vTotal;
    Button vOrder;
    Button vCancel;

    // Dado recebido da activity
    Produto produto = null;

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
        vQuantidade.setOnItemSelectedListener(this);
        vOrder = (Button) rootView.findViewById(R.id.btn_send_order);
        vCancel = (Button) rootView.findViewById(R.id.btn_cancel_order);
        vTotal = (TextView) rootView.findViewById(R.id.order_total);

        Bundle bundle = getArguments();
        if(bundle != null) {
            produto = (Produto) bundle.getSerializable(Constants.KEY.PRODUTO_DATA);
            fillView();
        } else{
            Log.d(TAG, "Bundle com lista de produtos vazia");
        }

        return rootView;
    }

    /**
     * Popula a view com os dados do produto
     */
    private void fillView(){
        if (produto.getUrlImagem()!= null)
            Glide.with(rootView.getContext())
                    .load(produto.getUrlImagem())
                    //.bitmapTransform(new VignetteFilterTransformation(mContext, new PointF(0.5f, 0.5f),new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f))
                    .dontAnimate()
                    //.placeholder(R.drawable.img_temp)
                    .crossFade()
                    .into(vFoto);
        vNome.setText(produto.getNome());
        vDescricao.setText(produto.getDescricao());

        // using Resources to set values of a resource string
        Resources res = getResources();
        String priceText = res.getString(R.string.product_price, produto.getValor());
        vPreco.setText(priceText);
        String totalText = res.getString(R.string.order_total, 0.0);
        vTotal.setText(totalText);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        Log.d(TAG, "onItemSelected");
        Resources res = getResources();
        String totalText = "-";
        //adapterView.getItemAtPosition(pos);
        try {
            String qtdSelecionada = (String) adapterView.getItemAtPosition(pos);
            qtdPedido = Integer.valueOf(qtdSelecionada);
            totalPedido = qtdPedido * produto.getValor();
            totalText= res.getString(R.string.order_total, totalPedido);
        } catch(Exception exc){
            totalText = "-";
            Log.d(TAG, exc.toString());
        }
        vTotal.setText(totalText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

package com.caue.splitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.caue.splitter.controller.BillAdapter;
import com.caue.splitter.controller.OrderAdapter;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Cartao;
import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Conta;
import com.caue.splitter.model.Pedido;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by CaueGarciaPolimanti on 5/21/2017.
 */

public class BillPaymentFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    View rootView;


    ArrayList<Pedido> mListaPedido = null;
    private RecyclerView mRecyclerView;
    private BillAdapter mBillAdapter;
    private TextView tvTotalConta;
    private Button btnPagarConta;

    // dados
    private Conta conta = null;
    Checkin checkin = null;

    // dados inseridos pelo usuario
    Cartao cartao;
    private EditText nomeCartao;
    private EditText numCartao;
    private EditText dataValidade;
    private EditText codCVV;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_bill_payment, container, false);


        // find views
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        tvTotalConta = (TextView) rootView.findViewById(R.id.txt_total_conta);

        nomeCartao = (EditText) rootView.findViewById(R.id.txt_input_nome_cartao);
        numCartao = (EditText) rootView.findViewById(R.id.txt_input_numero_cartao);
        dataValidade = (EditText) rootView.findViewById(R.id.txt_input_data_vencimento);
        codCVV = (EditText) rootView.findViewById(R.id.txt_input_cvc);

        btnPagarConta = (Button) rootView.findViewById(R.id.btn_realizar_pagamento);

        Bundle bundle = getArguments();
        if(bundle != null) {
            checkin = (Checkin) bundle.getSerializable(Constants.KEY.CHECKIN_DATA);
            conta = (Conta) bundle.getSerializable(Constants.KEY.CONTA_DATA);
            if(mBillAdapter != null){
                mBillAdapter = null;
            }
            if (conta != null) {
                setDataRecyclerViewer();
                fillViews();
            }
        } else{
            Log.d(TAG, "Bundle com lista de pedidos vazia");
        }

        btnPagarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String stringValidade = dataValidade.getText().toString();
                    String[] arrayValidade =  stringValidade.split("/");

                    Cartao cartaoPagamento = new Cartao(nomeCartao.getText().toString(),
                           Long.parseLong(numCartao.getText().toString()),
                            Integer.parseInt(arrayValidade[0]),
                            Integer.parseInt(arrayValidade[1]),
                            Integer.parseInt(codCVV.getText().toString()));

                    payBill(cartaoPagamento);
                } catch(Exception exc) {
                    switch (exc.getMessage()){
                        case Constants.CARTAO_EXCEPTION.CartaoVencido:
                            dataValidade.setError(getString(R.string.msg_expired_card));
                            break;
                        case Constants.CARTAO_EXCEPTION.CVVInvalido:
                            codCVV.setError(getString(R.string.msg_invalid_cvv));
                            break;
                        case Constants.CARTAO_EXCEPTION.DataValidadeInvalida:
                            dataValidade.setError(getString(R.string.msg_invalid_dexp));
                            break;
                        case Constants.CARTAO_EXCEPTION.NomeInvalido:
                            nomeCartao.setError(getString(R.string.msg_invalid_name));
                            break;
                        case Constants.CARTAO_EXCEPTION.NumeroInvalido:
                            numCartao.setError(getString(R.string.msg_invalid_card_number));
                            break;
                        default:
                            Toast.makeText(getActivity(), "Favor preencher os dados do cartão", Toast.LENGTH_LONG).show();
                            break;
                    }
                    Log.d(TAG, exc.toString());
                }
            }
        });

        return rootView;
    }


    private void setDataRecyclerViewer() {
        mBillAdapter = new BillAdapter(conta.getPedidos());
        mRecyclerView.setAdapter(mBillAdapter);
    }

    private void fillViews() {
        tvTotalConta.setText("R$ " + String.format(Locale.US,"%.2f", conta.getTotal()));
    }

    public void responsePagarContaReceived (Conta contaResposta) {
    }

    private void payBill(Cartao cartao) {
        Toast.makeText(getActivity(), "Conta paga com sucesso", Toast.LENGTH_LONG).show();
        //conta.pagar(this, cartao);
    }
}

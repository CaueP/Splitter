package com.caue.splitter;

import android.content.Context;
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
import android.widget.TextView;

import com.caue.splitter.controller.OrderAdapter;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Conta;
import com.caue.splitter.model.Pedido;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Caue Polimanti
 * @version 2.0
 *          Created on 5/20/2017.
 */
public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    View rootView;


    ArrayList<Pedido> mListaPedido = null;
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private TextView tvTotalConta;
    private Button btnFecharConta;

    // dados
    private Conta conta = null;
    Checkin checkin = null;

    // listener implementado na Activty
    OrderFragment.OnListItemSelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        Log.d("OrderFragment", "Entered in onAttach");
        super.onAttach(context);
        try {
            mListener = (OrderFragment.OnListItemSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_checkedin_orders, container, false);


        // find views
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        tvTotalConta = (TextView) rootView.findViewById(R.id.txt_total_conta);
        btnFecharConta = (Button) rootView.findViewById(R.id.btn_fechar_conta);

        Bundle bundle = getArguments();
        if (bundle != null) {
            checkin = (Checkin) bundle.getSerializable(Constants.KEY.CHECKIN_DATA);
            if (mOrderAdapter != null) {
                mOrderAdapter = null;
            }
            if (checkin != null) {
                if (conta == null) {
                    conta = new Conta(checkin.getMesa().getCodEstabelecimento(), checkin.getComanda().getCodComanda(), checkin.getMesa().getNrMesa());
                    conta.consultar(this);
                } else {
                    populateViews();
                }
            }
        } else {
            Log.d(TAG, "Bundle com lista de produtos vazia");
        }

        btnFecharConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    closeBill();
                } catch (Exception exc) {
                    Log.d(TAG, exc.toString());
                }
            }
        });

        return rootView;
    }


    private void setDataRecyclerViewer() {
        mOrderAdapter = new OrderAdapter(conta.getPedidos());
        mRecyclerView.setAdapter(mOrderAdapter);
        mOrderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int itemPosition) {
                //Toast.makeText(getActivity(), "onListItemSelected", Toast.LENGTH_SHORT).show();
                mListener.onPedidoSelected(conta, itemPosition);
            }
        });
    }

    public void responseConsultarContaReceived(Conta contaResposta) {
        if (contaResposta != null) {
            conta = contaResposta;
            populateViews();
        }
    }

    private void populateViews() {
        setDataRecyclerViewer();
        tvTotalConta.setText("R$ " + String.format(Locale.US, "%.2f", conta.getTotalIndividual()));
    }

    private void closeBill() {
        Conta fecharConta = new Conta(checkin.getMesa().getCodEstabelecimento(), checkin.getComanda().getCodComanda(), checkin.getMesa().getNrMesa());
        fecharConta.fechar(this);
    }

    public void responseFecharContaReceived(Conta contaFechada) {
        if (contaFechada != null) {
            conta = contaFechada;
            setDataRecyclerViewer();
            Log.d(TAG, "Total: " + conta.getTotalMesa());
            tvTotalConta.setText("R$ " + String.format(Locale.US, "%.2f", conta.getTotalIndividual()));
            mListener.onCloseBillClicked(contaFechada);
        }
    }

    /**
     * Interface que será implementada pela CheckedInActivity para chamada do detalhe dos items
     */
    public interface OnListItemSelectedListener {
        /**
         * Função para enviar o pedido clicado à Activity
         *
         * @param itemPosition Posição do produto na lista
         */
        public void onPedidoSelected(Conta conta, int itemPosition);

        /**
         * Função para enviar a conta fechada à activity
         *
         * @param contaFechada Conta fechada recebida
         */
        public void onCloseBillClicked(Conta contaFechada);
    }
}

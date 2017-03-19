package com.caue.splitter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor Noronha
 * @author Caue Polimanti
 * @version 1.0
 * Created on 25/09/2016.
 */
public class NewOrderFragment extends Fragment {

    View rootView = null;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static NewOrderFragment newInstance(int sectionNumber) {
        NewOrderFragment fragment = new NewOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    // set the layout
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        Log.d("FrontPageFragment", "NewOrderFragment just entered in its onCreateView");

        int option = getArguments().getInt(ARG_SECTION_NUMBER);    // save the arguments to
        // restore the fragments opened on this Fragment


        // loads the fragment selected (or the restored one)
        switch (option){
            case R.id.new_order_fragment:
                rootView = inflater.inflate(R.layout.fragment_new_order, container, false);
                break;
        }

        Spinner spnOrderType = (Spinner) rootView.findViewById(R.id.spn_OrderType);
        Spinner spnProduct = (Spinner) rootView.findViewById(R.id.spn_Product);
        Spinner spnQuantity = (Spinner) rootView.findViewById(R.id.spn_Quantidade);

        List<String> orderType;
        List<String> product;
        List<String> quantity;
        ArrayAdapter<String> adapter;

        orderType = new ArrayList<String>();
        orderType.add("Bebidas");
        orderType.add("Lanches");
        orderType.add("Petiscos");
        orderType.add("Pratos");
        orderType.add("Sobremesas");

        product = new ArrayList<String>();
        product.add("X-Burger");
        product.add("X-Salada");
        product.add("X-Tudo");

        quantity= new ArrayList<String>();
        quantity.add("1");
        quantity.add("2");
        quantity.add("3");
        quantity.add("4");
        quantity.add("5");

        adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_dropdown_item, orderType);
        spnOrderType.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_dropdown_item, product);
        spnProduct.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_dropdown_item, quantity);
        spnQuantity.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.newOrderTitle);
    }

}

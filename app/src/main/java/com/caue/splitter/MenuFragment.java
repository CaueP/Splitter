package com.caue.splitter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.caue.splitter.controller.MenuAdapter;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Produto;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by CaueGarciaPolimanti on 4/30/2017.
 */

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    View rootView;

    ArrayList<Produto> mListaProduto = null;
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;

    // listener implementado na Activty
    OnListItemSelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        Log.d("MenuFragment", "Entered in onAttach");
        super.onAttach(context);
        try {
            mListener = (OnListItemSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_checkedin_menu, container, false);

        // find views
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        Bundle bundle = getArguments();
        if (bundle != null) {
            mListaProduto = (ArrayList<Produto>) bundle.getSerializable(Constants.KEY.CARDAPIO_DATA);
            if (mMenuAdapter != null) {
                mMenuAdapter = null;
            }
            setDataRecyclerViewer();
        } else {
            Log.d(TAG, "Bundle com lista de produtos vazia");
        }

        mMenuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int itemPosition) {
                //Toast.makeText(getActivity(), "onListItemSelected", Toast.LENGTH_SHORT).show();
                mListener.onListItemSelected(itemPosition);
            }
        });


        return rootView;
    }

    private void setDataRecyclerViewer() {
        mMenuAdapter = new MenuAdapter(mListaProduto);
        mRecyclerView.setAdapter(mMenuAdapter);
    }

    /**
     * Interface que será implementada pela CheckedInActivity para chamada do detalhe dos items
     */
    public interface OnListItemSelectedListener {
        /**
         * Função para enviar o produto clicado à Activity
         *
         * @param itemPosition Posição do produto na lista
         */
        public void onListItemSelected(int itemPosition);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);        //Enabling OptionMenu on Action bar
    }

    // inflate the menu and define the actions
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");

        if (menu.findItem(R.id.action_search) == null) {
            Log.d(TAG, "Inflando action_search");
            inflater.inflate(R.menu.menu_menu_fragment, menu);    // add action items inside fragment
        }

        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    int position = findMoviePosition(query);
                    if (position >= 0)
                        mRecyclerView.scrollToPosition(position);
                    else
                        Toast.makeText(getContext(), "Não encontrado", Toast.LENGTH_LONG).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    // find the movie position based on the input text in the search view
    public int findMoviePosition(String query) {
        int position = -1;
        for (int i = 0; i < mMenuAdapter.getItemCount(); i++) {
            Produto produto = mListaProduto.get(i);

            String name = produto.getNome().toLowerCase();
            query = query.toLowerCase();
            if (name.contains(query)) {
                position = i;
                i = mMenuAdapter.getItemCount() + 1;
            }
            //mRecyclerViewAdapter.notifyItemChanged(i);
        }
        return position;
    }
}

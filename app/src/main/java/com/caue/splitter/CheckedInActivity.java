package com.caue.splitter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Conta;
import com.caue.splitter.model.Mesa;
import com.caue.splitter.model.Pedido;
import com.caue.splitter.model.Produto;
import com.caue.splitter.model.Usuario;
import com.caue.splitter.model.services.MesaClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity exibida quando o usuário realizar check-in em um estabelecimento
 *
 * @author Caue Polimanti
 * @version 1.0
 *          Created on 4/30/2017.
 */
public class CheckedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuFragment.OnListItemSelectedListener,
        ProductDetailsFragment.OnOrderListener,
        OrderFragment.OnListItemSelectedListener {

    // custom toolbars and navigation drawer
    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    // Log tags
    private static final String TAG = "CheckedInActivity";

    @BindView(R.id.progressBar_loading)
    ProgressBar progressBarLoading;

    @BindView(android.R.id.content)
    View mContentView;

    Fragment mContent;      // Fragment object that will receive the current Fragment on the screen
    // in order to be restored when the activity is destroyed, after
    // rotating the screen

    // Dados da activity anterior
    Usuario user = null;
    Checkin checkinResponse = null;
    Conta conta = null;

    // Cardapio do estabelecimento
    ArrayList<Produto> cardapio = null;

    // Fragment IDs
    private static final String CHECKIN_FRAGMENT_TAG = "checkin_fragment";

    /**
     * The system "short" animation time duration, in milliseconds. This duration is ideal for
     * subtle animations or animations that occur very frequently.
     */
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = findViewById(android.R.id.content);
        // Initially hide the content view.
        //mContentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
//        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//        Slide slide = new Slide(Gravity.RIGHT);
//        getWindow().setReturnTransition(slide);
//
//
        setContentView(R.layout.activity_checkedin);
        ButterKnife.bind(this);

        // Inicializando a Toolbar and a configurando como a ActionBar
        toolbar = (Toolbar) findViewById(R.id.checkedin_page_toolbar);
        setSupportActionBar(toolbar);

        // Inicializando a NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view_checkedin);
        navigationView.setNavigationItemSelectedListener(this);   // setando o listener do fragment

        // Inicializando Drawer Layout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };

        // Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // calling sync state is necessary or else hamburger icon won't show up
        actionBarDrawerToggle.syncState();


        // getting data from intent
        Bundle bundle = getIntent().getExtras();
        Log.d(TAG, "Bundle String:");
        Log.d(TAG, bundle.toString());
        if (bundle != null) {
            user = (Usuario) bundle.getSerializable(Constants.KEY.USER_DATA);
            checkinResponse = (Checkin) bundle.getSerializable(Constants.KEY.CHECKIN_DATA);
            Log.d(TAG, checkinResponse.toString());
        } else {
            Log.d(TAG, "Bundle NOT FOUND");
        }

        if (savedInstanceState == null) {
            if (checkinResponse.isPrimeiroUsuario()) {
                getTipoDivisaoMesa();
            } else {
                carregarFragmentCheckedIn();
            }
            carregarCardapio();
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mContentView, errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

    @MainThread
    private void showSnackbarMessage(String message) {
        Snackbar.make(mContentView, message, Snackbar.LENGTH_LONG)
                .show();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//        ImageView mUserProfilePicture = (ImageView) findViewById(R.id.user_profile_picture);
        TextView mUserDisplayName = (TextView) this.findViewById(R.id.user_display_name);
        TextView mUserEmail = (TextView) findViewById(R.id.user_email);

        // can be changed to restaurant's picture
//        if (userProfilePictureUri != null) {
//            Glide.with(this)
//                    .load(userProfilePictureUri)
//                    .fitCenter()
//                    .into(mUserProfilePicture);
//        }

        mUserDisplayName.setText("Estabelecimento: " + checkinResponse.getMesa().getCodEstabelecimento());
        mUserEmail.setText("Mesa: " + checkinResponse.getMesa().getNrMesa());

        return super.onPrepareOptionsMenu(menu);
    }

    // inflate the actionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionview_checkedin, menu);
        return true;
    }

    // defines the actions when a menu item is clicked
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.menu:
                Toast.makeText(CheckedInActivity.this, R.string.menu, Toast.LENGTH_SHORT).show();

                Fragment menuFragment = new MenuFragment();
                if (cardapio != null) {   // se o cardapio foi baixado com sucesso
                    Bundle menuBundle = new Bundle();
                    menuBundle.putSerializable(Constants.KEY.CARDAPIO_DATA, cardapio);
                    //menuBundle.putParcelableArrayList(Constants.KEY.CARDAPIO_DATA, cardapio);
                    //menuBundle.putString(Constants.KEY.CARDAPIO_DATA, new Gson().toJson(cardapio));
                    menuFragment.setArguments(menuBundle);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        //.setCustomAnimations(R.anim.move_up)
                        .replace(R.id.content_frame, menuFragment)
                        .addToBackStack(null)        // add to back stack
                        .commit();
                break;
            case R.id.orders:
                Toast.makeText(CheckedInActivity.this, R.string.orders_menu, Toast.LENGTH_SHORT).show();

                Fragment orderFragment = new OrderFragment();
                if (checkinResponse != null) {
                    Bundle orderBundle = new Bundle();
                    orderBundle.putSerializable(Constants.KEY.CHECKIN_DATA, checkinResponse);
                    orderFragment.setArguments(orderBundle);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, orderFragment)
                        .addToBackStack(null)        // add to back stack
                        .commit();
                break;
            case R.id.participants:
                Toast.makeText(CheckedInActivity.this, R.string.participants_menu, Toast.LENGTH_SHORT).show();
                Fragment participantsFragment = new ParticipantsFragment();
                if (checkinResponse != null) {
                    Bundle participantBundle = new Bundle();
                    participantBundle.putSerializable(Constants.KEY.MESA_DATA, checkinResponse.getMesa());
                    participantsFragment.setArguments(participantBundle);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, participantsFragment)
                        .addToBackStack(null)        // add to back stack
                        .commit();
                break;
//            case R.id.about:
//                Toast.makeText(CheckedInActivity.this, R.string.about_splitter, Toast.LENGTH_SHORT).show();
//                //getSupportFragmentManager().beginTransaction()
//                //       .replace(R.id.container, AboutMeFragment.newInstance(R.id.about_me_fragment))
//                //     .addToBackStack(null)
//                //   .commit();
//                break;
            default:
                Toast.makeText(CheckedInActivity.this, "Option not found", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    Callbacks
     */

    /**
     * Callback de resposta para a consulta do cardapio
     *
     * @param cardapioRecebido Cardapio recebido da consulta
     */
    public void responseCardapioReceived(ArrayList<Produto> cardapioRecebido) {
        Log.d(TAG, "Cardapio disponivel para consulta");
        cardapio = cardapioRecebido;
    }

    Snackbar snackBarMessage;

    /**
     * Callback de resposta para a realização de pedido
     */
    public void responseRealizarPedidoReceived(Pedido pedido) {
        Resources res = getResources();
        String msgResponse = "-";
        if (pedido != null && pedido.getCodigo() >= 0) {
            msgResponse = res.getString(R.string.msg_product_ordered, pedido.getCodigo());
        } else {
            msgResponse = res.getString(R.string.msg_product_order_failed);
        }
        showSnackbarMessage(msgResponse);

        snackBarMessage = Snackbar.make(mContentView, msgResponse, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok_string, new MyOKListener());
        snackBarMessage.setActionTextColor(getResources().getColor(R.color.white));

        snackBarMessage.show();
    }

    public class MyOKListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            snackBarMessage.dismiss();
            // Code to undo the user's last action
        }
    }

    /**
     * Abre a caixa de diálogo para obter o tipo de divisão da mesa
     */
    private void getTipoDivisaoMesa() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckedInActivity.this);

        builder.setTitle(R.string.split_method_dialog_title)
                .setItems(R.array.tipo_divisao, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selectedOpt) {
                        selectedOpt++;
//                        Toast.makeText(MainPageActivity.this, "Opt: " + selectedOpt, Toast.LENGTH_SHORT).show();

                        switch (selectedOpt) {
                            case Constants.TIPO_DIVISAO_PEDIDOS.MESA:
                                checkinResponse.getMesa().setTipoDivisao(selectedOpt);
                                atualizarTipoDivisao();
                                break;
                            case Constants.TIPO_DIVISAO_PEDIDOS.INDIVIDUAL:
                                checkinResponse.getMesa().setTipoDivisao(selectedOpt);
                                atualizarTipoDivisao();
                                break;
                            default:
                                Toast.makeText(CheckedInActivity.this, R.string.msg_split_method_invalid, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setCancelable(false)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (checkinResponse.getMesa().getTipoDivisao() != Constants.TIPO_DIVISAO_PEDIDOS.INDIVIDUAL && checkinResponse.getMesa().getTipoDivisao() != Constants.TIPO_DIVISAO_PEDIDOS.MESA) {
                            getTipoDivisaoMesa();
                        }
                    }
                });

//        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                Toast.makeText(MainPageActivity.this, "Opcao selecionada", Toast.LENGTH_SHORT).show();
//                checkin.realizarCheckin(MainPageActivity.this);
//            }
//        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * Atualiza o tipo de divisão da mesa
     */
    private void atualizarTipoDivisao() {
        Log.d(TAG, "Carregando lista de participantes");
        // criação do serviço
        MesaClient service = ServiceGenerator.createService(MesaClient.class);
        Call<Mesa> listCall = service.atualizarTipoDivisao(checkinResponse.getMesa().getCodEstabelecimento(), checkinResponse.getMesa().getNrMesa(), checkinResponse.getMesa().getTipoDivisao());

        // callback ao receber a resposta da API
        Callback<Mesa> atualizaTipoDivisaoMesaCallback = new Callback<Mesa>() {

            @Override
            public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                Log.d(TAG, "entered in onResponse");
                if (response.isSuccessful()) {
                    Log.d(TAG, "isSuccessful");
                    checkinResponse.getMesa().setTipoDivisao(response.body().getTipoDivisao());
                    carregarFragmentCheckedIn();
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    if (response.code() == 404) {    // usuario nao cadastrado
                        Log.d(TAG, "Parâmetros incorretos" + response.body());
                    }

                }
            }

            @Override
            public void onFailure(Call<Mesa> call, Throwable t) {
                Log.e(TAG, "onFailure - Ocorreu um erro ao chamar a API");
            }
        };

        // call
        listCall.enqueue(atualizaTipoDivisaoMesaCallback);
    }

    private void carregarFragmentCheckedIn() {
        // Loads CheckedInFragment
        if (user != null && checkinResponse != null) {
            mContent = CheckedInFragment.newInstance(R.id.checked_in_fragment, user, checkinResponse);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mContent, CHECKIN_FRAGMENT_TAG)
                    .commit();
        }
        progressBarLoading.setVisibility(View.INVISIBLE);
    }

    private void carregarCardapio() {
        Produto produto = new Produto();
        produto.obterCardapio(checkinResponse.getMesa().getCodEstabelecimento(), this);
    }

    /*
    Interfaces
     */

    /**
     * Implementação da Interface do MenuFragment para realizar a chamada dos Detalhes do Produto
     *
     * @param itemPosition Posição do produto na lista
     */
    @Override
    public void onListItemSelected(int itemPosition) {
        if (cardapio != null) {
            Produto produtoSelecionado = cardapio.get(itemPosition);

            Fragment productDetailsFragment = new ProductDetailsFragment();
            Bundle productBundle = new Bundle();
            productBundle.putSerializable(Constants.KEY.PRODUTO_DATA, produtoSelecionado);
            productDetailsFragment.setArguments(productBundle);
            // realização transição do fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, productDetailsFragment)
                    .addToBackStack(null)        // add to back stack
                    .commit();
        }
    }

    /**
     * Implementação da interface para realizar o pedido de um produto
     *
     * @param codProduto codigo do produto
     * @param qtdProduto quantidade de itens do produto
     * @param obs        observacao do cliente
     */
    @Override
    public void orderProduct(int codProduto, int qtdProduto, String obs) {
        Pedido pedido = new Pedido(checkinResponse.getMesa().getCodEstabelecimento(), checkinResponse.getMesa().getNrMesa(), checkinResponse.getComanda().getCodComanda(), codProduto, qtdProduto, obs);
        // realizar pedido
        pedido.pedir(this);
    }

    /**
     * Implementação da interface quando um pedido é selecionado
     *
     * @param itemPosition Posição do produto na lista
     */
    @Override
    public void onPedidoSelected(Conta conta, int itemPosition) {
        this.conta = conta;
        if (conta != null) {
            Pedido pedidoSelecionado = conta.getPedidos().get(itemPosition);

            Fragment pedidoDetailsFragment = new OrderDetailsFragment();
            Bundle productBundle = new Bundle();
            productBundle.putSerializable(Constants.KEY.PRODUTO_DATA, pedidoSelecionado);
            pedidoDetailsFragment.setArguments(productBundle);
            // realização transição do fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, pedidoDetailsFragment)
                    .addToBackStack(null)        // add to back stack
                    .commit();
        }
    }

    /**
     * Implementação da interface quando é selecionada a tela para fechar conta
     *
     * @param contaFechada Conta fechada recebida
     */
    @Override
    public void onCloseBillClicked(Conta contaFechada) {
        this.conta = contaFechada;

        if (conta != null && checkinResponse != null) {
            Fragment billPaymentFragment = new BillPaymentFragment();
            Bundle billBundle = new Bundle();
            billBundle.putSerializable(Constants.KEY.CHECKIN_DATA, checkinResponse);
            billBundle.putSerializable(Constants.KEY.CONTA_DATA, conta);
            billPaymentFragment.setArguments(billBundle);
            // realização transição do fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, billPaymentFragment)
                    .addToBackStack(null)        // add to back stack
                    .commit();
        }
    }


    /**
     * Overriding onBackPressed para alterar funcionalidade ao pressionar o botão para voltar
     */
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            Fragment checkinFrag = getSupportFragmentManager().findFragmentByTag(CHECKIN_FRAGMENT_TAG);

            Log.d("onBackPressed", "Quantidade de fragments na stack: " + fragments);
            if (checkinFrag != null && fragments == 0) {  // se o checkin frag estiver na stack e houver apenas 1 fragment na stack, nao retornar
                Log.d("onBackPressed", "Checkin fragment está na stack");
                //Toast.makeText(this, R.string.accound_should_be_closed, Toast.LENGTH_SHORT).show();
                showSnackbar(R.string.accound_should_be_closed);
                //finish();
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

}

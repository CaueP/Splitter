package com.caue.splitter;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Mesa;
import com.caue.splitter.model.Usuario;
import com.caue.splitter.model.services.MesaClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cgpolim on 12/04/2017.
 */

public class CheckedInFragment extends Fragment {
    private static final String TAG = "CheckedInFragment";
    @BindView(R.id.estabelecimento_place_name)
    TextView placeName;

    @BindView(R.id.qr_code_image)
    ImageView qrCodeImage;

    @BindView(R.id.message_owner)
    TextView message;

    public static CheckedInFragment newInstance(int sectionNumber, Usuario usuario, Checkin checkInResponse) {
        CheckedInFragment fragment = new CheckedInFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(Constants.KEY.USER_DATA, usuario);
        args.putSerializable(Constants.KEY.CHECKIN_DATA, checkInResponse);
        fragment.setArguments(args);
        return fragment;
    }

    public CheckedInFragment() {
    }

    View rootView = null;

    Usuario usuario;
    Checkin checkinResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_checked_in, container, false);
        ButterKnife.bind(this, rootView);

        // get user and checkin data
        usuario = (Usuario) getArguments().getSerializable(Constants.KEY.USER_DATA);
        checkinResponse = (Checkin) getArguments().getSerializable(Constants.KEY.CHECKIN_DATA);

        placeName.setText(checkinResponse.getMesa().getCodEstabelecimento());

        Log.d(TAG, checkinResponse != null ? checkinResponse.toString() : "CheckinResponse == null");

        // Se houver qrCodeOcupado, gera o QRCode
        if (checkinResponse != null && checkinResponse.getMesa().getQrCodeOcupado() != null) {
            message.setText(R.string.msg_show_qrcode);
            showQRCode(checkinResponse.getMesa().getQrCodeOcupado());
        } else {
            message.setVisibility(View.INVISIBLE);
            qrCodeImage.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }

    private void showQRCode(String qrCodeString) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeString, BarcodeFormat.QR_CODE, 150, 150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}

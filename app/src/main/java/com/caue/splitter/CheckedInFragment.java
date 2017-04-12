package com.caue.splitter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Usuario;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cgpolim on 12/04/2017.
 */

public class CheckedInFragment extends Fragment {
    @BindView(R.id.estabelecimento_place_name)
    TextView placeName;

    @BindView(R.id.qr_code_image)
    ImageView  qrCodeImage;

    @BindView(R.id.message_owner)
    TextView message;

    public static CheckedInFragment newInstance(int sectionNumber, Usuario usuario, Checkin checkInResponse) {
        CheckedInFragment fragment = new CheckedInFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable("UserData",usuario);
        args.putSerializable("CheckIn",checkInResponse);
        fragment.setArguments(args);
        return fragment;
    }

    public CheckedInFragment(){
    }

    View rootView = null;

    Usuario usuario;
    Checkin checkinResponse;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_checked_in, container, false);
        ButterKnife.bind(this, rootView);

        // get user and checkin data
        usuario = (Usuario) getArguments().getSerializable("UserData");
        checkinResponse = (Checkin) getArguments().getSerializable("CheckIn");

        placeName.setText("Bar");

        // Se houver qrCodeOcupado, gera o QRCode
        if(checkinResponse.getMesa().getQrCodeOcupado() != null){
            message.setText("Mostre o QR Code abaixo para seus amigos");
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try{
                BitMatrix bitMatrix = multiFormatWriter.encode(checkinResponse.getMesa().getQrCodeOcupado(), BarcodeFormat.QR_CODE,200,200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                qrCodeImage.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                e.printStackTrace();
            }
        } else {
            qrCodeImage.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }
}

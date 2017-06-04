package com.caue.splitter.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caue.splitter.R;
import com.caue.splitter.model.Participante;
import com.caue.splitter.model.Produto;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Cauê Garcia Polimanti
 * @version 1.0
 * Created on 6/4/2017.
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.Holder> {
    private static final String TAG = "ParticipantAdapter";
    Context mContext;

    private ArrayList<Participante> mParticipantes;

    // constructor
    public ParticipantAdapter(ArrayList<Participante> lista){
        Log.d(TAG, "Entered in constructor");
        mParticipantes = lista;//new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_participant, parent, false);

        return new ParticipantAdapter.Holder(view);
    }


    class Holder extends RecyclerView.ViewHolder{

        private ImageView foto;
        private TextView nome;
        private TextView nrComanda;
        private TextView email;

        private Holder(View itemView) {
            super(itemView);

            //find views
            foto = (ImageView) itemView.findViewById(R.id.participant_profile_picture);
            nome = (TextView) itemView.findViewById(R.id.tv_participant_name);
            email = (TextView) itemView.findViewById(R.id.tv_participant_email);
            nrComanda = (TextView) itemView.findViewById(R.id.tv_participant_account_number);
        }

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        View view = holder.itemView;

        Participante participante = mParticipantes.get(position);

        // setting animation
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_recycler_item_show);
        view.startAnimation(animation);

        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.foto.startAnimation(aa1);

        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);

        // set values
        holder.nome.setText(participante.getNome());

        String imagemURL = participante.getUrlFoto();
        if (imagemURL != null)
            Glide.with(view.getContext())
                    .load(imagemURL)
                    //.bitmapTransform(new VignetteFilterTransformation(mContext, new PointF(0.5f, 0.5f),new float[] { 0.0f, 0.0f, 0.0f }, 0f, 0.75f))
//                    .dontAnimate()
                    //.placeholder(R.drawable.img_temp)
                    .crossFade()
                    .into(holder.foto);

        holder.nrComanda.setText(participante.getComanda());
        holder.email.setText(participante.getEmail());

        holder.foto.startAnimation(aa);
    }

    @Override
    public int getItemCount() {
        return mParticipantes.size();
    }

    public void setItems(ArrayList<Participante> novaLista) {
        this.mParticipantes = novaLista;
        notifyDataSetChanged();
    }
}

package com.mab.relaxator;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Meditasyon> liste;

    public RecyclerAdapter(List<Meditasyon> list) {
        liste = list;

    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewholder, int position) {

        viewholder.title_text.setText(liste.get(position).getTitle());
        viewholder.description_text.setText(liste.get(position).getDescription());
        Picasso.with(viewholder.meditation_image.getContext()).load(liste.get(position).getImage()).into(viewholder.meditation_image);

    }

    @Override
    public int getItemCount() {
        return liste.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView card_view;
        public TextView title_text;
        public TextView description_text;
        public ImageView meditation_image;

        public ViewHolder(View view) {
            super(view);
            this.card_view = view.findViewById(R.id.cardView);
            this.title_text = view.findViewById(R.id.meditationTitle);
            this.description_text = view.findViewById(R.id.meditationSubTitle);
            this.meditation_image = view.findViewById(R.id.meditationImage);

        }
    }
}

package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    List<ImageDetails> imageDetailsList;
    Context context;

    public GalleryAdapter(List<ImageDetails> imageDetailsList, Context context) {
        this.imageDetailsList = imageDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cardview , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ImageDetails imageDetails = imageDetailsList.get(position);
        try {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imageDetails.getPath());
//            holder.imageView.setImageBitmap(myBitmap);
            holder.imageView.setImageDrawable(Drawable.createFromPath(imageDetails.getPath()));
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageProcessing.class );
                intent.putExtra("path" , imageDetails.getPath());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mobileImage);
        }
    }
}

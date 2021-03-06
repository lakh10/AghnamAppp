package com.nibrasco.aghnam.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nibrasco.aghnam.Model.CartItemCategory;
import com.nibrasco.aghnam.Model.RecyclerCartItemTouchListener;
import com.nibrasco.aghnam.R;

import java.util.List;


public class RecyclerCartItemAdapter extends RecyclerView.Adapter<RecyclerCartItemAdapter.CartItemHolder>
{
    private final List<CartItemCategory> items;
    private Context ctx;
    public OnClickListeners listeners;

    public RecyclerCartItemTouchListener listener;
    public class CartItemHolder extends RecyclerView.ViewHolder
    {
        TextView Category, Weight, Packaging, Qte, Slicing, Intestine;
        ImageView resource, rmvImg;
        public CartItemHolder(@NonNull View itemView) {
            super(itemView);
            Category = (TextView) itemView.findViewById(R.id.txtCartItemCategory);
            Qte = (TextView) itemView.findViewById(R.id.txtCartItemQte);
            Weight = (TextView) itemView.findViewById(R.id.txtCartItemWeight);
            Packaging = (TextView) itemView.findViewById(R.id.txtCartItemPackaging);
            Intestine = (TextView) itemView.findViewById(R.id.txtCartItemIntestine);
            Slicing = (TextView) itemView.findViewById(R.id.txtCartItemSlicing);
            resource = (ImageView) itemView.findViewById(R.id.resource);
            rmvImg = (ImageView)itemView.findViewById(R.id.rmvImg);
            rmvImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeners.OnRmvImageClick(v, getAdapterPosition());
                }
            });
        }

    }
    public RecyclerCartItemAdapter(Context ctx, List<CartItemCategory> items, OnClickListeners listeners)
    {
        this.listeners = listeners;
        this.ctx = ctx;
        this.items = items;
    }
    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater
                .from(ctx)
                .inflate(R.layout.recycler_cartitem, parent, false);

        return new CartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder itemHolder, int position) {
        CartItemCategory category = items.get(position);
        itemHolder.Category.setText(category.getCategory());
        itemHolder.Packaging.setText(category.getPackaging());
        itemHolder.Intestine.setText(category.getIntestine());
        itemHolder.Weight.setText(category.getWeight());
        itemHolder.Slicing.setText(category.getSlicing());
        itemHolder.Qte.setText(category.getQuantity());
        itemHolder.resource.setImageResource(category.getImage_drawable());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnClickListeners{
        void OnRmvImageClick(View v, int position);
    }
}



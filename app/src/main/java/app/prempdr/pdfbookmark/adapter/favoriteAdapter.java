package app.prempdr.pdfbookmark.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.prempdr.pdfbookmark.R;

public class favoriteAdapter extends RecyclerView.Adapter<favoriteAdapter.ViewHolder> {

    private final ItemClick itemClick;
    private final Context context;
    private ArrayList<favoriteModel> favoriteList;
    private favoriteDatabase favDatabase;

    public favoriteAdapter(ArrayList<favoriteModel> favoriteList, ItemClick itemClick, Context context) {
        this.favoriteList = favoriteList;
        this.itemClick = itemClick;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<favoriteModel> filterlist) {
        favoriteList = filterlist;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<favoriteModel> filteredlist) {
        favoriteList = filteredlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDatabase = new favoriteDatabase(context);
        SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences("adapter", Context.MODE_PRIVATE);
        boolean first_start = sharedPreferences.getBoolean("first_start", true);
        if (first_start) {
            createTableonFirstStart();
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_list, parent, false);
        return new ViewHolder(view);
    }

    private void createTableonFirstStart() {
        favDatabase.insertEmpty();
        SharedPreferences sharedPreferences = context.getSharedPreferences("adapter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_start", false);
        editor.apply();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(favoriteList.get(position).getImage());
        holder.title.setText(favoriteList.get(position).getTitle());
        holder.sub_title.setText(favoriteList.get(position).getSub_title());
        holder.url.setText(favoriteList.get(position).getUrl());
        holder.itemView.setOnClickListener(v -> itemClick.selectedItem(favoriteList.get(position)));
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public interface ItemClick {
        void selectedItem(favoriteModel itemClickModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favorite;
        TextView title, sub_title, url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.itemTitle);
            sub_title = itemView.findViewById(R.id.itemSubTitle);
            url = itemView.findViewById(R.id.itemUrl);
            favorite = itemView.findViewById(R.id.favorite);

            favorite.setOnClickListener(v -> {
                int position = getAdapterPosition();
                favoriteModel model = favoriteList.get(position);
                favDatabase.removeFavorite(model.getId());
                favoriteList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, favoriteList.size());
            });
        }
    }
}

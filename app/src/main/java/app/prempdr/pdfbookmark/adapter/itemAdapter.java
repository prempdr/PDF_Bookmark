package app.prempdr.pdfbookmark.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.prempdr.pdfbookmark.R;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {

    private final ItemClick itemClick;
    private final Context context;
    private ArrayList<itemModel> itemList;
    private favoriteDatabase favDatabase;

    public itemAdapter(ArrayList<itemModel> itemList, ItemClick itemClick, Context context) {
        this.itemList = itemList;
        this.itemClick = itemClick;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<itemModel> filterlist) {
        itemList = filterlist;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<itemModel> filteredlist) {
        itemList = filteredlist;
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
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
        final itemModel itemModel = itemList.get(position);
        readCursorData(itemModel, holder);

        holder.image.setImageResource(itemModel.getImage());
        holder.title.setText(itemModel.getTitle());
        holder.sub_title.setText(itemModel.getSub_title());
        holder.url.setText(itemModel.getUrl());
        //    for item click operations this line are required
        holder.itemView.setOnClickListener(v -> itemClick.selectedItem(itemModel));
    }

    private void readCursorData(itemModel itemModel, ViewHolder holder) {
        Cursor cursor = favDatabase.readAllData(itemModel.getId());
        SQLiteDatabase db = favDatabase.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String fav_status = cursor.getString(cursor.getColumnIndex(favoriteDatabase.FAVORITE_STATUS));
                itemModel.setFav_status(fav_status);

                if (fav_status != null && itemModel.getFav_status().equals("1")) {
                    holder.favorite.setImageResource(dev.oneuiproject.oneui.R.drawable.ic_oui_bookmark_list);
                } else if (fav_status != null && itemModel.getFav_status().equals("0")) {
                    holder.favorite.setImageResource(dev.oneuiproject.oneui.R.drawable.ic_oui_bookmark_list_outline);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface ItemClick {
        void selectedItem(itemModel itemClickModel);
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
                itemModel itemModel = itemList.get(position);
                if (itemModel.getFav_status().equals("0")) {
                    favorite.setImageResource(dev.oneuiproject.oneui.R.drawable.ic_oui_bookmark_list);
                    itemModel.setFav_status("1");
                    favDatabase.insertIntoDatabase(itemModel.getId(), itemModel.getTitle(), itemModel.getSub_title(), itemModel.getUrl(), itemModel.getImage(), itemModel.getFav_status());
                } else {
                    favorite.setImageResource(dev.oneuiproject.oneui.R.drawable.ic_oui_bookmark_list_outline);
                    itemModel.setFav_status("0");
                    favDatabase.insertIntoDatabase(itemModel.getId(), itemModel.getTitle(), itemModel.getSub_title(), itemModel.getUrl(), itemModel.getImage(), itemModel.getFav_status());
                }
            });
        }
    }
}

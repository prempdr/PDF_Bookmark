package app.prempdr.pdfbookmark.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import app.prempdr.pdfbookmark.activity.PDFActivity;
import app.prempdr.pdfbookmark.adapter.favoriteAdapter;
import app.prempdr.pdfbookmark.adapter.favoriteDatabase;
import app.prempdr.pdfbookmark.adapter.favoriteModel;
import app.prempdr.pdfbookmark.databinding.FragmentFavoriteBinding;

public class FavoriteFragment extends Fragment {

    ArrayList<favoriteModel> favList;
    private FragmentFavoriteBinding binding;
    private favoriteDatabase favDatabase;
    private favoriteAdapter favAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadData();
    }

    private void loadData() {
        favDatabase = new favoriteDatabase(getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(favAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favList = new ArrayList<>();

        SQLiteDatabase db = favDatabase.getReadableDatabase();
        Cursor cursor = favDatabase.selectAllFavoriteList();
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(favoriteDatabase.KEY_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(favoriteDatabase.ITEM_NAME));
                @SuppressLint("Range") String sub_title = cursor.getString(cursor.getColumnIndex(favoriteDatabase.ITEM_DESC));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(favoriteDatabase.ITEM_URL));
                @SuppressLint("Range") int image = cursor.getInt(cursor.getColumnIndex(favoriteDatabase.ITEM_IMAGE));
                favoriteModel model = new favoriteModel(image, id, title, sub_title, url, "1");
                favList.add(model);
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }
        favAdapter = new favoriteAdapter(favList, this::itemClick, getContext());
        binding.recyclerView.setAdapter(favAdapter);
    }

    public void itemClick(favoriteModel model) {
        String title = model.getTitle();
        String url = model.getUrl();

        Intent i = new Intent(requireContext(), PDFActivity.class);
        i.putExtra("title", title);
        i.putExtra("url", url);
        startActivity(i);
    }
}
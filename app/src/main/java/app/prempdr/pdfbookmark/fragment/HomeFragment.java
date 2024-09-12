package app.prempdr.pdfbookmark.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import app.prempdr.pdfbookmark.R;
import app.prempdr.pdfbookmark.activity.PDFActivity;
import app.prempdr.pdfbookmark.adapter.itemAdapter;
import app.prempdr.pdfbookmark.adapter.itemModel;
import app.prempdr.pdfbookmark.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    SearchView searchView;
    LinearLayout rootView;
    private FragmentHomeBinding binding;
    private ArrayList<itemModel> itemList;
    private itemAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        rootView.requestFocus();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rootView = requireView().findViewById(R.id.mainLayout);
        searchView = requireView().findViewById(R.id.searchView);

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        itemsData();
    }

    private void itemsData() {
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(manager);

        itemList = new ArrayList<>();
        // add image_id, item title, item subtitle
        itemList.add(new itemModel(R.drawable.img1,
                "1",
                "Vocabulary",
                "a test vocabulary pdf",
                "vocabulary.pdf",
                "0"
        ));
        itemList.add(new itemModel(R.drawable.img2,
                "2",
                "Test File 1",
                "a test 1 pdf",
                "test_1.pdf",
                "0"
        ));
        itemList.add(new itemModel(R.drawable.img3,
                "3",
                "Test File 2",
                "a test 2 pdf",
                "test_2.pdf",
                "0"
        ));

        adapter = new itemAdapter(itemList, this::itemClick, requireContext());
        binding.recyclerView.setAdapter(adapter);
    }

    public void itemClick(itemModel model) {
        String title = model.getTitle();
        String url = model.getUrl();

        Intent i = new Intent(requireContext(), PDFActivity.class);
        i.putExtra("title", title);
        i.putExtra("url", url);
        startActivity(i);
    }

    private void filterList(String query) {
        ArrayList<itemModel> filteredList = new ArrayList<>();
        for (itemModel item : itemList) {
            if (item.getTitle().toUpperCase().contains(query.toUpperCase()) || item.getSub_title().toUpperCase().contains(query.toUpperCase())) {
                filteredList.add(item);
            }
            adapter.setFilterList(filteredList);
        }
    }
}
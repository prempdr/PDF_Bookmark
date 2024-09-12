package app.prempdr.pdfbookmark.activity;

import static app.prempdr.pdfbookmark.core.prempdr.fullScreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import app.prempdr.pdfbookmark.R;
import app.prempdr.pdfbookmark.adapter.favoriteDatabase;
import app.prempdr.pdfbookmark.fragment.FavoriteFragment;
import app.prempdr.pdfbookmark.fragment.HomeFragment;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    private final Fragment fragment1 = new HomeFragment();
    private final Fragment fragment2 = new FavoriteFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active;
    favoriteDatabase favDatabase;
    SharedPreferences sharedPreferences;
    SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fullScreen(this);
        
        sharedPreferences = this.getSharedPreferences("adapter", Context.MODE_PRIVATE);
        boolean first_start = sharedPreferences.getBoolean("first_start", true);
        if (first_start) {
            favDatabase = new favoriteDatabase(this);
            favDatabase.insertEmpty();
            sharedPreferences = this.getSharedPreferences("adapter", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("first_start", false);
            editor.apply();
        }

        bottomBar = findViewById(R.id.bottomBar);

        fragmentData();
        backButton();
    }

    private void backButton() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int selectedPosition = bottomBar.getItemActiveIndex();

                if (selectedPosition != 0) {
                    bottomBar.setItemActiveIndex(0);
                    fm.beginTransaction().detach(active).attach(fragment1).commit();
                    active = fragment1;
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void fragmentData() {
        fm.beginTransaction().add(R.id.framelayout, fragment2, "2").commit();
        fm.beginTransaction().add(R.id.framelayout, fragment1, "1").commit();
        active = fragment1;

        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) item -> {
            if (item == 0) {
                fm.beginTransaction().detach(active).attach(fragment1).commit();
                active = fragment1;
            } else if (item == 1) {
                fm.beginTransaction().detach(active).attach(fragment2).commit();
                active = fragment2;
            }
            return true;
        });
    }
}
package app.prempdr.pdfbookmark.activity;

import static app.prempdr.pdfbookmark.core.prempdr.fullScreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.pdfviewer.PDFView;
import com.github.pdfviewer.scroll.DefaultScrollHandle;

import app.prempdr.pdfbookmark.R;

public class PDFActivity extends AppCompatActivity {

    String url;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pdf);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fullScreen(this);

        url = getIntent().getStringExtra("url");
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        pdfView = findViewById(R.id.pdfView);
        if (url != null) {
            pdfView.fromAsset(url)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(2)
                    .load();
        } else {
            startActivity(new Intent(PDFActivity.this, MainActivity.class));
            finish();
        }
    }
}
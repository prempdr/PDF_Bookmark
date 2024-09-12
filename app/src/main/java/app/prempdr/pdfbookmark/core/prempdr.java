package app.prempdr.pdfbookmark.core;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import app.prempdr.pdfbookmark.R;

public class prempdr {

    public static void fullScreen(Activity context) {
        Window window = context.getWindow();
        WindowCompat.setDecorFitsSystemWindows(window, false);
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(window, context.findViewById(R.id.main));
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
        if (Build.VERSION.SDK_INT <= 30) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
    }
}

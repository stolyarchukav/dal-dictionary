package org.forzaverita.daldic.util;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.forzaverita.daldic.R;
import org.forzaverita.daldic.service.DalDicService;

public final class CommonViewUtil {

    public static void addEdgeToEdgeMargins(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.topMargin = insets.top;
            mlp.bottomMargin = insets.bottom;
            mlp.leftMargin = insets.left;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });
    }

    public static void setupBackground(View view, DalDicService service) {
        if (service.isShowBackground()) {
            view.setBackgroundResource(R.drawable.texture);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }
    }

}

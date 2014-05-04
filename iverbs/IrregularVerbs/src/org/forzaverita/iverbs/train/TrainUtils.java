package org.forzaverita.iverbs.train;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import org.forzaverita.iverbs.R;
import org.forzaverita.iverbs.ScoresActivity;
import org.forzaverita.iverbs.service.AppService;

public class TrainUtils {

    private static final int MIN_COUNT_IN_TRAINING = 5;

    public static boolean checkSelectedVerbs(AppService service, final Activity activity) {
        if (service.getInTrainingCount() < MIN_COUNT_IN_TRAINING) {
            String title = String.format(activity.getString(R.string.train_min_dialog_title), MIN_COUNT_IN_TRAINING);
            new AlertDialog.Builder(activity).setTitle(title).setPositiveButton(R.string.train_min_dialog_button,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(activity, ScoresActivity.class).
                                    addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP));
                        }
                    }).create().show();
            return false;
        }
        return true;
    }

}

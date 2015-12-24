package com.udacity.myappportfolio.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.myappportfolio.R;

/**
 * Created by HP on 12/24/2015.
 */
public class MyUtil {

    public static void displayCustomToast(Context context, String message) {

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View toastView = toast.getView(); //This'll return the default View of the Toast.

        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(17);
        toastMessage.setPadding(35,10,35,10);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        toastMessage.setGravity(Gravity.CENTER);
        toastView.setBackground(context.getResources().getDrawable(R.drawable.toast_bg));
        toast.show();
    }
}

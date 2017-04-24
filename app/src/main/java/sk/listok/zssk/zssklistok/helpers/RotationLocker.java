package sk.listok.zssk.zssklistok.helpers;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;

/**
 * Pomocník pre uzamknutie otáčania displeja.
 */
public class RotationLocker {

    /**
     * Uzamkne otáčanie displeja. Odomknutie je nutné spraviť explicitne funkciou unlock.
     */
    public static void lockRotateScreen(AppCompatActivity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            height = display.getHeight();
            width = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
            width = size.x;
        }
        switch (rotation) {
            case Surface.ROTATION_90:
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT/* reversePortait */);
                break;
            case Surface.ROTATION_180:
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT/* reversePortait */);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE/* reverseLandscape */);
                break;
            case Surface.ROTATION_270:
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE/* reverseLandscape */);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default:
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    /**
     * Odomknutie otáčania displeja.
     */
    public static void unlockRotateScreen(AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}

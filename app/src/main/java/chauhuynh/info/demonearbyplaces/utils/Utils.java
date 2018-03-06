package chauhuynh.info.demonearbyplaces.utils;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by appro on 16/01/2018.
 */

public class Utils {

    private static Utils Instance = null;

    public static Utils getInstance() {
        if (Instance == null) {
            Instance = new Utils();
        }
        return Instance;
    }

    public boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    Log.d("Check_Wifi: ", "OK");
                    return true;
                }
            }
        }
        Log.d("Check_Wifi: ", "FAIL");
        return false;
    }

    public boolean isLocation(Context context){
        if (Build.VERSION.SDK_INT < 23) {

            Log.d("GPS: ", "OK");
            return true;

        } else {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                Log.d("GPS: ", "OK");
                return true;
            }
            Log.d("GPS: ", "FAIL");
            return false;
        }

    }

    // Alert
    public void alert(final Context context, final String type, String title, String message, String btnYes, String btnNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type.equals("Wifi")) {
                            Intent SettingWifi = new Intent();
                            SettingWifi.setAction(Settings.ACTION_WIFI_SETTINGS);
                            context.startActivity(SettingWifi);
                            dialog.dismiss();
                        }else {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    }
                })
                .setNegativeButton(btnNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
        Log.d("AlertDisconnect: ", "SHOW");
    }
}

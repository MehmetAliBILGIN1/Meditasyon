package com.mab.relaxator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


   public boolean checkInternet(final Context context){
       final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
       return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(checkInternet(this)){
            Thread splashThread;
            splashThread = new Thread(){
            @Override public void run(){
                try{
                synchronized (this){
                    wait(3000);

                }
                }catch (InterruptedException ex){
                    Log.d("splash error", ex.getLocalizedMessage());

                } finally {
                    startActivity( new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }

                }
            };
            splashThread.start();
        } else{

            AlertDialog alert  = new AlertDialog.Builder(this).create();
            alert.setTitle(R.string.noInternet);
            alert.setMessage("Please Check Your Connection.");
            alert.setButton(DialogInterface.BUTTON_NEUTRAL, "OKEY",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                            dialog.dismiss();
                        }
                    });
            alert.show();
        }

    }
}

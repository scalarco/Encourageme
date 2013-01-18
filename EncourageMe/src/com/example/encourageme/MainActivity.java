package com.example.encourageme;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;

public class MainActivity extends Activity {

	SharedPreferences mPrefs;
	final String welcomeScreenShownPref = "welcomeScreenShown";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	    // second argument is the default to use if the preference can't be found
	    Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

	    if (!welcomeScreenShown) {
	        // here you can launch another activity if you like
	        // the code below will display a popup

	        String whatsNewTitle = getResources().getString(R.string.whatsNewTitle);
	        String whatsNewText = getResources().getString(R.string.whatsNewText);
	        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(whatsNewTitle).setMessage(whatsNewText).setPositiveButton(
	                R.string.ok, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                        dialog.dismiss();
	                    }
	                }).show();
	        SharedPreferences.Editor editor = mPrefs.edit();
	        editor.putBoolean(welcomeScreenShownPref, true);
	        editor.commit(); // Very important to save the preference
	    }

	}
	/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    */
}

package com.example.encourageme;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

	private RelativeLayout settingsPage;
	private RelativeLayout configPage;
	private TextView currStartTV, currEndTV, freqTV;
	private TimePicker timePicker1;
	private TimePicker timePicker2;
	private Spinner frequencySpinner;
	private String frequencySet;
	private Button btnSetStart;
	private Button btnSetEnd;
	private Button btnSetEnc;
	private int hour1;
	private int hour2;
	private int minute1;
	private int minute2;
	static final int TIME_DIALOG_ID1=999;
	static final int TIME_DIALOG_ID2=998;
	
	SharedPreferences mPrefs;
	
	final String welcomeScreenShownPref = "welcomeScreenShown";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    settingsPage = (RelativeLayout) findViewById(R.id.settingsPage);
	    configPage=(RelativeLayout) findViewById(R.id.configPage);
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
	    setStartTimeOnView();
	    setEndTimeOnView();
	    addListenerOnButton();
	    currStartTV=(TextView) findViewById(R.id.currStart);
	    currEndTV=(TextView) findViewById(R.id.currEnd);
	    freqTV=(TextView) findViewById(R.id.currFreq);
	    addListenerOnSpinnerItemSelection();
	}
	
	public void addListenerOnSpinnerItemSelection() {
		frequencySpinner = (Spinner) findViewById(R.id.freqSpinner);
		frequencySpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }
	
	public void setStartTimeOnView(){
		timePicker1=(TimePicker) findViewById(R.id.timePicker1);
		
	}
	public void setEndTimeOnView(){
		timePicker2=(TimePicker) findViewById(R.id.timePicker2);
		
	}
	public void addListenerOnButton()
	{
		btnSetStart=(Button) findViewById(R.id.btnSetStart);
		frequencySpinner = (Spinner) findViewById(R.id.freqSpinner);
		
		btnSetEnc=(Button) findViewById(R.id.setEncourage);
		btnSetEnc.setOnClickListener(new OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
		 
			  frequencySet=String.valueOf(frequencySpinner.getSelectedItem());
		      configP(v);         
				//CALL SCHEDULE HERE USING PARAMETERS (int hour1, int minute1, int hour2, int minute2, String frequencySet)	
			  }
		 
			});
		  
		btnSetStart.setOnClickListener(new OnClickListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v){
				showDialog(TIME_DIALOG_ID1);
			}
		});
		
		btnSetEnd=(Button) findViewById(R.id.btnSetEnd);
		btnSetEnd.setOnClickListener(new OnClickListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v){
				showDialog(TIME_DIALOG_ID2);
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case TIME_DIALOG_ID1:
			return new TimePickerDialog(this,
					timePickerListener1, hour1, minute1, false);
			
		case TIME_DIALOG_ID2:
			return new TimePickerDialog(this,
					timePickerListener2, hour2, minute2, false);
		}
		return null;
	}
	
	private TimePickerDialog.OnTimeSetListener timePickerListener1 = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour1 = selectedHour;
			minute1 = selectedMinute;
 
			// set current time into timepicker
			timePicker1.setCurrentHour(hour1);
			timePicker1.setCurrentMinute(minute1);
			
		}
	};
	private TimePickerDialog.OnTimeSetListener timePickerListener2 = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour2 = selectedHour;
			minute2 = selectedMinute;
 
			// set current time into timepicker
			timePicker2.setCurrentHour(hour2);
			timePicker2.setCurrentMinute(minute2);
 
		}
	};
	
	public void configP(View v)
	{
		settingsPage.setVisibility(View.GONE);
		configPage.setVisibility(View.VISIBLE);
		String m1,m2;
		String min1, min2;
		if(hour1>=12) m1="PM";
		else m1="AM";
		
		if(hour2>=12) m2="PM";
		else m2="AM";
		
		if(minute1<10) min1="0"+minute1;
		else min1=""+minute1;
		
		if(minute2<10) min2="0"+minute2;
		else min2=""+minute2;
		
		if(hour1!=0&&hour1!=12)
			currStartTV.setText("Start time is "+ (hour1%12)+":"+(min1)+m1);
		else if(hour1==0||hour1==12)
			currStartTV.setText("Start time is "+ ("12")+":"+(min1)+m1);
		
		if(hour2!=0&&hour2!=12)
			currEndTV.setText("End time is "+ (hour2%12)+":"+(min2)+m2);
		else if(hour1==0||hour1==12)
			currEndTV.setText("End time is "+ ("12")+":"+(min2)+m2);
		
		freqTV.setText("The Frequency is set at " + frequencySet);
	}
	public void settingsP(View v)
	{
		configPage.setVisibility(View.GONE);
		settingsPage.setVisibility(View.VISIBLE);
	}
	
	
	
}

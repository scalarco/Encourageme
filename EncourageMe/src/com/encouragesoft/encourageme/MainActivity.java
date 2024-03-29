package com.encouragesoft.encourageme;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.telephony.TelephonyManager;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Dialog;
import android.app.TimePickerDialog;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RelativeLayout;

import android.widget.Spinner;




public class MainActivity extends Activity {
	String FILENAME="saver";
	private RelativeLayout settingsPage;
	private RelativeLayout configPage;
	private TextView currStartTV, currEndTV, freqTV, configTV, settingsTV, phoneTV, startTimeTV, endTimeTV;
	private TimePicker timePicker1;
	private TimePicker timePicker2;
	private Spinner frequencySpinner;
	private String frequencySet;
	private Button btnSetStart;
	private Button btnSetEnd;
	private Button btnSetEnc;
	private Button btnDelete;
	private int hour1;
	private int hour2;
	private int minute1;
	private int minute2;
	private String[] m;
	static final int TIME_DIALOG_ID1=999;
	static final int TIME_DIALOG_ID2=998;
	private SmsAlarm manager;
	private boolean settingsSet;

	SharedPreferences mPrefs;
	SharedPreferences setPrefs;
	final String welcomeScreenShownPref = "welcomeScreenShown";
	final String configScreenShownPref = "configScreenShown";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    settingsPage = (RelativeLayout) findViewById(R.id.settingsPage);
	    configPage=(RelativeLayout) findViewById(R.id.configPage);
	    settingsPage.setBackgroundResource(R.drawable.bgbeach);
	    configPage.setBackgroundResource(R.drawable.bgbeach);
	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	    setPrefs=PreferenceManager.getDefaultSharedPreferences(this);
	    m = getResources().getStringArray(R.array.messages);
	    //settingsPage.setBackgroundResource(R.drawable.beach_photo_background);
	    //configPage.setBackgroundResource(R.drawable.beach_photo_background);
	    // second argument is the default to use if the preference can't be found
	    Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
	    Boolean configScreenShown=setPrefs.getBoolean(configScreenShownPref, false);
	    if (!welcomeScreenShown) {
	        // here you can launch another activity if you like
	        // the code below will display a popup
	    	manager = new SmsAlarm();
	    	saveAlarm(getApplicationContext());
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
	    manager=new SmsAlarm();
	    manager=loadAlarm(getApplicationContext());
	    setStartTimeOnView();
	    setEndTimeOnView();
	    addListenerOnButton();
	    phoneTV=(TextView) findViewById(R.id.phoneN);
	    currStartTV=(TextView) findViewById(R.id.currStart);
	    currEndTV=(TextView) findViewById(R.id.currEnd);
	    freqTV=(TextView) findViewById(R.id.currFreq);
	    settingsTV=(TextView) findViewById(R.id.titleSettings);
	    settingsTV.setText("EncourageMe Settings");
	    configTV=(TextView) findViewById(R.id.titleConfig);
	    configTV.setText("EncourageMe Configuration");
	    addListenerOnSpinnerItemSelection();
	    //If a configuration is already set
	    if (configScreenShown)
	    {
	    	//get the values from the SharedPreferences
	    	hour1 = setPrefs.getInt("hour1", 0);
	    	hour2 = setPrefs.getInt("hour2", 0);
	    	minute1 = setPrefs.getInt("minute1", 0);
	    	minute2 = setPrefs.getInt("minute2", 0);
	    	int freqInt = setPrefs.getInt("frequency", 0);
	    	frequencySet=freqInttoString(freqInt);

	    	configPageNow();

	    }
	}

	public void addListenerOnSpinnerItemSelection() {
		frequencySpinner = (Spinner) findViewById(R.id.freqSpinner);
		frequencySpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }

	public void setStartTimeOnView(){
		//timePicker1=(TimePicker) findViewById(R.id.timePicker1);
		startTimeTV = (TextView) findViewById(R.id.startTimeTV);

	}
	public void setEndTimeOnView(){
		//timePicker2=(TimePicker) findViewById(R.id.timePicker2);
		endTimeTV = (TextView) findViewById(R.id.endTimeTV);

	}

	public String freqInttoString(int freq){
		if( freq==1440)
			return "Once a day";
		else if(freq==720)
			return "Twice a day";
		else if(freq==480)
			return "Every 8 hours";
		else if(freq==240)
			return "Every 4 hours";
		else if(freq==120)
			return "Every 2 hours";
		else if(freq==60)
			return "Every hour";
		else if(freq==30)
			return "Every 30 minutes";
		else
			return null;
	}

	public int frequencyToInt(String freq){

		if( freq.equals("Once a day"))
			return 1440;
		else if( freq.equals("Twice a day"))
			return 720;
		else if(freq.equals("Every 8 hours"))
			return 480;
		else if(freq.equals("Every 4 hours"))
			return 240;
		else if(freq.equals("Every 2 hours"))
			return 120;
		else if(freq.equals("Every hour"))
			return 60;
		else if(freq.equals("Every 30 minutes"))
			return 30;
		else
			return 0;

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
			  int freqInt=frequencyToInt(frequencySet);
			  Boolean configScreenShown=setPrefs.getBoolean(configScreenShownPref, false);
			  if(configScreenShown)
				  manager.CancelAlarm(getApplicationContext());
		      configP(v);
		      SharedPreferences.Editor editor = setPrefs.edit();
		      editor.putBoolean(configScreenShownPref, true);
		      
		    
			  
				//CALL SCHEDULE HERE USING PARAMETERS (int hour1, int minute1, int hour2, int minute2, String frequencySet)	
		      setSmsAlarm(hour1, hour2, minute1, minute2, freqInt);
		      editor.commit();
		      /*try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage("9737180155", null, "fucking test", null, null);
					
				  } catch (Exception e) {
					Toast.makeText(getApplicationContext(),
						"SMS faild, please try again later!",
						Toast.LENGTH_LONG).show();
					e.printStackTrace();
				  }*/
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

		btnDelete=(Button) findViewById(R.id.btnDel);
		btnDelete.setOnClickListener(new OnClickListener() {

			  @Override
			  public void onClick(View v) {

			  //Delete currently scheduled Encouragements
			  manager.CancelAlarm(getApplicationContext());
			  //startTimeTV.setText("");
			  //endTimeTV.setText("");
		      settingsPD(v); 
		      
		      SharedPreferences.Editor editor = setPrefs.edit();
		      editor.putBoolean(configScreenShownPref, false);
		      
		      editor.commit(); 

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
 
			// set current time into the start time textview
			
			String m1;
			String min1;
			if(hour1>=12) m1="PM";
			else m1="AM";


			if(minute1<10) min1="0"+minute1;
			else min1=""+minute1;


			if(hour1!=0&&hour1!=12)
				startTimeTV.setText("Start time is "+ (hour1%12)+":"+(min1)+m1);
			else if(hour1==0||hour1==12)
				startTimeTV.setText("Start time is "+ ("12")+":"+(min1)+m1);
		}
	};
	private TimePickerDialog.OnTimeSetListener timePickerListener2 = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour2 = selectedHour;
			minute2 = selectedMinute;
 
			// set current time into the end time textview
			String m2;
			String min2;

			if(hour2>=12) m2="PM";
			else m2="AM";


			if(minute2<10) min2="0"+minute2;
			else min2=""+minute2;


			if(hour2!=0&&hour2!=12)
				endTimeTV.setText("End time is "+ (hour2%12)+":"+(min2)+m2);
			else if(hour2==0||hour2==12)
				endTimeTV.setText("End time is "+ ("12")+":"+(min2)+m2);
 
		}
	};

	public void configP(View v)
	{
		settingsPage.setVisibility(View.GONE);
		configPage.setVisibility(View.VISIBLE);
		TelephonyManager tMgr1 =(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr1.getLine1Number();
		phoneTV.setText(phoneNumber);
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
		else if(hour2==0||hour2==12)
			currEndTV.setText("End time is "+ ("12")+":"+(min2)+m2);

		freqTV.setText("The Frequency is set at " + frequencySet);
	}
	public void configPageNow()
	{
		settingsPage.setVisibility(View.GONE);
		configPage.setVisibility(View.VISIBLE);
		TelephonyManager tMgr1 =(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr1.getLine1Number();
		phoneTV.setText(phoneNumber);
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
		else if(hour2==0||hour2==12)
			currEndTV.setText("End time is "+ ("12")+":"+(min2)+m2);

		freqTV.setText("The Frequency is set at " + frequencySet);
	}
	public void settingsP(View v)
	{
		configPage.setVisibility(View.GONE);
		settingsPage.setVisibility(View.VISIBLE);
		
	}
	public void settingsPD(View v)
	{
		
		configPage.setVisibility(View.GONE);
		settingsPage.setVisibility(View.VISIBLE);
		
	}

	public void setSmsAlarm(int h1, int h2, int m1, int m2, int freq) {
		manager.setHour1(h1);
		manager.setHour2(h2);
		manager.setMinute1(m1);
		manager.setHour2(m2);
		manager.setFrequency(freq);
		manager.SetAlarm(getApplicationContext());
		SharedPreferences.Editor editor = setPrefs.edit();
		
		editor.putInt("hour1", h1);
	    editor.putInt("hour2", h2);
	    editor.putInt("minute1", m1);
	    editor.putInt("minute2", m2);
	    editor.putInt("frequency", freq);
	    editor.putBoolean("settingsSet", true);
	    editor.commit();
	    saveAlarm(getApplicationContext());
	}
	public void saveAlarm(Context context){
		try{
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(manager);
		os.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public SmsAlarm loadAlarm(Context context){

		try{
		FileInputStream fis = context.openFileInput(FILENAME);
		ObjectInputStream is=new ObjectInputStream(fis);
		try {
			manager=(SmsAlarm) is.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		is.close();

		}
		catch(IOException e){
			e.printStackTrace();
		}
		return manager;
	}

}

package com.example.encourageme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class SmsAlarm extends BroadcastReceiver {
	private int hour1, minute1, hour2, minute2, frequency, index;
	String FILENAME="saver";

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		setHour1(prefs.getInt("hour1", 0));
		setHour2(prefs.getInt("hour2", 0));
		setMinute1(prefs.getInt("minute1", 0));
		setMinute2(prefs.getInt("minute2", 0));
		setFrequency(prefs.getInt("frequency", 0));
		SmsManager manager = SmsManager.getDefault();
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		int timeOption=timeOption(hour1, hour2, minute1, minute2);
		setIndex(prefs.getInt("ind", 0));
		
		String[] messages = context.getResources().getStringArray(R.array.messages);
		if(timeOption==1){
			if (currentHour > hour1 && currentHour < hour2) {
			manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() * (messages.length-1))], null, null);
			}
			else if (currentHour == hour1 && currentMinute > minute1) {
				manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() * (messages.length-1))], null, null);
				}
			else if (currentHour == hour2 && currentMinute < minute2) {
				manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() * (messages.length-1))], null, null);
				}
			else{}
		}
		else if(timeOption==2){
			if ((currentHour > hour1 ) || (currentHour < hour2)) {
			manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() * (messages.length-1))], null, null);
			}
			else if((currentHour==hour1)&&currentMinute>minute1){
				manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() * (messages.length-1))], null, null);
			}
			else if((currentHour==hour2)&&currentMinute<minute2){
				manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() *(messages.length-1))], null, null);
			}
			else{}
		}
		else{
			manager.sendTextMessage(phoneNumber, null, messages[(int)(Math.random() * (messages.length-1))], null, null);
		}
		
		index++;
		if (index >= messages.length) {
			index = 0;
		}
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("ind", index);
		editor.commit();
	}
	private int timeOption(int h1, int h2, int m1, int m2)
	{
		if(h1<h2)
			return 1;
		else if(h1==h2)
		{
			if(m1<m2)
				return 1;
			else if(m1>m2)
				return 2;
			else
				return 3;
		}
		else
			return 2;
	}
	public void SetAlarm(Context context) {
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SmsAlarm.class);
        i.setAction("com.android.encourageme.SMS_ALARM");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * frequency, pi); //millisecond * second * minutes
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, SmsAlarm.class);
		intent.setAction("com.android.encourageme.SMS_ALARM");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
	}



	public int getHour1() {
		return hour1;
	}

	public void setHour1(int num) {
		hour1 = num;
	}
	
	public void setIndex(int num) {
		index = num;
	}

	public int getMinute1() {
		return minute1;
	}

	public void setMinute1(int num) {
		minute1 = num;
	}

	public int getHour2() {
		return hour2;
	}

	public void setHour2(int num) {
		hour2 = num;
	}

	public int getMinute2() {
		return minute2;
	}

	public void setMinute2(int num) {
		minute2 = num;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int num) {
		frequency = num;
	}
}

package com.example.encourageme;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class SmsAlarm extends BroadcastReceiver {
	private int hour1, minute1, hour2, minute2, frequency;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SmsManager manager = SmsManager.getDefault();
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tMgr.getLine1Number();
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		if (currentHour > hour1 && currentHour < hour2 && currentMinute > minute1 && currentMinute < minute2) {
			manager.sendTextMessage(phoneNumber, null, "Test message", null, null);
		}
	}
	
	public void SetAlarm(Context context) {
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SmsAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * frequency, pi); //millisecond * second * minutes
	}
	
	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, SmsAlarm.class);
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

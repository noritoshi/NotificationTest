package com.example.notificationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BLUETOOTH = 1;
	private int mId;
	private boolean _isResumed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
		if (!Bt.isEnabled()) {
			Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	protected void onResume() {
		super.onResume();
		_isResumed = true;
	}

	@Override
	protected void onPause() {
		_isResumed = false;
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {

		v.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (_isResumed) {
					new AlertDialog.Builder(MainActivity.this).setTitle("Hello!").show();
				} else {
					mId = (int) (Math.random() * 100);

					NotificationCompat.Builder mBuilder =
							new NotificationCompat.Builder(MainActivity.this)
									.setSmallIcon(R.drawable.ic_launcher)
									.setContentTitle("My notification")
									.setContentText("Hello World!" + mId)
									.setAutoCancel(true)
									.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
									.setPriority(NotificationCompat.PRIORITY_HIGH)
									.setVibrate(new long[] { 0, 250, 250, 250 });
					// Creates an explicit intent for an Activity in your app
					Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.atlas.jp/"));

					// The stack builder object will contain an artificial back stack for the
					// started Activity.
					// This ensures that navigating backward from the Activity leads out of
					// your application to the Home screen.
					TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
					// Adds the back stack for the Intent (but not the Intent itself)
//				stackBuilder.addParentStack(ResultActivity.class);
					// Adds the Intent that starts the Activity to the top of the stack
					stackBuilder.addNextIntent(resultIntent);
					PendingIntent resultPendingIntent =
							stackBuilder.getPendingIntent(
									0,
									PendingIntent.FLAG_UPDATE_CURRENT
									);
					mBuilder.setContentIntent(resultPendingIntent);
					NotificationManager mNotificationManager =
							(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					// mId allows you to update the notification later on.
					mNotificationManager.notify(mId++, mBuilder.build());
				}
			}
		}, 5000);
	}

	@Override
	protected void onActivityResult(int requestCode, int ResultCode, Intent date) {
		if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
			if (ResultCode == Activity.RESULT_OK) {
				Toast.makeText(this, "BluetoothをONにしてもらえました。", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "BluetoothがONにしてもらえませんでした。", Toast.LENGTH_LONG).show();
			}
		}
	}
}

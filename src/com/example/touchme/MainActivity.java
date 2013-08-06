package com.example.touchme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	View view;
	TextView text;
	Connection connect;
	GestureDetector myDetector;
	WakeLock mWakeLock;
	private final int GET_IP_ADDRESS = 34;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = (View) findViewById(R.id.layout);
		text = (TextView) findViewById(R.id.textview);
		myDetector = new GestureDetector(this, new GestureListener());
		if (view == null) {
			Log.d("sygi", "returns");
			return;
		}
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "sygi");
		view.requestFocus();
		setListening();
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		Intent i = new Intent(this, ConnectActivity.class);
		startActivityForResult(i, GET_IP_ADDRESS);

		// connect = new Connection("192.168.43.71",
	}

	void createConnection(String ipAddress, int port) {
		Log.d("sygi", "connecting to " + ipAddress + ":" + port);
		connect = new Connection(ipAddress, port);
		mWakeLock.acquire();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Double startX, startY;

	private void setListening() {
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (myDetector.onTouchEvent(event))
					return true;
				// connect.send("move x: " + event.getX() + " y: " +
				// event.getY());
				text.setText("x: " + (event.getX() - startX) + " y: " + (event.getY() - startY));
				view.invalidate();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					connect.send("up");
					Log.d("sygi", "up");
					return true;
				}
				return true;
			}
		});
	}

	private class GestureListener implements GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			startX = Double.valueOf(e.getX());
			startY = Double.valueOf(e.getY());
			connect.send("down x: " + startX + " y: " + startY);
			return true;
		}

		@Override
		// TODO Auto-generated method stub
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			Log.d("sygi", "onScroll");
			if (Math.max(Math.abs(distanceX), Math.abs(distanceY)) < 0.01)
				return false;
			connect.send("move x: " + distanceX + " y: " + distanceY);
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			connect.send("click");
			return true;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			finish();
		}
		if (requestCode == GET_IP_ADDRESS) {
			if (data == null)
				finish();
			String ip = data.getStringExtra("ip");
			if (ip == null)
				finish();
			createConnection(ip, 4444);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mWakeLock.isHeld())
			mWakeLock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mWakeLock.isHeld() && connect != null)
			mWakeLock.acquire();
	}
}

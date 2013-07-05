package com.example.touchme;

import android.app.Activity;
import android.os.Bundle;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = (View) findViewById(R.id.layout);
		text = (TextView) findViewById(R.id.textview);
		myDetector = new GestureDetector(this, new GestureListener());
		if (view == null){
			Log.d("sygi", "returns");
			return;
		}
		view.requestFocus();
		setListening();
		view.setFocusable(true);
        view.setFocusableInTouchMode(true);
		connect = new Connection("192.168.1.104", 4444);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Double startX, startY;
	
	private void setListening(){
		view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				myDetector.onTouchEvent(event);
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					startX = Double.valueOf(event.getX());
					startY = Double.valueOf(event.getY());
					connect.send("down x: " + startX + " y: " + startY);
				} else {
					connect.send("move x: " + event.getX() +  " y: " + event.getY());
				}
				text.setText("x: " + (event.getX() - startX) + " y: " + (event.getY() - startY));
				view.invalidate();
				return true;
			}
		});
	}
	private class GestureListener implements GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override// TODO Auto-generated method stub
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			connect.send("click");
			return false;
		}
		
	}
	
}

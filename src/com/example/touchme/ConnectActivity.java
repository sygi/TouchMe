package com.example.touchme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ConnectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect, menu);
		return true;
	}

	public void finished(View view) {
		Intent i = new Intent();
		EditText et1 = (EditText) findViewById(R.id.ip1);
		String ip1 = "" + et1.getText();
		if (ip1.equals(""))
			ip1 = "192";
		EditText et2 = (EditText) findViewById(R.id.ip2);
		String ip2 = "" + et2.getText();
		if (ip2.equals(""))
			ip2 = "168";
		EditText et3 = (EditText) findViewById(R.id.ip3);
		String ip3 = "" + et3.getText();
		if (ip3.equals(""))
			ip3 = "1";
		EditText et4 = (EditText) findViewById(R.id.ip4);
		String ip = ip1 + "." + ip2 + "." + ip3 + "." + et4.getText();
		i.putExtra("ip", ip);
		setResult(RESULT_OK, i);
		finish();
	}
}

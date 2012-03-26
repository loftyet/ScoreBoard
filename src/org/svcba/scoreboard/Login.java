package org.svcba.scoreboard;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	public static final String USER = "USER";
	private static final int DIALOG_TEXT_ENTRY = 7;
	
	protected Dialog onCreateDialog (int id){
		switch (id){
		case DIALOG_TEXT_ENTRY :
		   LayoutInflater factory = LayoutInflater.from(this);
		   final View textEntryView = factory.inflate(R.layout.dlg_login, null);
		   
		   return new AlertDialog.Builder(Login.this)
		       .setIcon(R.drawable.alert_dialog_icon)
		       .setTitle(R.string.alert_dialog_text_entry)
		       .setView(textEntryView)
		       .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int whichButton) {
		        	   final EditText username = (EditText)textEntryView.findViewById(R.id.username_edit);
		        	   final EditText password = (EditText)textEntryView.findViewById(R.id.password_edit);
		        	   password.setOnKeyListener(new OnKeyListener() {
		        		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        		        // If the event is a key-down event on the "enter" button
		        		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		        		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        		          // Perform action on key press
		        		        	Log.d("here!","are we here?!");
		        		        	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        		        	imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
		        		          return true;
		        		        }
		        		        return false;
		        		    }
		        		});
		        	   
		        	   final String user = username.getText().toString().trim();
  		        	   final String pwd = password.getText().toString().trim();
  		        	 
  		        	   if ((user.equalsIgnoreCase("svcba") && pwd.equalsIgnoreCase("svcba")) || (user.equalsIgnoreCase("guest") && pwd.equalsIgnoreCase("guest")))
		        	   {
		        		   Intent i = new Intent(getApplicationContext(),ScoreBoard.class);
		        		   i.putExtra(USER,user);
		        		   startActivity(i);
		        	   }  else {
		        		  Toast.makeText(getApplicationContext(), "KEEP TRYING", Toast.LENGTH_SHORT).show();
		        	   }
  		        	   
		        	   
		        	   
		           }
		       })
		       .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int whichButton) {
		           	//finish();
		           }
		       })
		       .create();
		}
		
		return null;
	}
	
	@Override
	public void onCreate(Bundle onSavedInstanceBundle)
	{
		super.onCreate(onSavedInstanceBundle);
		setContentView(R.layout.login);
		
	    Button textEntry = (Button) findViewById(R.id.text_entry_button);
        textEntry.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_TEXT_ENTRY);
            }
        });
		
	}
	
}

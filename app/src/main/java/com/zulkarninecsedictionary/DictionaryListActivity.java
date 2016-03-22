package com.zulkarninecsedictionary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DictionaryListActivity extends Activity {

	TextView userTextView;
	EditText searchEditText;
	Button searchButton;
	ListView dictionaryListView;
	
	String logTagString="DICTIONARY";
	ArrayList<WordDefinition> allWordDefinitions=new ArrayList<WordDefinition>();
	
	
	DictionaryDatabaseHelper myDictionaryDatabaseHelper;
	SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary_list);
		

		
		
		userTextView=(TextView) findViewById(R.id.textView);

		
		searchEditText=(EditText) findViewById(R.id.editText);
		searchButton=(Button) findViewById(R.id.button);
		//dictionaryListView=(ListView) findViewById(R.id.dictionaryListView);
		
		myDictionaryDatabaseHelper=new DictionaryDatabaseHelper(this, "Dictionary", null, 1);
		sharedPreferences=getSharedPreferences(MainActivity.SHARED_NAME_STRING, MODE_PRIVATE);
		
		
		boolean initialized=sharedPreferences.getBoolean("initialized", false);
		
		if (initialized==false) {
			//Log.d(logTagString, "initializing for the first time");
			initializeDatabase();
			SharedPreferences.Editor editor=sharedPreferences.edit();
			editor.putBoolean("initialized", true);
			editor.commit();
			
		}else {

		}
		
		allWordDefinitions=myDictionaryDatabaseHelper.getAllWords();
		

		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String string=searchEditText.getText().toString();
				
				WordDefinition wordDefinition=myDictionaryDatabaseHelper.getWordDefinition(string);
				
				if (wordDefinition==null) {
					Toast.makeText(DictionaryListActivity.this, "Word not found", Toast.LENGTH_LONG).show();
				}else {
					Intent intent =new Intent(DictionaryListActivity.this, WordDefinitionDetailActivity.class);
					intent.putExtra("word", wordDefinition.word);
					intent.putExtra("definition", wordDefinition.definition);
					
					startActivity(intent);
				}
				
				
			}
		});
		
		
		
		
	}

	//
	
	
	private void initializeDatabase() {
		InputStream inputStream=getResources().openRawResource(R.raw.my_dictionary);
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
		DictionaryLoader.loadData(bufferedReader, myDictionaryDatabaseHelper);
				
	}

	

}

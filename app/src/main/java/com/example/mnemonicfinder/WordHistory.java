package com.example.mnemonicfinder;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class WordHistory extends AppCompatActivity {
    private ArrayList<String> wordList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_history);
        wordList = getIntent().getStringArrayListExtra("wordList");
        Collections.reverse(wordList);
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                wordList);
        ListView wordHistoryList = findViewById(R.id.word_history_list);
        wordHistoryList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        wordHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}

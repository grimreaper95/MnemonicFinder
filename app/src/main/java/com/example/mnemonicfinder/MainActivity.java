package com.example.mnemonicfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mnemonicfinder.Adapters.MeaningAdapter;
import com.example.mnemonicfinder.Adapters.MnemonicAdapter;
import com.example.mnemonicfinder.Models.MeaningModel;
import com.example.mnemonicfinder.Models.MnemonicModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private ArrayList<String> mnemonicList = new ArrayList<>();
    private ArrayList<String> meaningList = new ArrayList<>();
    private ArrayList<String> wordHistory = new ArrayList<>();
    ViewPager mnemonicViewPager, meaningViewPager;

    MnemonicAdapter mnemonicAdapter;
    MeaningAdapter meaningAdapter;

    List<MnemonicModel> mnemonicModel;
    List<MeaningModel> meaningModel;

    ArrayList<Integer> colors;

    private int cnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.color1));
        colors.add(getResources().getColor(R.color.color2));
        colors.add(getResources().getColor(R.color.color3));
        colors.add(getResources().getColor(R.color.color4));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.search_history){

                    Intent intent = new Intent(MainActivity.this, WordHistory.class);
                    intent.putExtra("wordList", wordHistory);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search word here!");
        /*MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(getApplication(), "onMenuItemActionExpand called", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(getApplication(), "onMenutItemActionCollapse called", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                cnt++;
                getWindow().getDecorView().setBackgroundColor(colors.get(cnt % 4));
                getMeaningAndMnemonics(queryString);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isValid(String str){
        if(str.length() == 0) return false;
        str = str.trim().toLowerCase();
        int cnt = 0;
        for(int i = 0; i < str.length();i++){
            if(str.charAt(i) == ' '){
                cnt++;
            }
            if(str.charAt(i) < 'a' || str.charAt(i) > 'z'){
                return false;
            }
        }
        return (cnt == 0);
    }

    private String getMnemonicsUrl(String word){
        return "https://www.mnemonicdictionary.com/?word=" + word;
    }

    private String getMeaningUrl(String word){
        return "https://od-api.oxforddictionaries.com/api/v2/entries/en-gb/" + word + "?fields=definitions";
    }

    private void parseMnemonicResponse(String response){
        mnemonicList.clear();
        Document doc = Jsoup.parse(response);
        Elements mnemonicDiv = doc.select(".card-text p");
        for(Element mnemonic : mnemonicDiv){
            if(!mnemonic.text().startsWith("Powered by")) {
                mnemonicList.add(mnemonic.text());
            }
        }
        if(mnemonicList.size() == 0)
            mnemonicList.add("Sorry, no mnemonics present for this word");
    }

    private void parseMeaningResponse(String response){
        meaningList.clear();
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(response != null){
                JSONArray results = jsonObject.getJSONArray("results");
                Log.d("Shashank2", results.length() + "");
                for(int i = 0;i < results.length();i++){
                    JSONArray lexicalEntries = results.getJSONObject(i).getJSONArray("lexicalEntries");
                    JSONArray entries = lexicalEntries.getJSONObject(0).getJSONArray("entries");
                    JSONArray senses = entries.getJSONObject(0).getJSONArray("senses");
                    String res = senses.getJSONObject(0).getJSONArray("definitions").getString(0);
                    meaningList.add(res);
                }
            }
        }catch (Exception e){
            meaningList.clear();
        }
        if(meaningList.size() == 0)
            meaningList.add("Sorry, no meaning present for this word");
    }




    private void getMeaningAndMnemonics(String searchString) {
        if (!isValid(searchString)) {
            Toast.makeText(this, "Please enter a valid word", Toast.LENGTH_SHORT).show();
            return;
        }
        wordHistory.add(searchString);

        HashMap<String, String> meaningAuthHeader = new HashMap<>();
        meaningAuthHeader.put("Accept","application/json");
        meaningAuthHeader.put("app_id","8c915cdc");
        meaningAuthHeader.put("app_key","afa6d9a8221b40ca1111741204344106");

        new ConnectionHandler(this, meaningAuthHeader, getMeaningUrl(searchString)).getResponse(new OnSuccessCallback() {
            @Override
            public void onSuccess(String result) {
                parseMeaningResponse(result);
                Log.d("Shashank", "onSuccess meanig is: " + result);
                meaningModel = new ArrayList<>();
                for(int i = 0; i < meaningList.size();i++)
                    meaningModel.add(new MeaningModel(meaningList.get(i)));
                meaningAdapter = new MeaningAdapter(meaningModel, getApplicationContext());
                meaningViewPager = findViewById(R.id.meaning_view_pager);
                meaningViewPager.setAdapter(meaningAdapter);
                meaningViewPager.setPadding(130, 0, 130, 0);
            }
        });

        new ConnectionHandler(this, null, getMnemonicsUrl(searchString)).getResponse(new OnSuccessCallback() {
            @Override
            public void onSuccess(String result) {
                parseMnemonicResponse(result);
                Log.d("Shashank", "onSuccess -  mnemonic result: " + result);
                mnemonicModel = new ArrayList<>();
                for(int i = 0; i < mnemonicList.size();i++)
                    mnemonicModel.add(new MnemonicModel(mnemonicList.get(i)));
                mnemonicAdapter = new MnemonicAdapter(mnemonicModel, getApplicationContext());
                mnemonicViewPager = findViewById(R.id.mnemonic_view_pager);
                mnemonicViewPager.setAdapter(mnemonicAdapter);
                mnemonicViewPager.setPadding(130, 0, 130, 0);
            }
        });
    }

}

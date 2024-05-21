package com.example.ontherun.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.ontherun.clients.TextToSpeechClient;
import com.example.ontherun.fragments.MenuFragment;
import com.example.ontherun.R;
import com.example.ontherun.fragments.TextbookFragment;
import com.example.ontherun.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {

    SharedPreferences.OnSharedPreferenceChangeListener listener;
    public static final String LOG_CODE = "pPrive";
    private ActivityMainBinding binding;

    public static Bundle params;

    ArrayList<Voice> appVoices;

    public MenuFragment menuFragment = new MenuFragment();

    public static TextToSpeechClient ttsClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("pPrive", "START");

        appVoices = new ArrayList<>();
        params = new Bundle();
        ttsClient = new TextToSpeechClient(this);

        File folder = new File(getFilesDir(), "textbooks");
        if (!folder.exists()) folder.mkdir();


        //Voice newVoice = new Voice("test", locale, 200, 300, false, );

        //mTTS.speak(DataBaseActivity.getText(), TextToSpeech.QUEUE_FLUSH, null, "Итоги");
        //startActivity(intent);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        listener = (sharedPreferences, key) -> TextToSpeechClient.loadSettings();
        sp.registerOnSharedPreferenceChangeListener(listener);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, menuFragment, "menu").commit();

    }


    @Override
    public void onInit(int status) {

    }

    private void setLanguage(String lang) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(lang);
        if (!Objects.equals(config.getLocales().get(0), locale)) {
            Locale.setDefault(locale);

            config.setLocale(locale);
            this.createConfigurationContext(config);

            Intent refresh = new Intent(this, MainActivity.class);
            finish();
            startActivity(refresh);
        }


    }

    public void saveInt(int number, String name) {
        SharedPreferences sp = getSharedPreferences("saved", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name, number);
        editor.apply();
        Log.d(LOG_CODE, name);
    }

    public int loadInt(String name) {
        SharedPreferences sp = getSharedPreferences("saved", 0);
        Log.d(LOG_CODE, name);
        return sp.getInt(name, 0);
    }

    public void saveList(List<String> list, int number,  String name) {
        SharedPreferences sp = getSharedPreferences("saved", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(name + "_" + number + "_size", list.size());
        for (int i = 0; i < list.size(); ++i) {
            editor.putString(name + "_" + number + "_" + i, list.get(i));
        }
        Log.d(LOG_CODE, "Saved");
        editor.apply();
    }

    public List<String> loadList(String name, int number) {
        List<String> list = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("saved", 0);
        int size = sp.getInt(name + "_" + number + "_size", 0);
        for (int i = 0; i < size; ++i) {
            list.add(sp.getString(name + "_" + number + "_" + i, ""));
        }
        return list;
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown mTTS!
        Log.d(LOG_CODE, "pPokaa2");
        if (ttsClient != null) {
            ttsClient.onDestroy(this, "main");
        }
        super.onDestroy();
    }
}
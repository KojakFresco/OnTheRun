package com.example.ontherun.clients;

import static com.example.ontherun.activities.MainActivity.LOG_CODE;
import static com.example.ontherun.activities.MainActivity.ttsClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.ontherun.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TextToSpeechClient implements TextToSpeech.OnInitListener {


    public static TextToSpeech mTTS;

    Context context;

    static Bundle params;
    static ArrayList<Voice> appVoices;

    int sentenceCounter;
    int pageCounter;
    List<List<String>> textbook;

    public TextToSpeechClient(Context context) {
        mTTS = new TextToSpeech(context, this);
        this.context = context;
        appVoices = new ArrayList<>();
        params = new Bundle();
        sentenceCounter = 0;
        pageCounter = 0;
        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                sentenceCounter++;
            }

            @Override
            public void onDone(String utteranceId) {
                String[] numbers = utteranceId.split("_");
                if (numbers.length == 3) {
                    sentenceCounter = 0;
                    pageCounter++;
                    speakText();
                }
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }
    public void speakSentence(String sentence, String utteranceId) {
        mTTS.speak(sentence, TextToSpeech.QUEUE_ADD, params, utteranceId);
    }

    public void speakText() {
        List<String> sentences = textbook.get(pageCounter);
        for (int i = sentenceCounter; i < sentences.size(); ++i) {
            if (i == sentences.size() - 1) {
                speakSentence(sentences.get(i), pageCounter + "_" + i + "_last");
            } else {
                speakSentence(sentences.get(i), pageCounter + "_" + i);
            }
        }
    }

    public void setTextbook(List<List<String>> textbook) {
        this.textbook = textbook;
    }

//    public void speakTextbook(List<List<String>>  textbook) {
//        this.textbook = textbook;
//        for (int i = pageCounter; i < textbook.size(); ++i) {
//            speakText(textbook.get(i), i);
//        }
//    }

    public void setPage(int page) {
        pageCounter = page;
        sentenceCounter = 0;
    }
    public void setSentence(int sentence) {
        sentenceCounter = sentence;
    }

    public void pause() {
        mTTS.stop();
    }

    public void resume() {
        speakText();
        sentenceCounter--;
    }

    public void nextSentence(boolean isNext) {
        pause();
        sentenceCounter += isNext ? 1 : -1;
        if (sentenceCounter < 0) {
            pageCounter--;
            sentenceCounter = textbook.get(pageCounter).size() - 1;
        } else if (sentenceCounter > textbook.get(pageCounter).size() - 1) {
            pageCounter++;
            sentenceCounter = 0;
        }
        resume();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale locale = new Locale("ru_RU");

            int result = mTTS.setLanguage(locale);
            //int result = mTTS.setLanguage(Locale.getDefault());

            locale = mTTS.getVoice().getLocale();
            Set<Voice> voices = mTTS.getVoices();
            Set<Voice> ruVoices = new HashSet<>();
            for (Voice v : voices) {
                if (v.getLocale() == locale) {
                    ruVoices.add(v);
                }
            }
            int[] goodVoices = {1, 3, 4, 6, 7, 9};
            for (int i : goodVoices) {
                appVoices.add((Voice) ruVoices.toArray()[i]);
            }

            loadSettings();

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Извините, этот язык не поддерживается");
            } else {
                Log.d(LOG_CODE, "TTS is ready");
            }

        } else {
            Log.e("TTS", "Ошибка!");
        }
    }

    public static void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ttsClient.context);
        int volume = sp.getInt("volume", 50);
        int speed = sp.getInt("speed", 5);
        int voice = Integer.parseInt(sp.getString("voice", "0"));

        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, volume / 100f);
        mTTS.setSpeechRate((speed + 10) / 10f);
        mTTS.setVoice(appVoices.get(voice));

        //String language = sp.getString("lang", "en");
        //setLanguage(language);
    }

    public void onStop(MainActivity activity, String name) {
        activity.saveInt(sentenceCounter, name + "_sentence");
        activity.saveInt(pageCounter, name + "_page");
        mTTS.stop();
    }

    public void onDestroy(MainActivity activity, String name) {
        activity.saveInt(sentenceCounter, name + "_sentence");
        activity.saveInt(pageCounter, name + "_page");
        mTTS.stop();
        mTTS.shutdown();
    }
}

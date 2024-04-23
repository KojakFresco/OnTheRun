package com.example.ontherun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements
        TextToSpeech.OnInitListener {

    private Button mButton;
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File file = new File("C:/Users/Admin/AndroidStudioProjects/OnTheRun/app/src/main/res/drawable/screenshot.png");
        //Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        //TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        //Frame imageFrame = new Frame.Builder().setBitmap(bitmap).build();
        //String imageText = "";
        //SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        //for (int i = 0; i < textBlocks.size(); i++) {
        //    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
        //    imageText = textBlock.getValue();
        //}
        //System.out.println(imageText);

        mTTS = new TextToSpeech(this, this);
        mTTS.setSpeechRate(1.3f);

        mButton = findViewById(R.id.button);

        //Voice newVoice = new Voice("test", locale, 200, 300, false, );

        mButton.setOnClickListener(v -> {
            String text = "в тысяча девятьсот двадцатые годы происходила консолидация " +
                    "политической системы, коммунистическая партия стала единственной " +
                    "влиятельной политической силой. После смерти Ленина разгорелась острая " +
                    "борьба за власть в партийном руководстве. Победителем в этой борьбе стал " +
                    "генеральный секретарь ЦК ВКПб Сталин";
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, "Итоги");
        });
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
            Log.d("pPrive", String.valueOf(ruVoices.size()));
            mTTS.setVoice((Voice) ruVoices.toArray()[3]); // 1 3 4 6 7 9

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Извините, этот язык не поддерживается");
            } else {
                mButton.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Ошибка!");
        }



    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown mTTS!
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}
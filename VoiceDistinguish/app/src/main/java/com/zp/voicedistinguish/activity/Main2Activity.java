package com.zp.voicedistinguish.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.zp.voicedistinguish.R;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;


public class Main2Activity extends ActionBarActivity implements RecognitionListener {
    private static final String KWS_SEARCH = "wakeup";
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "ok light";

    private final String TAG = getClass().getSimpleName();
    private ImageView lightBulbImageView;
    private SpeechRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        runRecognizerSetup();

    }

    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(Main2Activity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    ((TextView) findViewById(R.id.caption_text)).setText("Failed to init recognizer " + result);
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup().setAcousticModel(new File(assetsDir, "en-us-ptm"))//设置声学模型的文件夹
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))//设置字典模型
                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // 创建短语监听
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        //创建命令文件监听
        //        File menuGrammar = new File(assetsDir, "menu.gram");
        //        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);
        //
        //        // Create grammar-based search for digit recognition
        //        File digitsGrammar = new File(assetsDir, "digits.gram");
        //        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);
        //
        //        // Create language model search
        //        File languageModel = new File(assetsDir, "weather.dmp");
        //        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
        //
        //        // Phonetic search
        //        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        //        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);
    }


    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        Log.e(TAG, "++++++++++++++++++>onResult()=" + hypothesis.getHypstr());
        //        for(String command:commands)
        //        {
        //            if (command.equals("hide")){
        //                Toast.makeText(this,"You said:"+command, Toast.LENGTH_SHORT).show();
        //                hideLightBulb();
        //                return;
        //            }
        //
        //            if (command.equals("show")){
        //                Toast.makeText(this,"You said:"+command, Toast.LENGTH_SHORT).show();
        //                showLightBulb();
        //                return;
        //            }
        //
        //            if (command.equals("turn yellow")){
        //                Toast.makeText(this,"You said:"+command, Toast.LENGTH_SHORT).show();
        //                turnYellow();
        //                return;
        //            }
        //
        //            if (command.equals("turn blue")){
        //                Toast.makeText(this,"You said:"+command, Toast.LENGTH_SHORT).show();
        //                turnBlue();
        //                return;
        //            }
        //
        //            if (command.equals("turn pink")){
        //                Toast.makeText(this,"You said:"+command, Toast.LENGTH_SHORT).show();
        //                turnPink();
        //                return;
        //            }
        //        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }
}

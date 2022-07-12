package com.example.present;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener{
    private boolean isOnFront = false;
    private int currentCard = 0;

    Content contentCard1Front1 = new Content("font", "style", 5, 1, "Speeches often start with a hook");
    Content contentCard1Back1 = new Content("font", "style", 5, 1, "A hook is anything that grabs the audience's attention");
    Content contentCard1Back2 = new Content("font", "style", 5, 1, "Examples of hooks are anecdotes, jokes, hot takes");
    Content contentCard1Back3 = new Content("font", "style", 5, 1, "Knowing targed audience leads to better hooks");

    Content contentCard2Back1 = new Content("font", "style", 5, 1, "The audience needs to first know why they should pay attention to your speech");
    Content contentCard2Back2 = new Content("font", "style", 5, 1, "Then, deliver on your promise");
    Content contentCard2Front1 = new Content("font", "style", 5, 1, "Bottom line upfront");

    Side sideFront1 = new Side(1, new Content[]{contentCard1Front1});
    Side sideBack1 = new Side(2, new Content[]{contentCard1Back1, contentCard1Back2, contentCard1Back3});

    Side sideFront2 = new Side(1, new Content[]{contentCard2Front1});
    Side sideBack2 = new Side(2, new Content[]{contentCard2Back1, contentCard2Back2});

    Card card1 = new Card(1, "Knowing targed audience leads to better hooks", 0, sideFront1, sideBack1);
    Card card2 = new Card(1, "Then, deliver on your promise", 1, sideFront2, sideBack2);

    Presentation pres = new Presentation(new Card[]{card1, card2});

    private Intent speechRecognizerIntent;
    private String TAG = "Speech Recognizer";

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private TextView speechText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_lay);

        fillLayout(linearLayout, pres, 0, isOnFront);

        linearLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeLeft() {
                if (currentCard >= pres.cards.length - 1) {
                    Toast.makeText(MainActivity.this, "No more cards!", Toast.LENGTH_SHORT).show();
                    return;
                }

                linearLayout.removeAllViews();
                currentCard++;
                fillLayout(linearLayout, pres, currentCard, isOnFront);
            }

            public void onSwipeRight() {
                if (currentCard <= 0) {
                    Toast.makeText(MainActivity.this, "No more cards!", Toast.LENGTH_SHORT).show();
                    return;
                }

                linearLayout.removeAllViews();
                currentCard--;
                fillLayout(linearLayout, pres, currentCard, isOnFront);
            }

            public void onSwipeBottom() {
                linearLayout.removeAllViews();

                //display other side of card
                isOnFront = !isOnFront;
                fillLayout(linearLayout, pres, currentCard, isOnFront);
            }

            public void onSwipeTop() {
                linearLayout.removeAllViews();

                //display other side of card
                isOnFront = !isOnFront;
                fillLayout(linearLayout, pres, currentCard, isOnFront);
            }
        });

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        speechText = findViewById(R.id.speech_text);

        resetSpeechRecognizer();
        createRecognizerIntent();
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    //stop listening when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        speechRecognizer.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetSpeechRecognizer();
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    //stop listening when activity is stopped
    @Override
    protected void onStop() {
        super.onStop();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    //dynamically create a textview
    //modified from https://stackoverflow.com/questions/4203506/how-to-add-a-textview-to-a-linearlayout-dynamically-in-android
    public TextView createTextView(Presentation pres, int cardIndex, int contentIndex, boolean isOnFront) {
        TextView textView1 = new TextView(MainActivity.this);
        textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        if (isOnFront) {
            textView1.setText(pres.cards[cardIndex].front.content[contentIndex].message);
        } else {
            textView1.setText(pres.cards[cardIndex].back.content[contentIndex].message);
        }
        textView1.setBackgroundColor(0xffffffff); // hex color 0xAARRGGBB
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)

        return textView1;
    }

    //populate a linear layout with all messages in a content array
    public void fillLayout (LinearLayout linearLayout, Presentation pres, int cardIndex, boolean isOnFront) {
        if (isOnFront) {
            for (int i = 0; i < pres.cards[currentCard].front.content.length; i++) {
                linearLayout.addView(createTextView(pres, cardIndex, i, isOnFront));
            }
        } else {
            for (int i = 0; i < pres.cards[currentCard].back.content.length; i++) {
                linearLayout.addView(createTextView(pres, cardIndex, i, isOnFront));
            }
        }
    }

    private void checkPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
    }

    private void createRecognizerIntent() {
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
    }

    //@TODO cite this
    private void resetSpeechRecognizer() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        if (speechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer.setRecognitionListener(this);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        speechRecognizer.stopListening();
    }

    @Override
    public void onError(int error) {
        Log.d(TAG, "Error: " + error);
        resetSpeechRecognizer();
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        speechText.setText(data.get(0));
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }




}
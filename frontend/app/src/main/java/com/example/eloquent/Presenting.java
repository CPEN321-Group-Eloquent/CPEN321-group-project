package com.example.eloquent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import java.util.ArrayList;

public class Presenting extends AppCompatActivity implements RecognitionListener {

    private boolean isOnFront = false;
    private int currentCard = 0;
    Presentation pres;

    private Intent speechRecognizerIntent;
    private String TAG = "Speech Recognizer";

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private TextView speechText;
    private LinearLayout linearLayout;

//    Content contentCard1Front1 = new Content("font", "style", 5, 1, "Speeches often start with a hook");
//    Content contentCard1Back1 = new Content("font", "style", 5, 1, "A hook is anything that grabs the audience's attention");
//    Content contentCard1Back2 = new Content("font", "style", 5, 1, "Examples of hooks are anecdotes, jokes, hot takes");
//    Content contentCard1Back3 = new Content("font", "style", 5, 1, "Knowing targed audience leads to better hooks");
//
//    Content contentCard2Back1 = new Content("font", "style", 5, 1, "The audience needs to first know why they should pay attention to your speech");
//    Content contentCard2Back2 = new Content("font", "style", 5, 1, "Then, deliver on your promise");
//    Content contentCard2Front1 = new Content("font", "style", 5, 1, "Bottom line upfront");
//
//    Front sideFront1 = new Front(1, new Content[]{contentCard1Front1});
//    Back sideBack1 = new Back(2, new Content[]{contentCard1Back1, contentCard1Back2, contentCard1Back3});
//
//    Front sideFront2 = new Front(1, new Content[]{contentCard2Front1});
//    Back sideBack2 = new Back(2, new Content[]{contentCard2Back1, contentCard2Back2});
//
//    Cards card1 = new Cards(1, "Knowing targed audience leads to better hooks", 0, sideFront1, sideBack1);
//    Cards card2 = new Cards(1, "Then, deliver on your promise", 1, sideFront2, sideBack2);


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenting);

        pres = (Presentation) getIntent().getSerializableExtra("Presentation");
        linearLayout = (LinearLayout) findViewById(R.id.linear_lay);
        fillLayout(linearLayout, pres, 0, isOnFront);


        linearLayout.setOnTouchListener(new OnSwipeTouchListener(Presenting.this) {
            public void onSwipeLeft() {
                if (currentCard >= pres.cueCards.size() - 1) {
                    Toast.makeText(Presenting.this, "No more cards!", Toast.LENGTH_SHORT).show();
                    return;
                }

                linearLayout.removeAllViews();
                currentCard++;
                fillLayout(linearLayout, pres, currentCard, isOnFront);
            }

            public void onSwipeRight() {
                if (currentCard <= 0) {
                    Toast.makeText(Presenting.this, "No more cards!", Toast.LENGTH_SHORT).show();
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

        //get permissions for audio
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
//            checkPermission();
//        }
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

    //start listening when activity is resumed
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
    public TextView createTextView(Presentation pres, int cardIndex, boolean isOnFront) {
        TextView textView1 = new TextView(Presenting.this);
        textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        if (isOnFront) {
            Spannable spannable = getColoredtext(pres.cueCards.get(cardIndex).front.content.colour, pres.cueCards.get(cardIndex).front.content.message);
            textView1.setText(spannable);

        } else {
            Spannable spannable = getColoredtext(pres.cueCards.get(cardIndex).back.content.colour, pres.cueCards.get(cardIndex).back.content.message);
            textView1.setText(spannable);
        }
        textView1.setBackgroundColor(0xffffffff); // hex color 0xAARRGGBB
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)

        return textView1;
    }

    //populate a linear layout with all messages in a content array
    public void fillLayout (LinearLayout linearLayout, Presentation pres, int cardIndex, boolean isOnFront) {
        if (isOnFront) {
            linearLayout.addView(createTextView(pres, cardIndex, isOnFront));
        } else {

            linearLayout.addView(createTextView(pres, cardIndex,  isOnFront));
        }
    }
//    private void checkPermission() {
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
//    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "We need these location permissions to run!", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Need Recording Permissions")
                        .setMessage("We need your audio recording permissions to mark automatically switch cue cards")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Presenting.this, "We need these location permissions to run!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(Presenting.this, new String[] {Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
            }
        }
    }

    private void createRecognizerIntent() {
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
    }

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
        //see if a substring in the text matches the transition phrase, ignoring case and punctuation
        if (data.get(0).toLowerCase().contains(pres.cueCards.get(currentCard).transitionPhrase.toLowerCase().replaceAll("\\p{Punct}", ""))) {
            if (currentCard >= pres.cueCards.size() - 1) {
                Toast.makeText(Presenting.this, "No more cards!", Toast.LENGTH_SHORT).show();
                return;
            }

            linearLayout.removeAllViews();
            currentCard++;
            fillLayout(linearLayout, pres, currentCard, isOnFront);
        }
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private Spannable getColoredtext(int color, String text){
        Spannable colored_text = new SpannableString(text);
        colored_text.setSpan(new ForegroundColorSpan(color),0,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return colored_text;
    }
}

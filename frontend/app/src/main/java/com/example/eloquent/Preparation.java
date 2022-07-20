package com.example.eloquent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Preparation extends AppCompatActivity {

    private ImageButton nextButton;
    private ImageButton backButton;
    private ImageButton flipButton;
    private ImageButton addButton;
    private ImageButton deleteButton;
    private ImageButton swapnextButton;
    private ImageButton swaplastButton;
    private ImageButton redoButton;
    private ImageButton undoButton;
    private TextView pageNumber;
    private EditText content;
    private TextViewUndoRedo helper;
    private Presentation presentation = new Presentation();
    private int cueCards_num = 0;
    private int cueCards_max = 0;
    private int content_num = 0;
    private int cardFace = 0;//0: front | 1: back
    ObjectMapper objectMapper = new ObjectMapper();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);

        /**
         * get json from backend server by request
         */

//        try {
//            presentation = objectMapper.readValue(json, Preparation.class);
//        }
//        catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


        //then change json to pres obj


        cueCards_max = 3;

        Content content1 = new Content(Color.BLUE,"");
        Content content2 = new Content(Color.BLUE,"1Back");
        Back back1 = new Back(Color.BLACK);
        back1.content=content2;
        Front front1 = new Front(Color.WHITE);
        front1.content=content1;
        Cards card1 = new Cards(front1,back1,Color.WHITE);

        Content content3 = new Content(Color.BLUE,"2Front");
        Content content4 = new Content(Color.BLUE,"2Back");
        Back back2 = new Back(Color.BLACK);
        back2.content=content4;
        Front front2 = new Front(Color.WHITE);
        front2.content=content3;
        Cards card2 = new Cards(front2,back2,Color.WHITE);

        Content content5 = new Content(Color.BLUE,"3Front");
        Content content6 = new Content(Color.BLUE,"3Back");
        Back back3 = new Back(Color.BLACK);
        back3.content=content6;
        Front front3 = new Front(Color.WHITE);
        front3.content=content5;
        Cards card3 = new Cards(front3,back3,Color.WHITE);

        presentation.cueCards.add(card1);
        presentation.cueCards.add(card2);
        presentation.cueCards.add(card3);
        Log.w("TAG", "set success");

        //start page


        //!!!!!!!!!!


        pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));

        View back = findViewById(R.id.cueCard_background);
        back.setBackgroundColor(65280);

        content = findViewById(R.id.cueCard);
        helper = new TextViewUndoRedo(content);
        content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
//        Log.w("TAG", "background color success"+"|| cue card number is " +Integer.toString(cueCards_num));
//        Log.w("TAG", "background color is " +Integer.toString(presentation.getCards(cueCards_num).getFront().getBackground_color()));
        String text = presentation.cueCards.get(cueCards_num).front.getContent().getMessage();
//        Log.w("TAG", "text is " +text);
        int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
        content.setText(getColoredtext(color,text));
//        Log.w("TAG", "text success"+"|| cue card number is " +Integer.toString(cueCards_num));
        //content.setText(presentation.getCards(cueCards_num).getFront().getContent(content_num).getMessage());


        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first, save the change on the edit text


                String change = content.getText().toString();
                Cards tmp = presentation.cueCards.get(cueCards_num);
//                Log.w("TAG", "get success" + change);

                if(cardFace==0) {//front
                    tmp.front.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
                else{//back
                    tmp.back.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
//                Log.w("TAG", "save success");

                //second, change to the next page

                if(cueCards_num<cueCards_max-1){
                    cueCards_num = cueCards_num+1;
                    cardFace=0;

                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                    content.setText(getColoredtext(color,text));
                    pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                    helper.clearHistory();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Max number, cannot go to the next page",Toast.LENGTH_SHORT).show();
                }



            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first, save the change on the edit text


                String change = content.getText().toString();
                Cards tmp = presentation.cueCards.get(cueCards_num);

                if(cardFace==0) { //front
                    tmp.front.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
                else{ // back
                    tmp.back.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
//                Log.w("TAG", "save success");

                //second, change to the last page

                if(cueCards_num>0){
                    cueCards_num = cueCards_num-1;
                    cardFace=0;

                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                    content.setText(getColoredtext(color,text));
                    pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                    helper.clearHistory();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Min number, cannot go to the last page",Toast.LENGTH_SHORT).show();
                }

            }
        });

        flipButton = findViewById(R.id.flipButton);
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //save the change on the edit text and flip the page
                String change = content.getText().toString();
                Cards tmp = presentation.cueCards.get(cueCards_num);

                if(cardFace==0) { //front
                    tmp.front.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
//                    Log.w("TAG", "save success");
                    cardFace = 1;
                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getBack().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getBack().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getBack().getContent().getColor();
                    content.setText(getColoredtext(color,text));
                }
                else{ // back
                    tmp.back.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
//                    Log.w("TAG", "save success");
                    cardFace = 0;

                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                    content.setText(getColoredtext(color,text));
                }

                helper.clearHistory();

            }
        });

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first, save the change on the edit text


                String change = content.getText().toString();
                Cards tmp = presentation.cueCards.get(cueCards_num);
//                Log.w("TAG", "get success" + change);

                if(cardFace==0) {
                    tmp.front.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
                else{
                    tmp.back.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
//                Log.w("TAG", "save success");

                //second, create a new page

                Content new_content_front = new Content(Color.BLACK,"");
                Content new_content_back = new Content(Color.BLACK,"");
                Front new_front = new Front(Color.WHITE);
                new_front.content=new_content_front;
                Back new_back = new Back(Color.WHITE);
                new_back.content=new_content_back;
                Cards new_card = new Cards(new_front,new_back,Color.WHITE);

                //Third, change page position

                presentation.cueCards.add(new_card);
                cueCards_max=cueCards_max+1;
                for(int i=cueCards_max-1; i>cueCards_num; i=i-1) {
                    presentation.cueCards.set(i, presentation.cueCards.get(i - 1));
                }
                presentation.cueCards.set(cueCards_num, new_card);

                cardFace=0;

                content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                content.setText(getColoredtext(color,text));

                pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                helper.clearHistory();


            }
        });

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createDialog();
                //Don't need to save because the content will be deleted

                //change page position

                for(int i=cueCards_num; i<cueCards_max-1; i=i+1) {
                    presentation.cueCards.set(i, presentation.cueCards.get(i + 1));
                }

                presentation.cueCards.remove(cueCards_max-1);
                cueCards_max=cueCards_max-1;

                if(cueCards_num>cueCards_max-1 && cueCards_max!=0){//if this is the last page, go back to previous page
                    cueCards_num = cueCards_num-1;
                }
                else if(cueCards_max == 0){//if no page left after delete, create a new empty page
                    Content new_content_front = new Content(Color.BLACK,"");
                    Content new_content_back = new Content(Color.BLACK,"");
                    Front new_front = new Front(Color.WHITE);
                    new_front.content=new_content_front;
                    Back new_back = new Back(Color.WHITE);
                    new_back.content=new_content_back;
                    Cards new_card = new Cards(new_front,new_back,Color.WHITE);
                    presentation.cueCards.add(new_card);
                    cueCards_max=cueCards_max+1;
                }
//                Log.w("TAG", "get max : " + cueCards_max);
//                Log.w("TAG", "get num : " + (cueCards_num+1));

                cardFace=0;

                content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                content.setText(getColoredtext(color,text));

                pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                helper.clearHistory();


            }
        });

        swapnextButton = findViewById(R.id.swapnextButton);
        swapnextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first, save the change on the edit text


                String change = content.getText().toString();
                Cards tmp = presentation.cueCards.get(cueCards_num);
//                Log.w("TAG", "get success" + change);

                if(cardFace==0) {
                    tmp.front.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
                else{
                    tmp.back.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }


                //Second, change page position

                if(cueCards_num<cueCards_max-1){
                    //
                    Cards temp = presentation.getCards(cueCards_num);
                    presentation.cueCards.set(cueCards_num,presentation.cueCards.get(cueCards_num+1));
                    presentation.cueCards.set(cueCards_num+1,temp);

                    cardFace = 0;

                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                    content.setText(getColoredtext(color,text));

                }
                else{
                    Toast.makeText(getApplicationContext(),"Max number, cannot swap with the next page",Toast.LENGTH_SHORT).show();
                }



                pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                helper.clearHistory();


            }
        });

        swaplastButton = findViewById(R.id.swaplastButton);
        swaplastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first, save the change on the edit text


                String change = content.getText().toString();
                Cards tmp = presentation.cueCards.get(cueCards_num);
//                Log.w("TAG", "get success" + change);

                if(cardFace==0) {
                    tmp.front.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }
                else{
                    tmp.back.content.setMessage(change);
                    presentation.cueCards.set(cueCards_num,tmp);
                }


                //Second, change page position

                if(cueCards_num>0){
                    //
                    Cards temp = presentation.getCards(cueCards_num);
                    presentation.cueCards.set(cueCards_num,presentation.cueCards.get(cueCards_num-1));
                    presentation.cueCards.set(cueCards_num-1,temp);

                    cardFace = 0;

                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                    content.setText(getColoredtext(color,text));

                }
                else{
                    Toast.makeText(getApplicationContext(),"Min number, cannot swap with the last page",Toast.LENGTH_SHORT).show();
                }



                pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                helper.clearHistory();


            }
        });

        undoButton = findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.undo();

            }
        });

        redoButton = findViewById(R.id.redoButton);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.redo();

            }
        });


    }

    private void createDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Are you sure you want to make this action?");
        adb.setCancelable(false);

        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                for(int i=cueCards_num; i<cueCards_max-1; i=i+1) {
                    presentation.cueCards.set(i, presentation.cueCards.get(i + 1));
                }

                presentation.cueCards.remove(cueCards_max-1);
                cueCards_max=cueCards_max-1;

                if(cueCards_num>cueCards_max-1 && cueCards_max!=0){//if this is the last page, go back to previous page
                    cueCards_num = cueCards_num-1;
                }
                else if(cueCards_max == 0){//if no page left after delete, create a new empty page
                    Content new_content_front = new Content(Color.BLACK,"");
                    Content new_content_back = new Content(Color.BLACK,"");
                    Front new_front = new Front(Color.WHITE);
                    new_front.content=new_content_front;
                    Back new_back = new Back(Color.WHITE);
                    new_back.content=new_content_back;
                    Cards new_card = new Cards(new_front,new_back,Color.WHITE);
                    presentation.cueCards.add(new_card);
                    cueCards_max=cueCards_max+1;
                }
//                Log.w("TAG", "get max : " + cueCards_max);
//                Log.w("TAG", "get num : " + (cueCards_num+1));

                cardFace=0;

                content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();

                content.setText(getColoredtext(color,text));

                pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
                helper.clearHistory();
            }
        });
    }

    private Spannable getColoredtext(int color, String text){
        Spannable colored_text = new SpannableString(text);
        colored_text.setSpan(new ForegroundColorSpan(color),0,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return colored_text;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("TAG", "back button pressed");
            try {
                String presentationJson = objectMapper.writeValueAsString(presentation);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        boolean fromNewActivity=true;

        Intent mainIntent = new Intent(this, MainActivity.class);
        Bundle bundleObj = new Bundle();
        bundleObj.putString("fromNewActivity", Boolean.toString(fromNewActivity));
        mainIntent.putExtras(bundleObj);
        startActivityForResult(mainIntent, 0);
    }

}
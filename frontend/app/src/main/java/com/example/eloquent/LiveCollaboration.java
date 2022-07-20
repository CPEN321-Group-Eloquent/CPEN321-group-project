package com.example.eloquent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;



public class LiveCollaboration extends AppCompatActivity {

    private ImageButton nextButton;
    private ImageButton backButton;
    private ImageButton flipButton;
    private TextView pageNumber;
    private EditText content;
    private Presentation presentation;
    private String presentationID = "10000";
    private int cueCards_num = 0;
    private int cueCards_max = 0;
    private int content_num = 0;
    private int cardFace = 0;//0: front | 1: back
    private int userID = 120;
    private String title = "0";
    ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketClient webSocketClient;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_collaboration);

        //get json from backend



        //then change json to pres obj


        cueCards_max = 3;
        presentation = new Presentation(title,presentationID);

        Content content1 = new Content(Color.BLUE,"1Front");
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


        LiveCollaboration.this.runOnUiThread(new Runnable() {
            public void run() {
                createWebSocketClient();
            }
        });




        content = findViewById(R.id.cueCard);

        View back = findViewById(R.id.cueCard_background);
        back.setBackgroundColor(65280);

        if(cardFace == 0){
            content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
            String text = presentation.cueCards.get(cueCards_num).front.getContent().getMessage();
            int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
            content.setText(getColoredtext(color,text));
        }
        else{
            content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getBack().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
            String text = presentation.cueCards.get(cueCards_num).back.getContent().getMessage();
            int color = presentation.getCards(cueCards_num).getBack().getContent().getColor();
            content.setText(getColoredtext(color,text));
        }

        pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                JSONObject obj =  new JSONObject();
                try{
                    obj.put("userID",Integer.toString(userID));
                    obj.put("presentationID",presentationID);
                    obj.put("cueCards_num",Integer.toString(cueCards_num));
                    obj.put("cardFace",Integer.toString(cardFace));
                    String recent_text = content.getText().toString();
                    obj.put("recent_text",recent_text);
                }catch (JSONException e){
                    e.printStackTrace();
                }

                webSocketClient.send(obj.toString());
            }
        });



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


                //second, change to the next page

                if(cueCards_num<cueCards_max-1){
                    cueCards_num = cueCards_num+1;
                    cardFace=0;
                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
                    content.setText(getColoredtext(color,text));
                    pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));

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
                Log.w("TAG", "save success");

                //second, change to the last page

                if(cueCards_num>0){
                    cueCards_num = cueCards_num-1;
                    cardFace=0;
                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                    String text = presentation.getCards(cueCards_num).getFront().getContent().getMessage();
                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
                    content.setText(getColoredtext(color,text));
                    pageNumber.setText(Integer.toString(cueCards_num+1)+"/"+Integer.toString(cueCards_max));
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


            }
        });


    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://20.104.77.70:80/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                JSONObject obj =  new JSONObject();
                try{
                    obj.put("presentationID",presentationID);
//                    obj.put("cardFace",Integer.toString(cardFace));
//                    String recent_text = content.getText().toString();
//                    obj.put("recent_text",recent_text);

//                obj.put("mmBefore",tmp.mmBefore.toString());
//                obj.put("mmAfter",tmp.mmAfter.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }

                webSocketClient.send(obj.toString());
            }

            @Override
            public void onTextReceived(String s) {



                Log.i("WebSocket", "Message received");
                Log.i("WebSocket", s);
                JSONObject tmpjson = null;
                try {
                    tmpjson = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(tmpjson.has("presentation")){
                    //pack recent presentation obj to json and send to server
                    webSocketClient.send(s.toString());
                    Log.w("TAG", "PRESENTATION");
                }
                else{

                    if(tmpjson.has("cueCards_num")&&tmpjson.has("cardFace")){  // This is a change
                        Log.w("TAG", "Change");

                        int change_userID = 0;
                        try {
                            change_userID = Integer.valueOf(tmpjson.getString("userID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (userID == change_userID){
                            Log.w("TAG", "==");

                        }
                        else{
                            String change_presentationID = null;
                            try {
                                change_presentationID = tmpjson.getString("presentationID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int change_cueCards_num = 0;
                            try {
                                change_cueCards_num = Integer.valueOf(tmpjson.getString("cueCards_num"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int change_cardFace = 0;
                            try {
                                change_cardFace = Integer.valueOf(tmpjson.getString("cardFace"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String change_recent_text = null;
                            try {
                                change_recent_text = tmpjson.getString("recent_text");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.w("TAG", "111");
                            if(change_presentationID.equals(presentationID) ){

                                Cards tmp = presentation.cueCards.get(change_cueCards_num);

                                // change variable in presentation Obj

                                if(change_cardFace==0) {//front
                                    tmp.front.content.setMessage(change_recent_text);
                                    presentation.cueCards.set(change_cueCards_num,tmp);
                                }
                                else{//back
                                    tmp.back.content.setMessage(change_recent_text);
                                    presentation.cueCards.set(cueCards_num,tmp);
                                }
                                Log.w("TAG", "change save");

                                //refresh editText

                                LiveCollaboration.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        if(cardFace == 0){
                                            content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                            String text = presentation.cueCards.get(cueCards_num).front.getContent().getMessage();
                                            int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
                                            content.setText(getColoredtext(color,text));
                                        }
                                        else{
                                            content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getBack().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                            String text = presentation.cueCards.get(cueCards_num).back.getContent().getMessage();
                                            int color = presentation.getCards(cueCards_num).getBack().getContent().getColor();
                                            content.setText(getColoredtext(color,text));
                                        }
                                        Log.w("TAG", "change refresh");
                                    }
                                });



                            }
                            else{
                                // do nothing if the presentationID is different
                                Log.w("TAG", "222");
                            }
                        }




                    }
                    else if(tmpjson.has("title")){// this is a presentation json
                        //change json to presentation obj

                        Presentation tmp_pres = new Presentation();
                        Content content1 = new Content(Color.BLUE,"1Fronttest");
                        Content content2 = new Content(Color.BLUE,"1Backtest");
                        Back back1 = new Back(Color.BLACK);
                        back1.content=content2;
                        Front front1 = new Front(Color.WHITE);
                        front1.content=content1;
                        Cards card1 = new Cards(front1,back1,Color.WHITE);

                        Content content3 = new Content(Color.BLUE,"2Fronttest");
                        Content content4 = new Content(Color.BLUE,"2Backtest");
                        Back back2 = new Back(Color.BLACK);
                        back2.content=content4;
                        Front front2 = new Front(Color.WHITE);
                        front2.content=content3;
                        Cards card2 = new Cards(front2,back2,Color.WHITE);

                        Content content5 = new Content(Color.BLUE,"3Fronttest");
                        Content content6 = new Content(Color.BLUE,"3Backtest");
                        Back back3 = new Back(Color.BLACK);
                        back3.content=content6;
                        Front front3 = new Front(Color.WHITE);
                        front3.content=content5;
                        Cards card3 = new Cards(front3,back3,Color.WHITE);

                        tmp_pres.cueCards.add(card1);
                        tmp_pres.cueCards.add(card2);
                        tmp_pres.cueCards.add(card3);

                        //reset presentation

                        presentation = tmp_pres;
                        Log.w("TAG", "newPresentation save");

                        LiveCollaboration.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if(cardFace == 0){
                                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                    String text = presentation.cueCards.get(cueCards_num).front.getContent().getMessage();
                                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
                                    content.setText(getColoredtext(color,text));
                                }
                                else{
                                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getBack().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                    String text = presentation.cueCards.get(cueCards_num).back.getContent().getMessage();
                                    int color = presentation.getCards(cueCards_num).getBack().getContent().getColor();
                                    content.setText(getColoredtext(color,text));
                                }
                                Log.w("TAG", "newPresentation refresh");
                            }
                        });

                    }
                    else{

                    }


                }
//                final String message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                Log.i("WebSocket", "Bin received");
                String s = new String(data);
                Log.i("WebSocket", s);
                JSONObject tmpjson = null;
                try {
                    tmpjson = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(tmpjson.has("presentation")){
                    //pack recent presentation obj to json and send to server
                    webSocketClient.send(s.toString());
                    Log.w("TAG", "PRESENTATION");
                }
                else{

                    if(tmpjson.has("cueCards_num")&&tmpjson.has("cardFace")){  // This is a change
                        Log.w("TAG", "Change");


                        int change_userID = 0;
                        try {
                            change_userID = Integer.valueOf(tmpjson.getString("userID"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (userID == change_userID){ // do nothing if user ID the same
                            Log.w("TAG", "==");
                        }
                        else{
                            String change_presentationID = null;
                            try {
                                change_presentationID = tmpjson.getString("presentationID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int change_cueCards_num = 0;
                            try {
                                change_cueCards_num = Integer.valueOf(tmpjson.getString("cueCards_num"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            int change_cardFace = 0;
                            try {
                                change_cardFace = Integer.valueOf(tmpjson.getString("cardFace"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String change_recent_text = null;
                            try {
                                change_recent_text = tmpjson.getString("recent_text");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(change_presentationID.equals(presentationID)){
                                Log.w("TAG", "111");

                                Cards tmp = presentation.cueCards.get(change_cueCards_num);

                                // change variable in presentation Obj

                                if(change_cardFace==0) {//front
                                    tmp.front.content.setMessage(change_recent_text);
                                    presentation.cueCards.set(change_cueCards_num,tmp);
                                }
                                else{//back
                                    tmp.back.content.setMessage(change_recent_text);
                                    presentation.cueCards.set(cueCards_num,tmp);
                                }
                                Log.w("TAG", "change save");

                                //refresh editText

                                LiveCollaboration.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        if(cardFace == 0){
                                            content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                            String text = presentation.cueCards.get(cueCards_num).front.getContent().getMessage();
                                            int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
                                            content.setText(getColoredtext(color,text));
                                        }
                                        else{
                                            content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getBack().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                            String text = presentation.cueCards.get(cueCards_num).back.getContent().getMessage();
                                            int color = presentation.getCards(cueCards_num).getBack().getContent().getColor();
                                            content.setText(getColoredtext(color,text));
                                        }
                                        Log.w("TAG", "change refresh");
                                    }
                                });

                            }
                            else{
                                // do nothing if the presentationID is different
                                Log.w("TAG", "222");
                                Log.w("TAG", change_presentationID);
                                Log.w("TAG", presentationID);
                            }
                        }

                    }
                    else if(tmpjson.has("title")){// this is a presentation json

                        Log.w("TAG", "newPresentation");
                        //change json to presentation obj

                        Presentation tmp_pres = new Presentation();
                        Content content1 = new Content(Color.BLUE,"1Fronttest");
                        Content content2 = new Content(Color.BLUE,"1Backtest");
                        Back back1 = new Back(Color.BLACK);
                        back1.content=content2;
                        Front front1 = new Front(Color.WHITE);
                        front1.content=content1;
                        Cards card1 = new Cards(front1,back1,Color.WHITE);

                        Content content3 = new Content(Color.BLUE,"2Fronttest");
                        Content content4 = new Content(Color.BLUE,"2Backtest");
                        Back back2 = new Back(Color.BLACK);
                        back2.content=content4;
                        Front front2 = new Front(Color.WHITE);
                        front2.content=content3;
                        Cards card2 = new Cards(front2,back2,Color.WHITE);

                        Content content5 = new Content(Color.BLUE,"3Fronttest");
                        Content content6 = new Content(Color.BLUE,"3Backtest");
                        Back back3 = new Back(Color.BLACK);
                        back3.content=content6;
                        Front front3 = new Front(Color.WHITE);
                        front3.content=content5;
                        Cards card3 = new Cards(front3,back3,Color.WHITE);

                        tmp_pres.cueCards.add(card1);
                        tmp_pres.cueCards.add(card2);
                        tmp_pres.cueCards.add(card3);


                        //reset presentation
                        Log.w("TAG", "newPresentation save");

                        presentation = tmp_pres;

                        LiveCollaboration.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if(cardFace == 0){
                                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getFront().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                    String text = presentation.cueCards.get(cueCards_num).front.getContent().getMessage();
                                    int color = presentation.getCards(cueCards_num).getFront().getContent().getColor();
                                    content.setText(getColoredtext(color,text));
                                }
                                else{
                                    content.getBackground().setColorFilter(presentation.getCards(cueCards_num).getBack().getBackgroundColor(), PorterDuff.Mode.SRC_ATOP);
                                    String text = presentation.cueCards.get(cueCards_num).back.getContent().getMessage();
                                    int color = presentation.getCards(cueCards_num).getBack().getContent().getColor();
                                    content.setText(getColoredtext(color,text));
                                }
                                Log.w("TAG", "newPresentation refresh");
                            }
                        });



                    }
                    else{

                    }


                }
            }

            @Override
            public void onPingReceived(byte[] data) {
                Log.i("WebSocket", "Pin received");
            }

            @Override
            public void onPongReceived(byte[] data) {
                Log.i("WebSocket", "Pon received");
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
                Log.i("WebSocket", "Error");
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.enableAutomaticReconnection(1000);
        webSocketClient.connect();
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
            webSocketClient.close();
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
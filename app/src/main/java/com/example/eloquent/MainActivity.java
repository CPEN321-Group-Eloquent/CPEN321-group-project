package com.example.eloquent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnPresListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    List<Presentation> presentations;
    Adapter adapter;

    Presentation presentation = new Presentation("test1");
    Presentation presentation2 = new Presentation("test2");
    Presentation passedPres;
    private String TAG = "MainActivity.this";

    Content contentCard1Front1 = new Content("font", "style", 5, Color.BLUE, "Speeches often start with a hook");
    Content contentCard1Back1 = new Content("font", "style", 5, Color.BLUE, "A hook is anything that grabs the audience's attention");
    Content contentCard1Back2 = new Content("font", "style", 5, Color.BLUE, "Examples of hooks are anecdotes, jokes, hot takes");
    Content contentCard1Back3 = new Content("font", "style", 5, Color.BLUE, "Knowing target audience leads to better hooks");

    Content contentCard2Back1 = new Content("font", "style", 5, Color.BLUE, "The audience needs to first know why they should pay attention to your speech");
    Content contentCard2Back2 = new Content("font", "style", 5, Color.BLUE, "Then, deliver on your promise");
    Content contentCard2Front1 = new Content("font", "style", 5, Color.BLUE, "Bottom line upfront");

    Front sideFront1 = new Front(Color.WHITE, contentCard1Front1);
    Back sideBack1 = new Back(Color.WHITE, contentCard1Back1);

    Front sideFront2 = new Front(Color.WHITE, contentCard2Front1 );
    Back sideBack2 = new Back(Color.WHITE, contentCard2Back1);

    Cards card1 = new Cards(Color.WHITE, "Knowing target audience leads to better hooks", 0, sideFront1, sideBack1);
    Cards card2 = new Cards(Color.WHITE, "Then, deliver on your promise", 1, sideFront2, sideBack2);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presentation.cueCards.add(card1);
        presentation.cueCards.add(card2);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.listOfPres);
        presentations = new ArrayList<>();

        adapter = new Adapter(this, presentations, this::selectedPres);
        //extractPresentations();

        // put here to test the add functionality
        passedPres = (Presentation) getIntent().getSerializableExtra("Presentation");
        if(passedPres != null) {
            Log.d(TAG, passedPres.getTitle());
            presentations.add(passedPres);
        }

        showPresentation();



//        listView = findViewById(R.id.listView);
//        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);
//        listView.setAdapter(arrayAdapter);
    }

    private void showPresentation() {

        presentations.add(presentation);
        presentations.add(presentation2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add) {
            Intent addPres = new Intent(this, AddPres.class);
            startActivity(addPres);
        }
        if(item.getItemId() == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        getMenuInflater().inflate(R.menu.add_menu, menu);
        MenuItem menuitem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuitem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void selectedPres(Presentation presentation) {
        Intent intent = new Intent(this, EditPres.class);
        intent.putExtra("Presentation", presentation);
        startActivity(intent);
    }
}
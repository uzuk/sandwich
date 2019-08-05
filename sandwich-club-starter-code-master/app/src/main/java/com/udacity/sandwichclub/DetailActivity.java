package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView name = findViewById(R.id.name_tv);
        TextView alsoKnownAs = findViewById(R.id.also_known_tv);
        TextView origin = findViewById(R.id.origin_tv);
        TextView description = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);

        name.setText(handleMissing(sandwich.getMainName()));
        origin.setText(handleMissing(sandwich.getPlaceOfOrigin()));
        description.setText(handleMissing(sandwich.getDescription()));

        List<String> akaList = sandwich.getAlsoKnownAs();
        String fin = "";
        for (String s: akaList){
            fin += s + ", ";
        }
        if (fin.length() > 0){
            fin = fin.substring(0, fin.length() - 2);
        }
        alsoKnownAs.setText(handleMissing(fin));

        List<String> ingList = sandwich.getIngredients();
        fin = "";
        for (String s: ingList){
            fin += s + ", ";
        }
        if (fin.length() > 0){
            fin = fin.substring(0, fin.length() - 2);
        }
        ingredients.setText(handleMissing(fin));
    }
    private String handleMissing(String s){
        if (s.equals("")){
            return "[No data available]";
        } else{
            return s;
        }
    }
}

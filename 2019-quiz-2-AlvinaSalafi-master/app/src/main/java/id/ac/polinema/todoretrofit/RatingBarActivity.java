package id.ac.polinema.todoretrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import id.ac.polinema.todoretrofit.activities.LoginActivity;
import id.ac.polinema.todoretrofit.activities.SaveTodoActivity;

public class RatingBarActivity extends AppCompatActivity {

    private TextView getRating;
    private Button Submit;
    private AppCompatRatingBar RatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);

        getRating = findViewById(R.id.rate);
        Submit = findViewById(R.id.submit);
        RatingBar = findViewById(R.id.penilaian);

        //fungsi bertambahnya
        RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float nilai, boolean b) {

                getRating.setText("Rating: "+nilai);
            }
        });

        //submitnya
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Nilai Yang Anda Kirimkan: "+RatingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    //memanngil menu main
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SaveTodoActivity.class));
        } else if (item.getItemId() == R.id.searching) {
            startActivity(new Intent(this, NewActivity.class));
        } else if (item.getItemId() == R.id.ratingbar) {
            startActivity(new Intent(this, RatingBarActivity.class));
        }
        if (id == R.id.logout) {
            logout();
        }
        return true;
    }

    private void logout(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

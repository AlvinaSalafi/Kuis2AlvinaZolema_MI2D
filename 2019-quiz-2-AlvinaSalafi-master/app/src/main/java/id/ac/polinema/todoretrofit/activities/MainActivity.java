package id.ac.polinema.todoretrofit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.ac.polinema.todoretrofit.Application;
import id.ac.polinema.todoretrofit.Constant;
import id.ac.polinema.todoretrofit.NewActivity;
import id.ac.polinema.todoretrofit.R;
import id.ac.polinema.todoretrofit.RatingBarActivity;
import id.ac.polinema.todoretrofit.Session;
import id.ac.polinema.todoretrofit.adapters.TodoAdapter;
import id.ac.polinema.todoretrofit.generator.ServiceGenerator;
import id.ac.polinema.todoretrofit.models.Envelope;
import id.ac.polinema.todoretrofit.models.Todo;
import id.ac.polinema.todoretrofit.models.User;
import id.ac.polinema.todoretrofit.services.AuthService;
import id.ac.polinema.todoretrofit.services.TodoService;
import id.ac.polinema.todoretrofit.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoClickedListener {

    private RecyclerView todosRecyclerView;
    private Session session;
    private TodoService service;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveTodoActivity.class);
                intent.putExtra(Constant.KEY_REQUEST_CODE, Constant.ADD_TODO);
                startActivityForResult(intent, Constant.ADD_TODO);
            }
        });
        session = Application.provideSession();
        if (!session.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        todosRecyclerView = findViewById(R.id.rv_todos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        todosRecyclerView.setLayoutManager(layoutManager);
        adapter = new TodoAdapter(this, this);
        todosRecyclerView.setAdapter(adapter);
        service = ServiceGenerator.createService(TodoService.class);
        loadTodos();
    }

    private void loadTodos() {
        Call<Envelope<List<Todo>>> todos = service.getTodos(null, 1, 10);
        todos.enqueue(new Callback<Envelope<List<Todo>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Todo>>> call, Response<Envelope<List<Todo>>> response) {
                if (response.code() == 200) {
                    Envelope<List<Todo>> okResponse = response.body();
                    List<Todo> items = okResponse.getData();
                    adapter.setItems(items);
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Todo>>> call, Throwable t) {

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

    @Override
    public void onClick(Todo todo) {
        Intent intent = new Intent(this, SaveTodoActivity.class);
        intent.putExtra(Constant.KEY_TODO, todo);
        intent.putExtra(Constant.KEY_REQUEST_CODE, Constant.UPDATE_TODO);
        startActivityForResult(intent, Constant.UPDATE_TODO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadTodos();
        }
    }

//tambahan
@Override
public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()){
        case 121:
            removeTodo(item.getGroupId());
//                Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
            return true;
    }


    return super.onContextItemSelected(item);
}
//--untuk menghapus--
    public void removeTodo(int position){
        List<Todo> items = adapter.getItems();
        Todo item = items.get(position);
        Call<Envelope<Todo>> deleteTodo = service.deleteTodo(Integer.toString(item.getId()));
        deleteTodo.enqueue(new Callback<Envelope<Todo>>() {
            @Override
            public void onResponse(Call<Envelope<Todo>> call, Response<Envelope<Todo>> response) {
                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this,"Todo Deleted",Toast.LENGTH_SHORT).show();
                    loadTodos();
                }else{
                    Toast.makeText(MainActivity.this,response.toString(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Envelope<Todo>> call, Throwable t) {

            }
        });

    }
}

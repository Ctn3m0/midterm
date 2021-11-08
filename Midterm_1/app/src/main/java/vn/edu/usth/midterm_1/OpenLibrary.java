package vn.edu.usth.midterm_1;

import vn.edu.usth.midterm_1.models.Book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OpenLibrary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_BOOK = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_SUBJECTS = 2;


    private int mCurrentFragment = FRAGMENT_BOOK;
    private ImageView menuImage;
    private ImageView searchImage;
    private ImageView logoImage;


    private vn.edu.usth.booksearch.net.BookClient client;
    private boolean search;
    private ImageButton Search;
    private EditText input;

    private ActionBarDrawerToggle abdt;

    public void fetchBooks(String query, boolean search) {
        this.search = search;
        client = new vn.edu.usth.booksearch.net.BookClient();
        ListView listView = (ListView) findViewById(R.id.search_items);

        client.getBooks(query, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONObject response) {
                Log.v("fetch", query);
                try {
                    JSONArray docs = null;
                    if(response != null) {
                        docs = response.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<vn.edu.usth.midterm_1.models.Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        Log.v("Testing", "Hello");

//                        List<Book> books = new List<>();

                        ArrayList<String> books_info = new ArrayList<>();

                        ArrayList<String> books_names = new ArrayList<>();
                        ArrayList<String> books_authors = new ArrayList<>();
                        ArrayList<Integer> books_covers = new ArrayList<>();

                        for (vn.edu.usth.midterm_1.models.Book book : books) {
                            books_info.add(book.getTitle()+" \n"+book.getAuthor() + " \n" + book.getPublisher());
                            books_names.add(book.getTitle());
                            books_authors.add(book.getAuthor());
//                            books_covers.add(book.getCoverUrl());
                            Log.i("Data",book.getTitle());
                            Log.i("Image",book.getCoverUrl());

                        }

                        TextView headerTextView = new TextView(vn.edu.usth.midterm_1.OpenLibrary.this);
                        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
                        headerTextView.setText("List of Books");

                        listView.addHeaderView(headerTextView);

//                        CustomBookList customBookList = new CustomBookList(MainActivity.this, books_names, books_authors, books_covers);

                        ArrayAdapter arrayAdapter = new ArrayAdapter(vn.edu.usth.midterm_1.OpenLibrary.this, android.R.layout.simple_list_item_1, books_info);

                        listView.setAdapter(arrayAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR",e.toString());
                }
            }
            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, String responseString, Throwable throwable) {
                Log.v("BIG PROBLEM", "FAIL");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        ArrayList<vn.edu.usth.midterm_1.models.Book> aBooks = new ArrayList<vn.edu.usth.midterm_1.models.Book>();


        search = false;

        input = (EditText) findViewById(R.id.search_input);
        Search = (ImageButton) findViewById(R.id.search_button);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchBooks(input.getText().toString(), search);
                Toast.makeText(vn.edu.usth.midterm_1.OpenLibrary.this, input.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });





        abdt = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout.addDrawerListener(abdt);
        abdt.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new BookFragment());
        setTitle("Open Library");

//        menuImage = (ImageView) findViewById(R.id.menu_toolbar);
//        menuImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
//                    mDrawerLayout.closeDrawer(GravityCompat.END);
//                } else {
//                    mDrawerLayout.openDrawer(GravityCompat.END);
//                }
//            }
//        });

        logoImage = (ImageView) findViewById(R.id.logo);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentFragment != FRAGMENT_BOOK) {
                    replaceFragment(new BookFragment());
                    mCurrentFragment = FRAGMENT_BOOK;
                }
            }
        });

//        searchImage = (ImageView) findViewById(R.id.menu_search);
//        searchImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mCurrentFragment != FRAGMENT_SEARCH){
//                    replaceFragment(new SearchFragment());
//                    setTitle("Search");
//                    mCurrentFragment = FRAGMENT_SEARCH;
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                if (mCurrentFragment != FRAGMENT_SEARCH) {
                    replaceFragment(new SearchFragment());
                    setTitle("Search");
                    mCurrentFragment = FRAGMENT_SEARCH;
                }
                return true;
            case R.id.nav_menu:
                mDrawerLayout.openDrawer(GravityCompat.END);
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.author){
            Toast.makeText(OpenLibrary.this, "Get to author page", Toast.LENGTH_LONG).show();
        } else if(id == R.id.subjects){
            if (mCurrentFragment != FRAGMENT_SUBJECTS){
                replaceFragment(new SubjectsFragment());
                mCurrentFragment = FRAGMENT_SUBJECTS;
                setTitle("SUBJECTS");
            }
        }
        else if (id == R.id.my_books) {
            Intent intent = new Intent(OpenLibrary.this, MyBookActivity.class);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }else if (mCurrentFragment != FRAGMENT_BOOK) {
            setTitle("Open Library");
            replaceFragment(new BookFragment());
            mCurrentFragment = FRAGMENT_BOOK;
        }else {
            super.onBackPressed();
        }
    }

    private void changeActivity() {
        Intent intent = new Intent(OpenLibrary.this, SearchFragment.class);
        startActivity(intent);
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}
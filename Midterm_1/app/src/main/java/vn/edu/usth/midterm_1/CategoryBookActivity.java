package vn.edu.usth.midterm_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;

import vn.edu.usth.midterm_1.net.BookClient;
import vn.edu.usth.midterm_1.models.Book;
import vn.edu.usth.midterm_1.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class CategoryBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    BufferedReader reader;

    private static final int FRAGMENT_BOOK = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_SUBJECTS = 2;
    private static final int FRAGMENT_MY_BOOK = 4;

    private BookClient client;



    private int mCurrentFragment = FRAGMENT_MY_BOOK;
    private ImageView menuImage;
    private ImageView searchImage;
    private ImageView logoImage;
    private boolean search;
    private String passed_cate;

    ArrayList<String> mTitle = new ArrayList<String>();
    ArrayList<String> mAuthor = new ArrayList<String>();
    ArrayList<String> mUrls = new ArrayList<String>();
    ArrayList<BitmapDrawable> images = new ArrayList<BitmapDrawable>();

    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_book);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);


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


        logoImage = (ImageView) findViewById(R.id.logo);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryBookActivity.this, OpenLibrary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        search = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            passed_cate = extras.getString("temp");
        }
        fetchCategory(passed_cate, search);
//        Log.i("Pass", passed_cate);

    }

    public void fetchCategory(String query, boolean search) {
        this.search = search;
        client = new BookClient();
        ListView listView2 = (ListView) findViewById(R.id.category_book_items);

        if(listView2 == null){
            Log.i("LISTVIEW", "is null");
        }

        client.getCategory(query, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("fetch", query);
                try {
                    JSONArray works = null;
                    if(response != null) {
                        works = response.getJSONArray("works");
                        // Parse json array into array of model objects
                        final ArrayList<Category> _Cate = Category.fromJson(works);
                        // Remove all books from the adapter
                        Log.v("Testing", "Work");

                        for (Category cate : _Cate) {
                            mTitle.add(cate.getTitle());
                            Log.i("TITLE", cate.getTitle());
                            mAuthor.add(cate.getAuthor());
                            Log.i("AUTHOR", cate.getAuthor());
                            mUrls.add(cate.getCoverUrl());
                        }

                        String[] aTitle = new String[mTitle.size()];
                        String[] aAuthor = new String[mAuthor.size()];
                        BitmapDrawable[] aImage = new BitmapDrawable[images.size()];

                        aTitle = mTitle.toArray(aTitle);
                        aAuthor = mAuthor.toArray(aAuthor);
                        aImage = images.toArray(aImage);

                        if(listView2 == null){
                            Log.i("Listview", "is null");
                        }
                        MyAdapter adapter = new MyAdapter(CategoryBookActivity.this, aTitle, aAuthor);//, images);
                        listView2.setAdapter(adapter);

                        TextView headerTextView = new TextView(CategoryBookActivity.this);
                        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
                        headerTextView.setText("List of Books");
                        headerTextView.setTextSize(18);
                        headerTextView.setPadding(10, 20, 0, 20);

                        listView2.addHeaderView(headerTextView);

                        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.i("CLICKED", "Item " + i);
                                TextView _author = (TextView)adapterView.findViewById(R.id.textView2);
                                TextView _title = (TextView)adapterView.findViewById(R.id.textView1);

                                Log.i("Author", String.valueOf(_author.getText()));
                                Intent temp_item = new Intent(CategoryBookActivity.this, ViewBookActivity.class);
                                temp_item.putExtra("author", mAuthor.get(i-1));
                                temp_item.putExtra("title", mTitle.get(i-1));
                                temp_item.putExtra("urlCover", mUrls.get(i-1));
                                startActivity(temp_item);
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR",e.toString());
                }
            }
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("BIG PROBLEM", "FAIL");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        else if(id == R.id.subjects){
//            if (mCurrentFragment != FRAGMENT_SUBJECTS){
//                replaceFragment(new SubjectsFragment());
//                mCurrentFragment = FRAGMENT_SUBJECTS;
//                setTitle("SUBJECTS");
//            }
//        }
        if (id == R.id.subjects) {
            Intent intent = new Intent(CategoryBookActivity.this, SubjectActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.my_books) {
            Intent intent = new Intent(CategoryBookActivity.this, MyBookActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.favorites) {
//            Intent intent = new Intent(CategoryBookActivity.this, FavoriteActivity.class);
//            startActivity(intent);
//        }
        else if (id == R.id.my_account) {
            Intent intent = new Intent(CategoryBookActivity.this, MyAccountActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.log_out) {
            Intent intent = new Intent(CategoryBookActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] rTitle;
        String[] rAuthor;
//        BitmapDrawable rImgs[];

        MyAdapter (Context c, String title[], String author[]){// ArrayList<BitmapDrawable> imgs) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rAuthor = author;
//            this.rImgs = Images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myAuthor = row.findViewById(R.id.textView2);

            // now set our resources on views
//            images.setImageDrawable(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myAuthor.setText(rAuthor[position]);

            return row;
        }
    }

}
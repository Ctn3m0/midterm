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

public class SubjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    ArrayList<String> mTitle = new ArrayList<String>();
    ArrayList<BitmapDrawable> images = new ArrayList<BitmapDrawable>();

    private ActionBarDrawerToggle abdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

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
                Intent intent = new Intent(SubjectActivity.this, OpenLibrary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        List<String> _available_cate = new ArrayList<String>();

        try{
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("subjects")));
            String line = reader.readLine();
            while(line != null){
                _available_cate.add(line);
                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }

        ArrayList<String> _nice_cate = new ArrayList<String>();

        for (String cate : _available_cate) {
            String new_cate = cate.replace("subjects/", "").replace("_", " ").replace("place%3A", "").replace("  ", " ");
            String capitalized_cate = new_cate.substring(0, 1).toUpperCase() + new_cate.substring(1);
            _nice_cate.add(capitalized_cate);
        }

        String[] categoryList = new String[_nice_cate.size()];
        categoryList = _nice_cate.toArray(categoryList);


        search = false;
        Log.v("AVAILABLE_CATE", _available_cate.toString());
        Log.v("AVAILABLE_CATE_SIZE", String.valueOf(_available_cate.size()));

        for (int i = 0; i < _available_cate.size(); i++) {
            Log.v("READ FROM FILE ",_available_cate.get(i));
//            fetchCategory(_available_cate.get(i) , search);

        }

        client = new BookClient();
        ListView listView = (ListView) findViewById(R.id.category_items);

        ArrayAdapter adapter = new ArrayAdapter(SubjectActivity.this, android.R.layout.simple_list_item_1, categoryList);//, images);
        listView.setAdapter(adapter);

        TextView headerTextView = new TextView(SubjectActivity.this);
        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
        headerTextView.setText("List of Categories");
        headerTextView.setTextSize(18);
        headerTextView.setPadding(10, 20, 0, 20);

        listView.addHeaderView(headerTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("CLICKED", "Item " + i);
                Intent temp_item_index = new Intent(SubjectActivity.this, CategoryBookActivity.class);
                temp_item_index.putExtra("temp", _available_cate.get(i-1));
                startActivity(temp_item_index);
            }
        });
    }

    public void fetchCategory(String query, boolean search) {
        this.search = search;
//        client = new BookClient();
//        ListView listView = (ListView) findViewById(R.id.category_items);

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
                            Log.i("Category",cate.getCoverUrl());
                        }

                        Log.i("Data",mTitle.toString());
                        String[] aTitle = new String[mTitle.size()];
                        BitmapDrawable[] aImage = new BitmapDrawable[images.size()];

                        aTitle = mTitle.toArray(aTitle);
                        for (String title : aTitle) {
                            Log.i("TITLE", "bro lmao");
                        }

                        aImage = images.toArray(aImage);
                        Log.i("Image", aImage.toString());

//                        SubjectActivity.MyAdapter adapter = new SubjectActivity.MyAdapter(SubjectActivity.this, aTitle);//, images);
//                        listView.setAdapter(adapter);

//                        TextView headerTextView = new TextView(SubjectActivity.this);
//                        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
//                        headerTextView.setText("List of Categories");

//                        listView.addHeaderView(headerTextView);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
                Intent intent = new Intent(SubjectActivity.this, SearchActivity.class);
                startActivity(intent);
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
//        else if(id == R.id.subjects){
//            if (mCurrentFragment != FRAGMENT_SUBJECTS){
//                replaceFragment(new SubjectsFragment());
//                mCurrentFragment = FRAGMENT_SUBJECTS;
//                setTitle("SUBJECTS");
//            }
//        }
        if (id == R.id.subjects) {
            Intent intent = new Intent(SubjectActivity.this, SubjectActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.my_books) {
            Intent intent = new Intent(SubjectActivity.this, MyBookActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.favorites) {
//            Intent intent = new Intent(SubjectActivity.this, FavoriteActivity.class);
//            startActivity(intent);
//        }
//        else if (id == R.id.my_account) {
//            Intent intent = new Intent(SubjectActivity.this, MyAccountActivity.class);
//            startActivity(intent);
//        }

        else if (id == R.id.log_out) {
            Intent intent = new Intent(SubjectActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
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
        String[] cate;
//        BitmapDrawable rImgs[];

        MyAdapter (Context c, String[] cate){// ArrayList<BitmapDrawable> imgs) {
            super(c, R.layout.row, R.id.textView1, cate);
            this.context = c;
            this.cate = cate;
//            this.rImgs = Images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView category = row.findViewById(R.id.textView1);

            // now set our resources on views
//            images.setImageDrawable(rImgs[position]);
            category.setText(cate[position]);

            return row;
        }
    }


}
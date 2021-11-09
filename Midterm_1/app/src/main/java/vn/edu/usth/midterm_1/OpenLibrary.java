package vn.edu.usth.midterm_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import vn.edu.usth.midterm_1.models.Book;
import vn.edu.usth.midterm_1.models.Category;
import vn.edu.usth.midterm_1.net.BookClient;

public class OpenLibrary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_BOOK = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_SUBJECTS = 2;


    private int mCurrentFragment = FRAGMENT_BOOK;
    private ImageView menuImage;
    private ImageView searchImage;
    private ImageView logoImage;
    private boolean search;
    private BookClient client;

    private ActionBarDrawerToggle abdt;

    ArrayList<String> mTitle = new ArrayList<String>();
    ArrayList<String> mAuthor = new ArrayList<String>();
    ArrayList<String> mPublisher = new ArrayList<String>();
    ArrayList<String> mUrls = new ArrayList<String>();
    ArrayList<BitmapDrawable> images = new ArrayList<BitmapDrawable>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(OpenLibrary.this, OpenLibrary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
//        View v = findViewById(R.id.classic_book1);
//        TextView t = v.findViewById(R.id.home_author1);
//        t.setText("Test");
        fetchBooks("Harry", search);
    }

    public void fetchBooks(String query, boolean search) {
        Bitmap book2 = BitmapFactory.decodeResource(getResources(),R.drawable.book2);
        this.search = search;
        client = new BookClient();
        ListView listView = (ListView) findViewById(R.id.popular);
        ListView classicListView = (ListView) findViewById(R.id.classicz);

        client.getBooks(query, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("fetch", query);
                try {
                    JSONArray docs = null;
                    if(response != null) {
                        docs = response.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        Log.v("Testing", "Hello");

//                        List<Book> books = new List<>();

                        ArrayList<String> books_info = new ArrayList<>();

                        ArrayList<String> books_names = new ArrayList<>();
                        ArrayList<String> books_authors = new ArrayList<>();
                        ArrayList<Integer> books_covers = new ArrayList<>();

                        for (Book book : books) {
                            mTitle.add(book.getTitle());
                            Log.i("TITLE", book.getTitle());
//                            mTitle.add("books.get(i).getTitle()");
//                            mAuthor.add("books.get(i).getAuthor()");
                            mAuthor.add(book.getAuthor());
                            mPublisher.add(book.getPublisher());
                            mUrls.add(book.getCoverUrl());
//                            Log.i("Data",book.getTitle());
//                            books_covers.add(book.getCoverUrl());
                            images.add(new BitmapDrawable(book2));

                        }

                        Log.i("Data",mTitle.toString());
                        Log.i("Data",mAuthor.toString());
                        Log.i("Data",images.toString());

                        String[] aTitle = new String[mTitle.size()];
                        String[] aAuthor = new String[mAuthor.size()];
                        BitmapDrawable[] aImage = new BitmapDrawable[images.size()];

                        aTitle = mTitle.toArray(aTitle);
                        aAuthor = mAuthor.toArray(aAuthor);
                        aImage = images.toArray(aImage);
                        Log.i("Image", aImage.toString());
                        Log.i("Test Publisher", mPublisher.toString());

                        MyAdapter adapter = new MyAdapter(OpenLibrary.this,
                                Arrays.copyOfRange(aTitle, 0, 8),
                                Arrays.copyOfRange(aAuthor, 0, 8),
                                Arrays.copyOfRange(aImage, 0, 8));//, images);
                        listView.setAdapter(adapter);

                        setDynamicHeight(listView);

                        Log.i("publisher", mPublisher.toString());

                        TextView headerTextView = new TextView(OpenLibrary.this);
                        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
                        headerTextView.setText("Most loved books");
                        headerTextView.setTextSize(24);
                        headerTextView.setPadding(20, 30, 0, 30);

                        listView.addHeaderView(headerTextView);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.i("CLICKED", "Item " + i);

                                Intent temp_item = new Intent(OpenLibrary.this, ViewBookActivity.class);
                                temp_item.putExtra("author", mAuthor.get(i-1));
                                temp_item.putExtra("title", mTitle.get(i-1));
                                temp_item.putExtra("publisher", mPublisher.get(i-1));
                                Log.i("Publisher", mPublisher.get(i-1));
                                temp_item.putExtra("urlCover", mUrls.get(i-1));
                                startActivity(temp_item);
                            }
                        });

                        MyAdapter adapter1 = new MyAdapter(OpenLibrary.this,
                                Arrays.copyOfRange(aTitle,8,17),
                                Arrays.copyOfRange(aAuthor,8,17),
                                Arrays.copyOfRange(aImage,8,17));
                        classicListView.setAdapter(adapter1);

                        setDynamicHeight(classicListView);

                        TextView headerTextViewClassic = new TextView(OpenLibrary.this);
                        headerTextViewClassic.setTypeface(Typeface.DEFAULT_BOLD);
                        headerTextViewClassic.setText("Classic books");
                        headerTextViewClassic.setTextSize(24);
                        headerTextViewClassic.setPadding(20, 30, 0, 30);

                        classicListView.addHeaderView(headerTextViewClassic);

                        classicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.i("CLICKED", "Item " + i);

                                Intent temp_item = new Intent(OpenLibrary.this, ViewBookActivity.class);
                                temp_item.putExtra("author", mAuthor.get(i-1+8));
                                temp_item.putExtra("title", mTitle.get(i-1+8));
                                temp_item.putExtra("publisher", mPublisher.get(i-1+8));
                                Log.i("Publisher", mPublisher.get(i-1+8));
                                temp_item.putExtra("urlCover", mUrls.get(i-1+8));
                                startActivity(temp_item);
                            }
                        });

//                        CustomBookList customBookList = new CustomBookList(MainActivity.this, books_names, books_authors, books_covers);
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
//                if (mCurrentFragment != FRAGMENT_SEARCH) {
//                    replaceFragment(new SearchFragment());
//                    setTitle("Search");
//                    mCurrentFragment = FRAGMENT_SEARCH;
//                }
                Intent intent = new Intent(OpenLibrary.this, SearchActivity.class);
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
            Intent intent = new Intent(OpenLibrary.this, SubjectActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.my_books) {
            Intent intent = new Intent(OpenLibrary.this, MyBookActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.favorites) {
//            Intent intent = new Intent(OpenLibrary.this, FavoriteActivity.class);
//            startActivity(intent);
//        }
        else if (id == R.id.my_account) {
            Intent intent = new Intent(OpenLibrary.this, MyAccountActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.log_out) {
            Intent intent = new Intent(OpenLibrary.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
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
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rAuthor[];
        BitmapDrawable rImgs[];

        MyAdapter (Context c, String title[], String author[], BitmapDrawable Images[]){// ArrayList<BitmapDrawable> imgs) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rAuthor = author;
            this.rImgs = Images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_home, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myAuthor = row.findViewById(R.id.textView2);

            // now set our resources on views
            images.setImageDrawable(rImgs[position]);
//            images.setTag("ImageSearch"+position);
//            images.setId(position);
            myTitle.setText(rTitle[position]);
            myAuthor.setText(rAuthor[position]);

            return row;
        }


    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }
}
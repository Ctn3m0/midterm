package vn.edu.usth.midterm_1;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.edu.usth.midterm_1.net.BookClient;
import vn.edu.usth.midterm_1.models.Book;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.nfc.Tag;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    SearchView mySearchView;
    ListView myList;
    private ImageView mImageViewResult;

    private BookClient client;
    private ProgressBar progress;
    private ArrayList<Book> aBooks;
    private boolean search;
    private ImageButton Search;
    private EditText input;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ArrayList<Book> aBooks = new ArrayList<Book>();

        search = false;


        input = (EditText) findViewById(R.id.search_input);
        Search = (ImageButton) findViewById(R.id.search_buttonnn);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchBooks(input.getText().toString(), search);
                Toast.makeText(SearchActivity.this, input.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void fetchBooks(String query, boolean search) {
        this.search = search;
        client = new BookClient();
        ListView listView = (ListView) findViewById(R.id.search_items);

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
                            books_info.add(book.getTitle()+" \n"+book.getAuthor() + " \n" + book.getPublisher());
                            books_names.add(book.getTitle());
                            books_authors.add(book.getAuthor());
//                            books_covers.add(book.getCoverUrl());
                            Log.i("Data",book.getTitle());
                            Log.i("Image",book.getCoverUrl());

                        }

                        TextView headerTextView = new TextView(SearchActivity.this);
                        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
                        headerTextView.setText("List of Books");

                        listView.addHeaderView(headerTextView);

//                        CustomBookList customBookList = new CustomBookList(MainActivity.this, books_names, books_authors, books_covers);

                        ArrayAdapter arrayAdapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, books_info);

                        listView.setAdapter(arrayAdapter);
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


    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
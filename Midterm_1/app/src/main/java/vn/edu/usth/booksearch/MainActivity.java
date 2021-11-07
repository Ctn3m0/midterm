package vn.edu.usth.booksearch;

import androidx.appcompat.app.AppCompatActivity;
import vn.edu.usth.booksearch.net.BookClient;
import vn.edu.usth.booksearch.models.Book;
import vn.edu.usth.booksearch.models.Category;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.nfc.Tag;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SearchView mySearchView;
    ListView myList;
    BufferedReader reader;

    private TextView mTextViewResult;
    private BookClient client;
    private ProgressBar progress;
    private ArrayList<Book> aBooks;
    private ArrayList<Category> _Cate;
    private boolean search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Book> aBooks = new ArrayList<Book>();
        ArrayList<Category> _Cate = new ArrayList<Category>();
        mTextViewResult = findViewById(R.id.text_view_result);
        mTextViewResult.setText("123213");

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

//      Read from File

        for (int i = 0; i < _available_cate.size(); i++) {
            Log.v("READ FROM FILE ",_available_cate.get(i));
        }

//      API TESTING
        search = false;
//        fetchBooks("Harry Potter", search);
//        fetchCategory("architecture", search);
    }

    public void fetchBooks(String query, boolean search) {
        this.search = search;
        client = new BookClient();

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
                        for (Book book : books) {
                            Log.i("Data",book.getTitle());
                            mTextViewResult.setText(book.toString());
                        }
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

    public void fetchCategory(String query, boolean search) {
        this.search = search;
        client = new BookClient();

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
                            Log.i("Category",cate.getCoverUrl());
                        }
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

}
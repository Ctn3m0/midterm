package vn.edu.usth.booksearch;

import androidx.appcompat.app.AppCompatActivity;
import vn.edu.usth.booksearch.net.BookClient;
import vn.edu.usth.booksearch.models.Book;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;
import android.nfc.Tag;
import android.widget.ProgressBar;

import java.io.IOException;
import org.apache.http.Header;
import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    SearchView mySearchView;
    ListView myList;
    private TextView mTextViewResult;

    private String query;
    private Integer _limit;

    private BookClient client;
    private ProgressBar progress;
    private ArrayList<Book> aBooks;
    private boolean search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Book> aBooks = new ArrayList<Book>();
        mTextViewResult = findViewById(R.id.text_view_result);
        mTextViewResult.setText("123213");

        search = false;
        fetchBooks("Oscar Wilde", search);

//        this.search = search;
        // Show progress bar before making network request
//        if(search)
//            progress.setVisibility(ProgressBar.VISIBLE);
//        client = new BookClient();
        /*if (query == null) {
        } else {
            query = "oscar Wilde";
        }*/

//        query = "oscar Wilde";
//        Log.v("Testing", "Hello 1");
//        client.getBooks(query, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Log.v("Inside client", "Hello there");
//                try {
//                    progress.setVisibility(ProgressBar.GONE);
//                    JSONArray docs = null;
//                    Log.v("Testing", "Hello 1");
//                    if(response != null) {
//                        // Get the docs json array
//                        JSONObject docs = new JSONObject(response);
////                        docs = response.getJSONArray("docs");
//                        final ArrayList<Book> books = Book.fromJson(docs);
//                        mTextViewResult.setText("123213");
//                        Log.v("Testing", "Hello");
//                        for (Book book : books) {
//                            Log.i("Data",book.toString());
//                            mTextViewResult.setText(book.toString());
//                        }
////
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.v("ERROR",e.toString());
//                }
//            }

//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                progress.setVisibility(ProgressBar.GONE);
//            }
//        });
//
    }

    private void fetchBooks(String query, boolean search) {
        // Show progress bar before making network request
//        if(search)
//            progress.setVisibility(ProgressBar.VISIBLE);
//        client = new BookClient();
        /*if (query == null) {
        } else {
            query = "oscar Wilde";
        }*/

        client = new BookClient();

        client.getBooks(query, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("fetch", query);
                try {
                    // hide progress bar
                    progress.setVisibility(ProgressBar.GONE);
//                    JSONArray docs = null;
                    if(response != null) {
                        // Get the docs json array
                        JSONArray docs = response.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        Log.v("Testing", "Hello");
                        for (Book book : books) {
                            Log.i("Data",book.toString());
                            mTextViewResult.setText(book.toString());
                        }
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(ProgressBar.GONE);
            }
        });
    }

}
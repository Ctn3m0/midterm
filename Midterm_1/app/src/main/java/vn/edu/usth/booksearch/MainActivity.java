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

import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SearchView mySearchView;
    ListView myList;
    private TextView mTextViewResult;

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
        fetchBooks("Harry Potter", search);
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
                        Log.v("Testing", "Hello");
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

}
package vn.edu.usth.midterm_1;

import static android.widget.Toast.LENGTH_LONG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.edu.usth.midterm_1.net.BookClient;
import vn.edu.usth.midterm_1.models.Book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    BufferedReader reader;

    private BookClient client;
    private ProgressBar progress;
    private ArrayList<Book> aBooks;
    private boolean search;
    private ImageButton Search;
    private EditText input;

    URL url;
    Bitmap bitmap;

    ListView listView;
    ArrayList<String> mTitle = new ArrayList<String>();
    ArrayList<String> mAuthor = new ArrayList<String>();
    ArrayList<String> mPublisher = new ArrayList<String>();
    ArrayList<String> mUrls = new ArrayList<String>();
    ArrayList<BitmapDrawable> images = new ArrayList<BitmapDrawable>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ArrayList<Book> aBooks = new ArrayList<Book>();

        search = false;

        List<String> _available_cate = new ArrayList<String>();

        //      Read from File

        for (int i = 0; i < _available_cate.size(); i++) {
            Log.v("READ FROM FILE ",_available_cate.get(i));
        }

//      API TESTING
        search = false;
//        fetchBooks("Harry Potter", search);
//        fetchCategory("subjects/architecture", search);
        input = (EditText) findViewById(R.id.search_input);
        Search = (ImageButton) findViewById(R.id.search_button);

//        try{
//            reader = new BufferedReader(
//                    new InputStreamReader(getAssets().open("subjects")));
//            String line = reader.readLine();
//            while(line != null){
//                _available_cate.add(line);
//                line = reader.readLine();
//            }
//        } catch(IOException ioe){
//            ioe.printStackTrace();
//        }


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
                            mTitle.add(book.getTitle());
//                            mTitle.add("book.getTitle()");
//                            mAuthor.add("book.getAuthor()");
                            mAuthor.add(book.getAuthor());
                            mPublisher.add(book.getPublisher());
                            mUrls.add(book.getCoverUrl());
//                            Log.i("Data",book.getTitle());
                            books_info.add(book.getTitle()+" \n"+book.getAuthor() + " \n" + book.getPublisher());
                            books_names.add(book.getTitle());
                            books_authors.add(book.getAuthor());
//                            books_covers.add(book.getCoverUrl());

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

                        MyAdapter adapter = new MyAdapter(SearchActivity.this, aTitle, aAuthor);//, images);
                        listView.setAdapter(adapter);

                        Log.i("publisher", mPublisher.toString());

                        TextView headerTextView = new TextView(SearchActivity.this);
                        headerTextView.setTypeface(Typeface.DEFAULT_BOLD);
                        headerTextView.setText("List of Books");
                        headerTextView.setTextSize(18);
                        headerTextView.setPadding(10, 20, 0, 20);

                        listView.addHeaderView(headerTextView);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.i("CLICKED", "Item " + i);
                                TextView _author = (TextView)adapterView.findViewById(R.id.textView2);
                                TextView _title = (TextView)adapterView.findViewById(R.id.textView1);

                                Log.i("Author", String.valueOf(_author.getText()));
                                Intent temp_item = new Intent(SearchActivity.this, ViewBookActivity.class);
                                temp_item.putExtra("author", _author.getText());
                                temp_item.putExtra("title", _title.getText());
                                temp_item.putExtra("publisher", mPublisher.get(i-1));
                                temp_item.putExtra("urlCover", mUrls.get(i-1));
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


    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

        public String[] getrAuthor() {
            return rAuthor;
        }

        public String[] getrTitle() {
            return rTitle;
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
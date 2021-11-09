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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class ViewBookActivity extends AppCompatActivity {

    TextView tAuthor;
    TextView tTitle;
    TextView tPublisher;
    ImageView iImage;
    URL URL;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        Bundle extras = getIntent().getExtras();

        String author = "Author";
        String title = "Title";
        String publisher = "Publisher";
        String url = "";

        if (extras != null) {
            author = extras.getString("author");
            Log.i("author", author);
            title = extras.getString("title");
            Log.i("title", title);
            publisher = extras.getString("publisher");
            Log.i("publisher", publisher);
            url = extras.getString("urlCover");
            Log.i("urlCover", url);
        }

        tAuthor = findViewById(R.id.book_author);
        tTitle = findViewById(R.id.book_title);
        tPublisher = findViewById(R.id.book_publisher);
        iImage = findViewById(R.id.book_image);

        tAuthor.setText(author);
        tTitle.setText(title);
        tPublisher.setText("Publishers: " + publisher);
//        iImage.setImageDrawable(LoadImageFromWebOperations(url));

        String finalUrl = url;
        AsyncTask<String, Integer, Message> task = new AsyncTask<String, Integer, Message>() {
            @Override
            protected void onPreExecute() {
                // do some preparation here, if needed
            }
            @Override
            protected Message doInBackground(String... params) {
                try {
                    // wait for 5 seconds to simulate a long network access
//                    Thread.sleep(5000);
//                  initialize URL
                    URL = new URL(finalUrl);
                    // Make a request to server
                    HttpURLConnection connection =
                            (HttpURLConnection) URL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    // allow reading response code and response dataconnection.
                    connection.connect();
                    // Receive response
                    int response = connection.getResponseCode();
                    Log.i("CRAWL IMAGE", "The response is: " + response);
                    InputStream is = connection.getInputStream();
                    // Process image response
                    bitmap = BitmapFactory.decodeStream(is);
                    connection.disconnect();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                // Assume that we got our data from server
                Bundle bundle = new Bundle();
                bundle.putString("server_response", "some sample json here");
                // notify main thread
                Message msg = new Message();
                msg.setData(bundle);
                return msg;
            }
            @Override
            protected void onProgressUpdate(Integer... values) {
                // This method is called in the main thread, so it's possible
                // to update UI to reflect the worker thread progress here.
                // In a network access task, this should update a progress bar
                // to reflect how many percent of data has been retrieved
//                logo = (ImageView) findViewById(R.id.logo);
//                logo.setImageBitmap(bitmap);

            }
            @Override
            protected void onPostExecute(Message msg) {
//                test.setImageDrawable(new BitmapDrawable(bitmap));
                if(bitmap == null){
//                    test.setImageResource(R.drawable.cloud);
                    iImage.setImageResource(R.drawable.err);
                    Log.i("Image", "okay set here");
                } else {
                    iImage.setImageDrawable(new BitmapDrawable(bitmap));
                }
//                iImage.setImageDrawable(LoadImageFromWebOperations(finalUrl));
//                                    test.setImageDrawable(new BitmapDrawable(bitmap));
                String content = msg.getData().getString("server_response");
                Toast.makeText(ViewBookActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();

    }
    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            Log.i("SUCCESS", "get the image");
            return d;
        } catch (Exception e) {
            Log.i("FAILLLL", e.toString());
            return null;
        }
    }
}
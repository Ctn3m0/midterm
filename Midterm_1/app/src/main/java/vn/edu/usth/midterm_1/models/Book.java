package vn.edu.usth.midterm_1.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Book implements Parcelable {
    private String openLibraryId;
    private String author;
    private String title;
    private String publisher;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverUrl() {
        return "https://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }

    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
//        Log.i("JSON Object",jsonObject);
        try {
            if (jsonObject.has("cover_edition_key")) {
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            } else if(jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0);
            }
            book.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            book.author = getAuthor(jsonObject);
            book.publisher = getPublisher(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    private static String getPublisher(final JSONObject jsonObject) {
        try {
            final JSONArray pubs = jsonObject.getJSONArray("publisher");
            int numPubs = pubs.length();
            final String[] pubStrings = new String[numPubs];
            for (int i = 0; i < numPubs; ++i) {
                pubStrings[i] = pubs.getString(i);
            }
            return TextUtils.join(", ", pubStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
                Log.i("JSON Object",bookJson.toString());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

    public Book() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.openLibraryId);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeString(this.publisher);
    }

    private Book(Parcel in) {
        this.openLibraryId = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.publisher = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}

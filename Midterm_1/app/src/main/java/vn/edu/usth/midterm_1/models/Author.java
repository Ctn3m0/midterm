package vn.edu.usth.midterm_1.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Author implements Parcelable {
    private String title;
    private String author;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


    public static Author fromJson(JSONObject jsonObject) {
        Author _cate = new Author();
        try {
            _cate.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            _cate.author = getAuthor(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return _cate;
    }

    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("authors");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                JSONObject _authours = authors.getJSONObject(i);

                Log.i("authorStrings",_authours.optString("name"));
                authorStrings[i] = _authours.optString("name");
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    public static ArrayList<Author> fromJson(JSONArray jsonArray) {
        ArrayList<Author> categorys = new ArrayList<Author>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject categoryJson = null;
            try {
                categoryJson = jsonArray.getJSONObject(i);
                Log.i("JSON Object",categoryJson.toString());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Author book = Author.fromJson(categoryJson);
            if (book != null) {
                categorys.add(book);
            }
        }
        return categorys;
    }

    public Author() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.title);
    }

    private Author(Parcel in) {
        this.author = in.readString();
        this.title = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
}

package com.example.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.example.moviedb.api.ApiClient;
import com.example.moviedb.api.ApiService;
import com.example.moviedb.constant.Constant;
import com.example.moviedb.models.ImageResponse;
import com.example.moviedb.models.ImageResult;
import com.example.moviedb.models.Result;
import com.example.moviedb.models.VideoResponse;
import com.example.moviedb.models.VideoResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.moviedb.constant.Constant.IMAGE_SIZE;
import static com.example.moviedb.constant.Constant.IMAGE_URL;

public class DbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "MovieDB";

    private static final String TABLE_USERS = "Users";
    private static final String USERS_COLUMN_USERNAME = "UserName";
    private static final String USERS_COLUMN_PASSWORD = "Password";
    private static final String USERS_COLUMN_PROFILE_PICTURE = "ProfilePicture";

    private static final String TABLE_FAVOURITES = "Favourites";
    private static final String FAVOURITES_COLUMN_ID = "ID";
    private static final String FAVOURITES_COLUMN_USERNAME = "UserName";
    private static final String FAVOURITES_COLUMN_MOVIE_ID = "MovieId";

    private static final String TABLE_MOVIES = "Movie";
    private static final String MOVIES_COLUMN_ID = "ID";
    private static final String MOVIES_COLUMN_TITLE = "Title";
    private static final String MOVIES_COLUMN_DATE = "Date";
    private static final String MOVIES_COLUMN_DESCRIPTION = "Description";
    private static final String MOVIES_COLUMN_POSTER = "Poster";

    private static final String TABLE_IMAGES = "Images";
    private static final String IMAGES_COLUMN_ID = "ID";
    private static final String IMAGES_COLUMN_MOVIE_ID = "MovieId";
    private static final String IMAGES_COLUMN_IMAGE_PATH = "ImagePath";

    private static final String TABLE_VIDEOS = "Videos";
    private static final String VIDEOS_COLUMN_MOVIE_ID = "MovieId";
    private static final String VIDEOS_COLUMN_VIDEO_PATH = "VideoPath";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                        USERS_COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                        USERS_COLUMN_PASSWORD + " TEXT, " +
                        USERS_COLUMN_PROFILE_PICTURE + " TEXT);"
        );
        db.execSQL("CREATE TABLE " + TABLE_FAVOURITES + " (" +
                FAVOURITES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FAVOURITES_COLUMN_USERNAME + " TEXT, " +
                FAVOURITES_COLUMN_MOVIE_ID + " INTEGER);"
        );
        db.execSQL("CREATE TABLE " + TABLE_MOVIES + " (" +
                MOVIES_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MOVIES_COLUMN_TITLE + " TEXT, " +
                MOVIES_COLUMN_DATE + " TEXT, " +
                MOVIES_COLUMN_DESCRIPTION + " TEXT, " +
                MOVIES_COLUMN_POSTER + " TEXT);"
        );
        db.execSQL("CREATE TABLE " + TABLE_IMAGES + " (" +
                IMAGES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMAGES_COLUMN_MOVIE_ID + " INTEGER, " +
                IMAGES_COLUMN_IMAGE_PATH + " TEXT);"
        );
        db.execSQL("CREATE TABLE " + TABLE_VIDEOS + " (" +
                VIDEOS_COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                VIDEOS_COLUMN_VIDEO_PATH + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    public boolean insertUser(String userName, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_USERNAME, userName);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        contentValues.put(USERS_COLUMN_PROFILE_PICTURE, "");

        long result = db.insert(TABLE_USERS,null, contentValues);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public boolean userExists(String userName){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select 1 from " + TABLE_USERS + " where " + USERS_COLUMN_USERNAME + " = \"" + userName + "\"",null);
        if(res.getCount()==0) {
            return false;
        }
        else {
            return true;
        }

    }

    public String getPassword(String userName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + USERS_COLUMN_PASSWORD +" from " + TABLE_USERS + " where " + USERS_COLUMN_USERNAME + " = \"" + userName + "\"",null);
        if(res.getCount()==0) {
            return "";
        }
        else {
            res.moveToNext();
            return res.getString(0);
        }
    }



    public boolean changePassword(String username, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_PASSWORD, newPassword);

        long result = db.update(TABLE_USERS, contentValues, USERS_COLUMN_USERNAME + " = \"" + username + "\"", null);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public  boolean saveProfilePicture(String username, Bitmap image){
        SQLiteDatabase db = this.getWritableDatabase();

        String imagePath = saveToInternalStorage(image, "profile.jpg");

        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_PROFILE_PICTURE, imagePath);

        long result = db.update(TABLE_USERS, contentValues, USERS_COLUMN_USERNAME + " = \"" + username + "\"", null);
        if (result == -1)
            return  false;
        else
            return true;
    }

    public String getProfilePicture(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + USERS_COLUMN_PROFILE_PICTURE +" from " + TABLE_USERS + " where " + USERS_COLUMN_USERNAME + " = \"" + username + "\"",null);
        if(res.getCount()==0) {
            //return Uri.parse("");
            return "";
        }
        else {
            res.moveToNext();
            //return Uri.parse(res.getString(0));
            return res.getString(0);
        }
    }

    public boolean saveMovie(String userName, final Result movie){
        final SQLiteDatabase db = this.getWritableDatabase();

        if(movieIsSaved(userName, movie.getId())){
            db.execSQL("delete from " + TABLE_FAVOURITES + " where " + FAVOURITES_COLUMN_USERNAME + " = \"" + userName + "\" and " + FAVOURITES_COLUMN_MOVIE_ID + " = " + movie.getId());

            if(!movieIsSaved(movie.getId())){
                deleteMovieDetails(movie.getId());
            }
            return true;
        }

        if(movieIsSaved(movie.getId())){
            //save only to favourites
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAVOURITES_COLUMN_USERNAME, userName);
            contentValues.put(FAVOURITES_COLUMN_MOVIE_ID, movie.getId());

            long result = db.insert(TABLE_FAVOURITES,null, contentValues);
            if (result == -1)
                return  false;
            else
                return true;
        }
        else {
            //save movie details as well
            String api_key = Constant.API_KEY;
            ApiService service = ApiClient.getInstance().getApiService();

            ContentValues contentValues = new ContentValues();
            contentValues.put(FAVOURITES_COLUMN_USERNAME, userName);
            contentValues.put(FAVOURITES_COLUMN_MOVIE_ID, movie.getId());
            long result1 = db.insert(TABLE_FAVOURITES,null, contentValues);

            contentValues = new ContentValues();
            contentValues.put(MOVIES_COLUMN_ID, movie.getId());
            contentValues.put(MOVIES_COLUMN_TITLE, movie.getTitle());
            contentValues.put(MOVIES_COLUMN_DATE, movie.getReleaseDate());
            contentValues.put(MOVIES_COLUMN_DESCRIPTION, movie.getOverview());
            contentValues.put(MOVIES_COLUMN_POSTER, movie.getBackdropPath());
            long result2 = db.insert(TABLE_MOVIES,null, contentValues);

            Call<ImageResponse> call_images = service.getImages(movie.getId(), api_key);
            call_images.enqueue(new Callback<ImageResponse>() {
                @Override
                public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                    final List<ImageResult> images = response.body().getBackdrops();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(ImageResult img : images){
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(IMAGES_COLUMN_MOVIE_ID, movie.getId());
                                String imageUrl = IMAGE_URL + IMAGE_SIZE + img.getFilePath();
                                Bitmap bitmap = null;
                                InputStream inputStream;
                                try {
                                    inputStream = new java.net.URL(imageUrl).openStream();
                                    bitmap = BitmapFactory.decodeStream(inputStream);
                                    inputStream.close();
                                    String imagePath = saveToInternalStorage(bitmap, img.getFilePath());
                                    contentValues.put(IMAGES_COLUMN_IMAGE_PATH, imagePath);
                                    long result3 = db.insert(TABLE_IMAGES,null, contentValues);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    thread.start();
                }
                @Override
                public void onFailure(Call<ImageResponse> call, Throwable t) {

                }
            });

            Call<VideoResponse> call = service.getVideos(movie.getId(), api_key);
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    List<VideoResult> videos = response.body().getResults();
                    for (VideoResult video: videos) {
                        if(video.getType().equals("Trailer")){
                            final String key = video.getKey();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String videoPath = saveVideoToInternalStorage(key);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(VIDEOS_COLUMN_MOVIE_ID, movie.getId());
                                    contentValues.put(VIDEOS_COLUMN_VIDEO_PATH, videoPath);
                                    long result4 = db.insert(TABLE_VIDEOS,null, contentValues);
                                }
                            });
                            thread.start();
                            break;
                        }
                    }
                }
                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {

                }
            });

            if(result1!=-1 && result2!=-1){
                return true;
            }
            else{
                return false;
            }
        }
    }

    private void deleteMovieDetails(int movieId){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ImageResult> images = getImages(movieId);
        for(ImageResult img: images){
            File file = new File(img.getFilePath());
            file.delete();
        }
        db.execSQL("delete from " + TABLE_MOVIES + " where " + MOVIES_COLUMN_ID + " = " + movieId);
        db.execSQL("delete from " + TABLE_IMAGES + " where " + IMAGES_COLUMN_MOVIE_ID + " = " + movieId);
        db.execSQL("delete from " + TABLE_VIDEOS + " where " + VIDEOS_COLUMN_MOVIE_ID + " = " + movieId);
    }

    private boolean movieIsSaved(int movieId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select 1 from " + TABLE_FAVOURITES + " where " + FAVOURITES_COLUMN_MOVIE_ID + " = " + movieId,null);
        if(res.getCount()==0) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean movieIsSaved(String userName, int movieId){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select 1 from " + TABLE_FAVOURITES + " where " + FAVOURITES_COLUMN_USERNAME + " = \"" + userName + "\" and " + FAVOURITES_COLUMN_MOVIE_ID + " = " + movieId,null);
        if(res.getCount()==0) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<Result> getFavourites(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Result> favourites = new ArrayList<>();
        Cursor res = db.rawQuery("select m." + MOVIES_COLUMN_ID +
                                          ", m." + MOVIES_COLUMN_TITLE +
                                          ", m." + MOVIES_COLUMN_DATE +
                                          ", m." + MOVIES_COLUMN_DESCRIPTION +
                                          ", m." + MOVIES_COLUMN_POSTER +
                                     " from " + TABLE_FAVOURITES + " f JOIN " + TABLE_MOVIES + " m" +
                                     " on f." + FAVOURITES_COLUMN_MOVIE_ID + " = m." + MOVIES_COLUMN_ID +
                                     " where f." + FAVOURITES_COLUMN_USERNAME + " = \"" + username + "\"",null);
        while(res.moveToNext()){
            Result movie = new Result();
            movie.setId(res.getInt(0));
            movie.setTitle(res.getString(1));
            movie.setReleaseDate(res.getString(2));
            movie.setOverview(res.getString(3));
            movie.setBackdropPath(res.getString(4));
            favourites.add(movie);
        }
        return favourites;
    }

    public ArrayList<ImageResult> getImages(int movieId){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<ImageResult> images = new ArrayList<>();
        Cursor res = db.rawQuery("select " + IMAGES_COLUMN_IMAGE_PATH +
                " from " + TABLE_IMAGES +
                " where " + IMAGES_COLUMN_MOVIE_ID + " = " + movieId,null);
        while(res.moveToNext()){
            ImageResult img = new ImageResult();
            img.setFilePath(res.getString(0));
            images.add(img);
        }
        return images;
    }

    public VideoResult getVideo(int movieId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + VIDEOS_COLUMN_VIDEO_PATH +
                " from " + TABLE_VIDEOS +
                " where " + VIDEOS_COLUMN_MOVIE_ID + " = " + movieId,null);
        VideoResult video = new VideoResult();
        if(res.moveToNext()){
            video.setKey(res.getString(0));
        }
        return video;
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String filename){
        filename = filename.replaceAll("/","");
        // path to /data/data/yourapp/app_data/imageDir
        File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + filename;
    }

    private String saveVideoToInternalStorage (String key) {
        File directory = context.getDir("videoDir", Context.MODE_PRIVATE);
        String filename = key + ".mp4";
        File file = new File(directory, filename);
        try {
            String filePath = "https://www.youtube.com/embed/" + key;
            java.net.URL url = new URL(filePath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream input = connection.getInputStream();
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return directory.getAbsolutePath() + "/" + filename;
    }
}


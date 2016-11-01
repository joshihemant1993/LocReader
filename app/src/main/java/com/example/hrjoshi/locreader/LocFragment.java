package com.example.hrjoshi.locreader;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LocFragment extends Fragment {
    public LocFragment() {
    }

    //private ArrayAdapter<String> mLocAdapter;
    private CustomListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.loc_fragment, menu);
    }

    ImageView imageView;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            FetchLandmark fetchLandmark = new FetchLandmark();
            fetchLandmark.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchLandmark fetchLandmark = new FetchLandmark();
        fetchLandmark.execute();
    }

    List<RowItem> rowItems;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] data = {
                "Restaurant XYZ",
                "Hotel ABC",
                "Museum AAA",
                "Your home!!!"
        };
        List<String> landmark = new ArrayList<String>(Arrays.asList(data));
        List<Bitmap> imglist = new ArrayList<Bitmap>();
    /*    for(int i=0;i<landmark.size();i++){
            RowItem item = new RowItem(imglist.get(i),landmark.get(i), "Hello");
            rowItems.add(item);
        }
    */
        rowItems = new ArrayList<RowItem>();

   /*     mLocAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_landmark,
                R.id.list_item_landmark_text,
                landmark
        );
        mImgAdapter = new ArrayAdapter<Bitmap>(
                getActivity(),
                R.layout.list_item_landmark,
                R.id.list_item_landmark_image,
                thumbnail
        );
    */
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //    FetchImage fetchImage = new FetchImage(imageView);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_landmark);
        adapter = new CustomListViewAdapter(
                getActivity(),
                R.layout.list_item_landmark,
                new ArrayList<RowItem>());
        listView.setAdapter(adapter);
    //    imageView = (ImageView) rootView.findViewById(R.id.list_item_landmark_image);


        return rootView;

    }

 /*  public class FetchImage extends AsyncTask<String, Object, Bitmap[]> {
        private final String LOG_TAG = FetchImage.class.getSimpleName();
        //private WeakReference<ImageView> imageViewReference;
        private ImageView imageView;
        private int data = 0;

        public FetchImage(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            //imageViewReference = new WeakReference<ImageView>(imageView);
            this.imageView = imageView;
        }

        @Override
        protected Bitmap[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String landmarkJson = null;
            String baseUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=coordinates%7Cpageimages%7Cpageterms&colimit=50&piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description&generator=geosearch&ggscoord=47.606209%7C-122.332071&ggsradius=100&ggslimit=50";
            URL url = null;
            try {
                url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                landmarkJson = buffer.toString();

                Log.v(LOG_TAG, "Landmark String: " + landmarkJson);


            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                    Log.v(LOG_TAG, "Disconnect");
                } else {
                    Log.v(LOG_TAG, "Disconnected");
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream", e);
                }
            }

            return null;
        }

    /*    private Bitmap[] getLandmarkImage(String landmarkJson) throws JSONException {
            JSONObject LandmarkJson = new JSONObject(landmarkJson);

            JSONObject queryobject = LandmarkJson.getJSONObject("query");

            ArrayList<Bitmap> resultImgs = new ArrayList<Bitmap>();
            JSONObject pagesObject = queryobject.getJSONObject("pages");
            Iterator<String> iterator = pagesObject.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    JSONObject value;
                    value = (JSONObject) pagesObject.get(key);
                    JSONObject thumbnail = value.getJSONObject("thumbnail");
                    String source = thumbnail.getString("source");
                    Log.v(LOG_TAG, "thumbnail " + source);
                    URL imgURL = new URL(source);
                    HttpURLConnection imgConnection = (HttpURLConnection) imgURL.openConnection();
                    InputStream inputStream = imgConnection.getInputStream();
                    Bitmap bit = BitmapFactory.decodeStream(inputStream);
                    resultImgs.add(bit);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Bitmap[] resultImg = new Bitmap[resultImgs.size()];
            resultImgs.toArray(resultImg);
            Log.v(LOG_TAG, "String " +resultImgs);
            return resultImg;
        }


       @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            //imageViewReference = new WeakReference<ImageView>(imageView);
            if(imageView!=null && bitmaps!=null){
                for(Bitmap b:bitmaps) {
                    Log.v(LOG_TAG,"Printing Image");
                    imageView.setImageBitmap(b);
                }
            }
        }
    }
*/
        public class FetchLandmark extends AsyncTask<Object, Object, RowItem[]> {

        private final String LOG_TAG = FetchLandmark.class.getSimpleName();

        @Override
        protected RowItem[] doInBackground(Object... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String landmarkJson = null;

            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = lm.getBestProvider(criteria, false);
            Location location;

        /*    location = lm.getLastKnownLocation(bestProvider);


            if (location == null){
                Toast.makeText(getActivity(),"Location Not found",Toast.LENGTH_LONG).show();
            }
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.v(LOG_TAG,"Location" + latitude + longitude);
        */
            try{

              String baseUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=coordinates%7Cpageimages%7Cpageterms&colimit=50&piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description&generator=geosearch&ggscoord=47.606209%7C-122.332071&ggsradius=50&ggslimit=50";
                URL url = new URL(baseUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                landmarkJson = buffer.toString();

             //   Log.v(LOG_TAG,"Landmark String: "+landmarkJson);

            } catch (IOException e) {
                Log.e(LOG_TAG,"Error ",e);
                e.printStackTrace();
            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                    Log.v(LOG_TAG, "Disconnect");
                } else {
                    Log.v(LOG_TAG, "Disconnected");
                }
            }
                if(reader!=null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG,"Error Closing Stream", e);
                    }
                }

            try{
            //    Log.v(LOG_TAG, "Trying to call method");
                return getLandmarkData(landmarkJson);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        return null;
        }

        private RowItem[] getLandmarkData(String landmarkJson) throws JSONException{

            JSONObject LandmarkJson = new JSONObject(landmarkJson);

            //JSONObject batchcomplete = LandmarkJson.getJSONObject("batchcomplete");
            JSONObject queryobject = LandmarkJson.getJSONObject("query");

            ArrayList<String> resultStrs = new ArrayList<String>();
            ArrayList<Bitmap> resultImgs = new ArrayList<Bitmap>();
            JSONObject pagesObject = queryobject.getJSONObject("pages");
            Iterator<String> iterator = pagesObject.keys();

            while(iterator.hasNext()){
                String key = iterator.next();
                try{
                    JSONObject value;
                    value = (JSONObject) pagesObject.get(key);
                    String title = value.getString("title");

                    Log.v(LOG_TAG, "Title is " +title);
                    resultStrs.add(title);
                    if(value.has("thumbnail")) {
                        Log.v(LOG_TAG, "Entering thumbnail ");
                        JSONObject thumbnail = value.getJSONObject("thumbnail");
                            String source = thumbnail.getString("source");
                            //Log.v(LOG_TAG, "thumbnail" + source);
                            URL imgURL = new URL(source);
                            HttpURLConnection imgConnection = (HttpURLConnection) imgURL.openConnection();

                            InputStream inputStream = imgConnection.getInputStream();
                            Bitmap bit = BitmapFactory.decodeStream(inputStream);
                            resultImgs.add(bit);
                            inputStream.close();
                            //Log.v(LOG_TAG, "Image is" + bit);
                    }else{
                        Log.v(LOG_TAG, "No thumbnail ");
                        Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.image_unavailable);
                        Log.v(LOG_TAG, "Bitmap is " +bit);
                        resultImgs.add(bit);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            RowItem[] resultSet= new RowItem[resultStrs.size()];

            for(int i=0;i<resultStrs.size();i++){
                if(resultImgs.get(i)!=null) {
                    resultSet[i].setImage(resultImgs.get(i));
                }
                resultSet[i].setTitle(resultStrs.get(i));
            }
            return resultSet;
        }


        @Override
        protected void onPostExecute(RowItem[] result) {
            if(result!=null){
                adapter.clear();
                for(RowItem listlandmark: result){
                    adapter.add(listlandmark);
                }
            }
        }
    }

}
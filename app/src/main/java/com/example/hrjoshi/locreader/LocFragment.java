package com.example.hrjoshi.locreader;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
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

import static com.example.hrjoshi.locreader.MainActivity.notifyID;

public class LocFragment extends Fragment {
    public static ArrayList<LatLng> Locationresults = new ArrayList<LatLng>();
    private final String TAG = LocFragment.class.getSimpleName();


    public LocFragment() {
    }

    public CustomListViewAdapter adapter;

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
    public Intent Mapintent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            FetchLandmark fetchLandmark = new FetchLandmark();
            fetchLandmark.execute();
            return true;
        }
        if (id == R.id.action_map) {
            Log.v(TAG, "Map item about to be called");
            Mapintent = new Intent(getActivity(),MapsActivity.class);
            Mapintent.putExtra("title","Location");

            startActivity(Mapintent);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        rowItems = new ArrayList<RowItem>();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ArrayList<RowItem> rowList = new ArrayList<RowItem>();
        ListView listView = (ListView) rootView.findViewById(R.id.listview_landmark);
        adapter = new CustomListViewAdapter(
                getContext(),
                R.layout.list_item_landmark,
                rowList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int position, long l){
                RowItem detailItem = adapter.getItem(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, detailItem.toString());
                startActivity(intent);
                }
            }
        );
        return rootView;
    }
    RowItem item = null;

    public class FetchLandmark extends AsyncTask<Object, Object, ArrayList<RowItem>> {

        private final String LOG_TAG = FetchLandmark.class.getSimpleName();

        @Override
        protected ArrayList<RowItem> doInBackground(Object... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String landmarkJson = null;
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = lm.getBestProvider(criteria, false);
            Location location;

            try {
                String baseUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=coordinates%7Cpageimages%7Cpageterms&colimit=50&piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description&generator=geosearch&ggscoord=47.606209%7C-122.332071&ggsradius=1000&ggslimit=50";
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
                Log.e(LOG_TAG, "Error ", e);
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
            try {
                return getLandmarkData(landmarkJson);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        private ArrayList<RowItem> getLandmarkData(String landmarkJson) throws JSONException {
            JSONObject LandmarkJson = new JSONObject(landmarkJson);
            JSONObject queryobject = LandmarkJson.getJSONObject("query");
            ArrayList<LatLng> getLocations = new ArrayList<>();
            ArrayList<RowItem> resultRow = new ArrayList<RowItem>();
            ArrayList<String> resultStrs = new ArrayList<String>();
            ArrayList<Bitmap> resultImgs = new ArrayList<Bitmap>();
            JSONObject pagesObject = queryobject.getJSONObject("pages");
            Iterator<String> iterator = pagesObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String title;
                Bitmap bit;
                double Lat;
                double Long;
                try {
                    JSONObject value;
                    value = (JSONObject) pagesObject.get(key);
                    title = value.getString("title");
                    Log.v(LOG_TAG, "Title is " + title);
                    if (value.has("thumbnail")) {
                        Log.v(LOG_TAG, "Entering thumbnail ");
                        JSONObject thumbnail = value.getJSONObject("thumbnail");
                        String source = thumbnail.getString("source");
                        URL imgURL = new URL(source);
                        HttpURLConnection imgConnection = (HttpURLConnection) imgURL.openConnection();
                        InputStream inputStream = imgConnection.getInputStream();
                        bit = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        Log.v(LOG_TAG, "Bitmap is " + bit);
                    } else {
                        Log.v(LOG_TAG, "No thumbnail ");
                        bit = BitmapFactory.decodeResource(getResources(), R.drawable.image_unavailable);
                        Log.v(LOG_TAG, "Bitmap is " + bit);
                    }
                    item = new RowItem(bit, title);
                    resultRow.add(item);
                    if(value.has("coordinates")){
                        Log.v(LOG_TAG,"fetching latlongs");
                        JSONArray coordinates = value.getJSONArray("coordinates");
                        JSONObject coordinate = (JSONObject) coordinates.get(0);
                        Lat=coordinate.getDouble("lat");
                        Long=coordinate.getDouble("lon");
                        Log.v(LOG_TAG,"Lat is "+Lat);
                        Locationresults.add(new LatLng(Lat,Long));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (ArrayList<RowItem>) resultRow;
        }
        @Override
        protected void onPostExecute(ArrayList<RowItem> result) {
            adapter.clear();
            for (RowItem landmark : result) {
            //    Log.v(LOG_TAG, "End string is " + landmark.getTitle());
                adapter.add(landmark);
            //    Log.v(LOG_TAG, "end of loop");
            }
                  sendNotif();
        }
    }
    //PendingIntent pi = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MapsLocation.class), PendingIntent.FLAG_UPDATE_CURRENT);
    public void sendNotif() {
        NotificationCompat.Builder mnotif = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Nearest location")
                //.setContentIntent(pi)
                .setContentText(item.getTitle());

        mnotif.setPriority(NotificationCompat.PRIORITY_HIGH);
        mnotif.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        //int notifyID = 001;
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyID, mnotif.build());
    }

}

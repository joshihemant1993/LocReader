package com.example.hrjoshi.locreader;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LocFragment extends Fragment {
    public LocFragment() {
    }

    private ArrayAdapter<String> mLocAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.loc_fragment, menu);
    }

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

        mLocAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_landmark,
                R.id.list_item_landmark_text,
                landmark
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_landmark);
        listView.setAdapter(mLocAdapter);

        return rootView;

    }

    public class FetchLandmark extends AsyncTask<Object, Object, String[]> {

        private final String LOG_TAG = FetchLandmark.class.getSimpleName();

        @Override
        protected String[] doInBackground(Object... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String landmarkJson = null;


            String format = "json";
            String lists = "geosearch";

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

                //final String base = "https://en.wikipedia.org/w/api.php?action=query";
                //final String format_param = "format";
                //final String list_param = "list";


            //    String baseUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=geosearch&gscoord=47.606209%7C-122.332071&gsradius=10000&gslimit=10";

              String baseUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=coordinates%7Cpageimages%7Cpageterms&colimit=50&piprop=thumbnail&pithumbsize=144&pilimit=50&wbptterms=description&generator=geosearch&ggscoord=47.606209%7C-122.332071&ggsradius=10000&ggslimit=50";
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

                Log.v(LOG_TAG,"Landmark String: "+landmarkJson);

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

        private String[] getLandmarkData(String landmarkJson) throws JSONException{
            final String LD_LIST = "list";
        //    final String LD_LONGITUDE=  "LONGITUDE";
        //    final String LD_LATITUDE = "LATITUDE";
            final String LD_TITLE = "title";

            JSONObject LandmarkJson = new JSONObject(landmarkJson);

            //JSONObject batchcomplete = LandmarkJson.getJSONObject("batchcomplete");
            JSONObject queryobject = LandmarkJson.getJSONObject("query");

            String[] resultStrs = new String[queryobject.length()];
            /*
            JSONArray geoArray = queryobject.getJSONArray("geosearch");

            //JSONArray pagesArray = queryobject.getJSONArray("pages");

            //JSONObject pageObject = queryobject.getJSONObject("pages")
            String[] resultStrs = new String[geoArray.length()];
            String[] resultStrs = new String[geoArray.length()];
            for(int i=0; i<geoArray.length();i++){
                //JSONObject singleObject = geoArray.getJSONObject(i);
                JSONObject singleObject = geoArray.getJSONObject(i);
                resultStrs[i] = singleObject.getString("title");
                Log.v(LOG_TAG, "Title " + resultStrs[i]);
            }
            */

            JSONObject pagesObject = queryobject.getJSONObject("pages");
            Iterator<String> iterator = pagesObject.keys();

            HashMap<String, String> map = new HashMap<String, String>();

            int i = 0;
            while(iterator.hasNext()){
                String key = iterator.next();
                try{
                    String value;
                    value = pagesObject.getString("title");
                    Log.v(LOG_TAG, "Title is " +value);
                    map.put(key,value);
                    resultStrs[i] = value;
                    i++;
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            return resultStrs;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result!=null){
                mLocAdapter.clear();
                for(String listlandmark: result){
                    mLocAdapter.add(listlandmark);
                }
            }
        }
    }
}
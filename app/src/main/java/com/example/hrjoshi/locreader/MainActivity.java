package com.example.hrjoshi.locreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import static com.example.hrjoshi.locreader.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    public static int notifyID = 001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        if(savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new LocFragment())
                    .commit();
        }

    }
    private int notifyCount = 0;
/*   public void sendNotification(View v){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showPopUp = prefs.getBoolean("pref_show_notification",true);

        if(showPopUp) {
            PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MapsLocation.class), PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("Location Reader")
                    .setContentIntent(pi)
                    .setContentText("Want to see this in a map?");

            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            int notifyID = 001;
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notifyID, mBuilder.build());
            Toast.makeText(this, "The Location Reader app", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "The Location Reader app", Toast.LENGTH_LONG).show();
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

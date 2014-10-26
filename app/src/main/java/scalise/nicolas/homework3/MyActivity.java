package scalise.nicolas.homework3;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MyActivity extends Activity {

    Handler mHandler;
    static SeekBar seek;
    Button play, pause;
    MediaPlayer song;
    GridLayout grid;
    FrameLayout frame;
    static int hour, minute;
    static TextView clock;
    Handler seekHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    protected void onStart() {
        super.onStart();
//
//        loadPreferences();
//        createInit();
//        setColor();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPreferences();
        createInit();
        setColor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (song != null) {
            song.release();
            clearSong();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (song != null) {
            song.release();
            clearSong();
        }
        savePreferences();
        //finish();
    }

    public void createInit() {
//        final Calendar cal = Calendar.getInstance();
//        hour = cal.get(Calendar.HOUR_OF_DAY);
//        minute = cal.get(Calendar.MINUTE);


        grid = (GridLayout) findViewById(R.id.gridLayout);
        frame = (FrameLayout) findViewById(R.id.frameLayout);
        seek = (SeekBar) findViewById(R.id.seekBar);
        play = (Button) findViewById(R.id.PlayButton);
        pause = (Button) findViewById(R.id.StopButton);
        clock = (TextView) findViewById(R.id.textView);
        clock.setText(hour + ":" + minute);
        mHandler = new Handler();

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (song != null && fromUser) {
                    Log.d("Progress", "Progress:  " + progress);
                    song.seekTo(progress);

                }
            }
        });
    }

    public void setSong() {
        if (hour >= 18 || hour < 6) {
            //night song
            song = MediaPlayer.create(MyActivity.this, R.raw.bakerstreet);
        } else {
            //day song
            song = MediaPlayer.create(MyActivity.this, R.raw.comesthesun);
        }
        seek.setMax(song.getDuration());
    }

    public void clearSong() {

        if (song != null) {
            song.release();
            song = null;
            seek.setProgress(0);
        } else
            Toast.makeText(getApplicationContext(), "No song is playing", Toast.LENGTH_SHORT).show();

    }

    public void onTimeClick(View v) {
        setTime();
    }

    public void onPlayClick(View v) {
        if (song == null) {
            setSong();
            song.start();
            seekUpdation();

            //Toast.makeText(getApplicationContext(), hour + ":" + minute,
            //        Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(getApplicationContext(), "Song is already playing", Toast.LENGTH_SHORT).show();

    }

    public void onStopClick(View v) {
        clearSong();
    }

    public void setColor() {

        if (hour >= 18 || hour < 6) {
            grid.setBackgroundColor(getResources().getColor(R.color.NightBlue));
        } else {
            grid.setBackgroundColor(getResources().getColor(R.color.DayRed));
        }
        frame.setBackgroundColor(getResources().getColor(R.color.NuetralGray));

    }


    public void setTime() {
        Bundle args = new Bundle();
        args.putInt("h", hour);
        args.putInt("m", minute);


        MyFragment frag1 = new MyFragment();
        frag1.setArguments(args);
        frag1.setGrid(grid, frame, song, seek);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(frag1, "fragment");
        ft.commit();

        clearSong();

//        FragmentManager fm = getFragmentMa1nager();
//        frag1.show(fm, "picker");
//
//        loadPreferences();
//        setColor();
//        clearSong();
//        clock.setText(hour+":"+minute);
//        TimePickerDialog tpd = new TimePickerDialog(this,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay,
//                                          int minuteOfDay) {
//                        hour = hourOfDay;
//                        minute = minuteOfDay;
//                        setColor();
//                        clearSong();
//                        clock.setText(hour+":"+minute);
//                        savePreferences();
//                    }
//                }, hour, minute, false);
//        tpd.show();
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {

        if (song != null) {
            seek.setProgress(song.getCurrentPosition());
            seekHandler.postDelayed(run, 1000);
        }
    }

    public void savePreferences() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        //editor.putInt("progress", seek.getProgress());
        //if(song!=null)editor.putInt("songProgress", song.getCurrentPosition());
        editor.commit();
    }

    public void loadPreferences() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        final Calendar cal = Calendar.getInstance();
        hour = pref.getInt("hour", cal.get(Calendar.HOUR_OF_DAY));
        minute = pref.getInt("minute", cal.get(Calendar.MINUTE));
        cal.set(0, Calendar.JANUARY, 0, hour, minute);
        //   if(song!=null)song.seekTo(pref.getInt("songProgress", 0));
        //   seek.setProgress(pref.getInt("progress", 0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}




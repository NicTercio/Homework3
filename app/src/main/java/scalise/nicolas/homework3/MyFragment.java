package scalise.nicolas.homework3;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Nicolas on 10/20/2014.
 */
public class MyFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    int hour, minute;
    GridLayout grid;
    FrameLayout frame;
    MediaPlayer song;
    SeekBar seek;

    public void setGrid(GridLayout grd, FrameLayout f, MediaPlayer s, SeekBar bar){
        grid=grd;
        frame = f;
        song = s;
        seek = bar;
    }
    public void setColor()
    {

        if(hour >= 18 || hour <6)
        {
            grid.setBackgroundColor(getResources().getColor(R.color.NightBlue));
        }
        else
        {
            grid.setBackgroundColor(getResources().getColor(R.color.DayRed));
        }
        frame.setBackgroundColor(getResources().getColor(R.color.NuetralGray));

    }
    public void clearSong()
    {

        if(song != null)
        {
            song.release();
            song = null;
            seek.setProgress(0);
        }
    }
    @Override
    public Dialog onCreateDialog (Bundle b)
    {
        hour = MyActivity.hour;
        minute = MyActivity.minute;
        return new TimePickerDialog(getActivity(), this, hour, minute,
                false);

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay)
    {
        MyActivity.hour = hourOfDay;
        MyActivity.minute = minuteOfDay;
        hour = hourOfDay;
        minute = minuteOfDay;
        MyActivity.clock.setText(hourOfDay + ":" + minuteOfDay);
        setColor();
    }
}



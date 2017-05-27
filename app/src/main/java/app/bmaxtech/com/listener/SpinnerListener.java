package app.bmaxtech.com.listener;

import android.view.View;
import android.widget.AdapterView;

import app.bmaxtech.com.utility.GetPlaces;

public class SpinnerListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        GetPlaces.updateRadius(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> val) {
        // do nothing
    }
}

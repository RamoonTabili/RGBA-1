package mad9132.rgb_a;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import model.RGBAModel;

/**
 * The Controller for RGBAModel.
 * <p>
 * As the Controller:
 * a) event handler for the View
 * b) observer of the Model (RGBAModel)
 * <p>
 * Features the Update / React Strategy.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1.0
 */
public class MainActivity extends Activity implements Observer, SeekBar.OnSeekBarChangeListener {
    // CLASS VARIABLES
    private static final String ABOUT_DIALOG_TAG = "About";
    private static final String LOG_TAG = "RGBA";

    // INSTANCE VARIABLES
    // Pro-tip: different naming style; the 'm' means 'member'
    private AboutDialogFragment mAboutDialog;
    private TextView mColorSwatch;
    private RGBAModel mModel;
    private SeekBar mRedSB;
    private TextView mRedTV;
    private SeekBar mGreenSB;
    private TextView mGreenTV;
    private SeekBar mBlueSB;
    private TextView mBlueTV;
    private SeekBar mAlphaSB;
    private TextView mAlphaTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a new AboutDialogFragment()
        // but do not show it (yet)
        mAboutDialog = new AboutDialogFragment();

        // Instantiate a new RGBA model
        // Initialize the model red (max), green (min), blue (min), and alpha (max)
        mModel = new RGBAModel();
        mModel.setRed(RGBAModel.MAX_RGB);
        mModel.setGreen(RGBAModel.MIN_RGB);
        mModel.setBlue(RGBAModel.MIN_RGB);
        mModel.setAlpha(RGBAModel.MAX_ALPHA);
        // The Model is observing this Controller (class MainActivity implements Observer)
        mModel.addObserver(this);

        // reference each View
        mColorSwatch = findViewById(R.id.colorSwatch);
        mRedSB = findViewById(R.id.redSB);
        mRedTV = findViewById(R.id.red);
        mGreenSB = findViewById(R.id.greenSB);
        mGreenTV = findViewById(R.id.green);
        mBlueSB = findViewById(R.id.blueSB);
        mBlueTV = findViewById(R.id.blue);
        mAlphaSB = findViewById(R.id.alphaSB);
        mAlphaTV = findViewById(R.id.alpha);

        // set the domain (i.e. max) for each component
        mRedSB.setMax(RGBAModel.MAX_RGB);
        mGreenSB.setMax(RGBAModel.MAX_RGB);
        mBlueSB.setMax(RGBAModel.MAX_RGB);
        mAlphaSB.setMax(RGBAModel.MAX_ALPHA);

        // register the event handler for each <SeekBar>
        mRedSB.setOnSeekBarChangeListener(this);
        mGreenSB.setOnSeekBarChangeListener(this);
        mBlueSB.setOnSeekBarChangeListener(this);
        mAlphaSB.setOnSeekBarChangeListener(this);

        // initialize the View to the values of the Model
        this.updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_about:
                mAboutDialog.show(getFragmentManager(), ABOUT_DIALOG_TAG);
                return true;
            case R.id.action_red:
                mModel.asRed();
                return true;
            case R.id.action_yellow:
                mModel.asYellow();
                return true;
            case R.id.action_green:
                mModel.asGreen();
                return true;
            case R.id.action_cyan:
                mModel.asCyan();
                return true;
            case R.id.action_blue:
                mModel.asBlue();
                return true;
            case R.id.action_magenta:
                mModel.asMagenta();
                return true;
            case R.id.action_black:
                mModel.asBlack();
                return true;
            case R.id.action_white:
                mModel.asWhite();
                return true;

            default:
                Toast.makeText(this, "MenuItem: " + item.getTitle(), Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Event handler for the <SeekBar>s: red, green, blue, and alpha.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // Did the user cause this event?
        // YES > continue
        // NO  > leave this method
        if (fromUser == false) {
            return;
        }

        // Determine which <SeekBark> caused the event (switch + case)
        // GET the SeekBar's progress, and SET the model to it's new value
        switch (seekBar.getId()) {
            case R.id.redSB:
                mModel.setRed(mRedSB.getProgress());
                mRedTV.setText(getResources().getString(R.string.redProgress, progress).toUpperCase());
                break;
            case R.id.greenSB:
                mModel.setGreen(mGreenSB.getProgress());
                mGreenTV.setText(getResources().getString(R.string.greenProgress, progress).toUpperCase());
                break;
            case R.id.blueSB:
                mModel.setBlue(mBlueSB.getProgress());
                mBlueTV.setText(getResources().getString(R.string.blueProgress, progress).toUpperCase());
                break;
            case R.id.alphaSB:
                mModel.setAlpha(mAlphaSB.getProgress());
                mAlphaTV.setText(getResources().getString(R.string.alphaProgress, progress).toUpperCase());
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // No-Operation
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.redSB:
                mRedTV.setText(getResources().getString(R.string.red));
                break;
            case R.id.greenSB:
                mGreenTV.setText(getResources().getString(R.string.green));
                break;
            case R.id.blueSB:
                mBlueTV.setText(getResources().getString(R.string.blue));
                break;
            case R.id.alphaSB:
                mAlphaTV.setText(getResources().getString(R.string.alpha));
                break;

        }
    }

    // The Model has changed state!
    // Refresh the View to display the current values of the Model.
    @Override
    public void update(Observable observable, Object data) {
        Log.i(LOG_TAG, "The color (int) is: " + mModel.getColor() + "");

        this.updateView();
    }

    private void updateRedSB() {
        //GET the model's red value, and SET the red <SeekBar>
        mRedSB.setProgress(mModel.getRed());
    }

    private void updateGreenSB() {
        mGreenSB.setProgress(mModel.getGreen());
    }

    private void updateBlueSB() {
        mBlueSB.setProgress(mModel.getBlue());
    }

    private void updateColorSwatch() {
        //GET the model's r,g,b,a values, and SET the background colour of the swatch <TextView>
        mColorSwatch.setBackgroundColor(Color.argb(mModel.getAlpha()
                , mModel.getRed()
                , mModel.getGreen()
                , mModel.getBlue()
        ));
    }

    // synchronize each View component with the Model
    public void updateView() {
        this.updateColorSwatch();
        this.updateRedSB();
        this.updateGreenSB();
        this.updateBlueSB();
    }
}   // end of class

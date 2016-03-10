package com.example.robert.morseprototype.Tools;


import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.robert.morseprototype.Misc.BaseActivity;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Switch;
import com.example.robert.morseprototype.Hardware.Output;
import com.example.robert.morseprototype.Misc.MorseTranslations;
import com.example.robert.morseprototype.Options.Options;
import com.example.robert.morseprototype.R;
import com.example.robert.morseprototype.SwipeDialogs.LettersDialog;
import com.example.robert.morseprototype.SwipeDialogs.MorseGestureDetector;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class EnglishToMorse extends BaseActivity {

    private GestureDetectorCompat mDetector;
    private MorseTranslations             morseTranslations;
    private Output                        mOutput;

    @Bind(R.id.editText) EditText    textViewSource;
    @Bind(R.id.textView2) TextView   textViewResult;
    @Bind(R.id.switch1) Switch       switchLight;
    @Bind(R.id.translate) ButtonFlat translate;
    @Bind(R.id.reset) ButtonFlat     reset;

    private ShowcaseConfig config = new ShowcaseConfig();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_to_morse);
        ButterKnife.bind(this);

        mDetector = new GestureDetectorCompat(this, new mainMorseGestureDetector());

        morseTranslations = new MorseTranslations();


        mOutput = new Output(this, false, false, false, false);

        switchLight.setClickable(mOutput.getHasCamera());

        switchLight.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(Switch buttonView, boolean isChecked) {
                mOutput.setLightEnabled(isChecked);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(Options.getEnabledDialogs(this))
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }




    public void translate(View view) {

        String str = textViewSource.getText().toString().trim();

        if (str.isEmpty()) {
            SuperToast.create(this, "You must enter some text", SuperToast.Duration.VERY_SHORT,
             Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
        } else {
            String translatedText = morseTranslations.translatedText(str);
            textViewResult.setText(translatedText);

            if(switchLight.isCheck())
            mOutput.outputString(translatedText);
        }
    }

    public void reset(View view) {
        textViewSource.setText("");
        textViewResult.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mOutput.release();
    }


    private class mainMorseGestureDetector extends MorseGestureDetector {

        public void onSwipeRight() {
            LettersDialog.showLetters(EnglishToMorse.this);
        }

        public void onSwipeLeft() {
            LettersDialog.showNumbers(EnglishToMorse.this);
        }

        public void onSwipeBottom() {
            LettersDialog.showQCodes(EnglishToMorse.this);
        }

        public void onSwipeTop() {
            LettersDialog.showZCodes(EnglishToMorse.this);
        }
    }



    private void showCaseMainActivity(){


        config.setDelay(300);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

        sequence.setConfig(config);

        sequence.addSequenceItem(textViewSource,
                "Enter the text you would like translated here", "GOT IT");

        sequence.addSequenceItem(translate,
                "This button then translates the text you entered", "GOT IT");

        sequence.addSequenceItem(reset,
                "Use reset to clear the text so you can enter a new word", "GOT IT");

        sequence.addSequenceItem(switchLight,
                "This switch toggles the flash on or off, easy!", "GOT IT");


        sequence.start();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:

                showCaseMainActivity();
        }
        return true;

    }





}



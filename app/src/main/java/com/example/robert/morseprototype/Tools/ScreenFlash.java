package com.example.robert.morseprototype.Tools;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.robert.morseprototype.Misc.BaseActivity;
import com.example.robert.morseprototype.Hardware.Output;
import com.example.robert.morseprototype.Misc.MorseTranslations;
import com.example.robert.morseprototype.Options.Options;
import com.example.robert.morseprototype.R;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFlat;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;


import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ScreenFlash extends BaseActivity {




    @Bind(R.id.imageViewQuiz) ImageView imageViewQuiz;
    @Bind(R.id.button12) ButtonFlat     startButton;
    @Bind(R.id.editText4)EditText       textToFlash;

    private ShowcaseConfig config = new ShowcaseConfig();


    String SHOWCASE_ID;

    private Output mOutput;

    Handler mImageHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 1)
                imageViewQuiz.setBackgroundColor(Color.WHITE);
            else
                imageViewQuiz.setBackgroundColor(Color.rgb(30, 136, 229));
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_flash);
        ButterKnife.bind(this);


        if(Options.getSosSpeed(this)) {
            Options.setSpeedFast();
        }else{
            Options.setSpeedSlow();
        }

        mOutput = new Output(this, false, false, false, false);

        bindInterface();

        mOutput.setScreenViewHandler(mImageHandler);

        startButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

        startScreenFlash();

            }
        });
    }



    private void bindInterface() {

                mOutput.setScreenEnabled(true, mImageHandler);

    }



    private void startScreenFlash(){

       final MorseTranslations morseTranslations = new MorseTranslations();


       String str = textToFlash.getText().toString().trim();

       if(str.isEmpty()) {
           SuperToast.create(this, "You must enter some text", SuperToast.Duration.VERY_SHORT,
           Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
       }
       else{
           final String st = morseTranslations.translatedText(str);
           mOutput.outputString(st);

       }



   }




    private void showCaseMainActivity(){


        config.setDelay(300);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

        sequence.setConfig(config);

        sequence.addSequenceItem(textToFlash,
                "Enter the text you would like translated here", "GOT IT");

        sequence.addSequenceItem(startButton,
                "The button starts the flash process", "GOT IT");


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



    @Override
    protected void onDestroy() {
        super.onDestroy();

        mOutput.release();
    }




}
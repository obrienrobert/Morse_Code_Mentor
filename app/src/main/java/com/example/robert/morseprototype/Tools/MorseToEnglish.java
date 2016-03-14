package com.example.robert.morseprototype.Tools;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.robert.morseprototype.Hardware.Sound;
import com.example.robert.morseprototype.Misc.BaseActivity;
import com.example.robert.morseprototype.Misc.MorseTranslations;
import com.example.robert.morseprototype.Options.Options;
import com.example.robert.morseprototype.R;


import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;



public class MorseToEnglish extends BaseActivity {



    private ShowcaseConfig config = new ShowcaseConfig();


    private StringBuilder morseToConvert = new StringBuilder();
    private MorseTranslations mt = new MorseTranslations();

    private Sound playSound = new Sound();


    @Bind(R.id.enterMorse) TextView     enterMorse;
    @Bind(R.id.convertedMorse) TextView convertedMorse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_to_english);
        ButterKnife.bind(this);


    }

    public void dot(View view) {

        morseToConvert.append(".");
        enterMorse.setText(morseToConvert);


        if(Options.getEnabledVoice(this))
            playSound.playSymbol(MorseToEnglish.this, R.raw.dot);

    }

    public void dash(View view){

        morseToConvert.append("-");
        enterMorse.setText(morseToConvert);


        if(Options.getEnabledVoice(this))
            playSound.playSymbol(MorseToEnglish.this, R.raw.dash);

    }

    public void space(View view){

        morseToConvert.append("|");
        enterMorse.setText(morseToConvert);


        if(Options.getEnabledVoice(this))
            playSound.playSymbol(MorseToEnglish.this, R.raw.space);


    }

    public void displayConvertedMorse(View view) {


        if(Options.getEnabledVoice(this))
            playSound.playSymbol(MorseToEnglish.this, R.raw.convert);

        String s = morseToConvert.toString();
        String res[] = s.split("\\|", -1);
        String x = "";

         for(String var : res) {

             x = x + " " + mt.convertMorse(var);
             if(x.equals("|")){
              x = x + " ";
         }
         }

         convertedMorse.setText(x);

         reset();

    }


    private void showCaseMainActivity(){


        config.setDelay(300);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

        sequence.setConfig(config);

        sequence.addSequenceItem(enterMorse,
                "Enter the Morse you would like to translate here using the buttons below", "GOT IT");

        sequence.addSequenceItem(convertedMorse,
                "The converted Morse will be displayed here", "GOT IT");

        sequence.start();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:

                if(Options.getEnabledVoice(this))
                    playSound.playSymbol(MorseToEnglish.this, R.raw.help);

                showCaseMainActivity();
        }
        return true;

    }







    private void reset(){
        enterMorse.setText("");
        morseToConvert.setLength(0);

    }
}

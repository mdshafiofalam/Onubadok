package com.shafiul.onubadok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class translation extends AppCompatActivity {
    TextView detectingText, translatedMsgText;
    TextView outputText;
    String value, s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        getSupportActionBar().setTitle("Translator");

        //showText = findViewById(R.id.showTextField);
        detectingText = findViewById(R.id.detectingTextField);
        outputText = findViewById(R.id.output_textView);
        translatedMsgText = findViewById(R.id.translatedMsgTextField);

        //collecting passed string from main activity
        Intent intent= getIntent();
        value = intent.getStringExtra("passString");
        prepareForTranslation(value);

    }

        public void prepareForTranslation(String value){
            s = value;

            // identify language
        LanguageIdentifier languageIdentifier =
                LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(s)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String languageCode) {
                                if (languageCode.equals("und")) {
                                    Toast.makeText(getApplicationContext(), "Language Not Identified", Toast.LENGTH_SHORT).show();
                                } else {
                                    translationStart(languageCode, s);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be loaded or other internal error.
                                // ...
                            }
                        });
    }

    public void translationStart(String languageCode, String s){
        printLanguageName(languageCode);

        // Create an translator:

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(languageCode)
                        .setTargetLanguage(TranslateLanguage.BENGALI)
                        .build();
        final Translator bengaliTranslator =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .build();
        bengaliTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Translating", Toast.LENGTH_SHORT).show();
                        bengaliTranslator.translate(s).addOnSuccessListener(new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                translatedMsgText.setVisibility(View.VISIBLE);
                                outputText.setVisibility(View.VISIBLE);
                                outputText.setText(s);
                                bengaliTranslator.close();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Translation Failed",Toast.LENGTH_LONG).show();

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Download Failed",Toast.LENGTH_LONG).show();
                    }
                });




    }

    //print input language for the translator
    public void printLanguageName(String languageCode){
        String texten = "English";
        String textde = "German";
        String textbn = "Bengali";
        String textfr = "French";
        String textes = "Spanish";
        String textfi = "Finnish";
        String textga = "Irish";
        String textis = "Icelandic";
        String textit = "Italian";
        String textno = "Norwegian";
        String textpt = "Portuguese";
        String textsv = "Swedish";
        String textNon = "Language not Supported Fully";

        if(languageCode.equals("en")){
            detectingText.setText(texten);
        }
        else if (languageCode.equals("de")){
            detectingText.setText(textde);
        }
        else if(languageCode.equals("bn")){
            detectingText.setText(textbn);
        }
        else if(languageCode.equals("fr")){
            detectingText.setText(textfr);
        }
        else if (languageCode.equals("es")){
            detectingText.setText(textes);
        }
        else if (languageCode.equals("fi")){
            detectingText.setText(textfi);
        }
        else if (languageCode.equals("ga")){
            detectingText.setText(textga);
        }
        else if (languageCode.equals("is")){
            detectingText.setText(textis);
        }
        else if (languageCode.equals("it")){
            detectingText.setText(textit);
        }
        else if (languageCode.equals("no")){
            detectingText.setText(textno);
        }
        else if (languageCode.equals("pt")){
            detectingText.setText(textpt);
        }
        else if (languageCode.equals("sv")){
            detectingText.setText(textsv);
        }
        else {
            detectingText.setText(textNon);
        }

    }


}
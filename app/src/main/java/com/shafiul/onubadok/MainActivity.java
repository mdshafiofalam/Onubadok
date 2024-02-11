package com.shafiul.onubadok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView1;
    EditText editText;
    String passString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = findViewById(R.id.inputImageId);
        textView1 = findViewById(R.id.recognizedMsgTextField);
        editText = findViewById(R.id.recognizedEditTextId);


        //Check camera permission
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    public void textRecognition(View view) {
        //camera Intent object
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        //set image in inputImageId
        imageView.setImageBitmap(bitmap);
        //send image to the recognizer
        int i = 0;
        InputImage image = InputImage.fromBitmap(bitmap, i); //i is rotation value

        TextRecognizer recognizer = TextRecognition.getClient();
        //image recognition
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                String s = visionText.getText();
                                textView1.setVisibility(View.VISIBLE);
                                editText.setText(s);
                                editText.setVisibility(View.VISIBLE);

                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Text Recognition Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

    }


    //passing text for translation
    public void translate(View view) {
        passString = editText.getText().toString();
        Intent intent = new Intent(this, translation.class);
        intent.putExtra("passString", passString);
        startActivity(intent);

    }

    public void about(View view) {
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }
}
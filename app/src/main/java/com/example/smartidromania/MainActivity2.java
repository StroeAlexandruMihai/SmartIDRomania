package com.example.smartidromania;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {
    private EditText edit_text_adress,edit_text_nume, edit_text_prenume, edit_text_bDay, edit_text_medical, edit_text_finance,edit_text_studii,edit_text_cazier;
    private ImageView profile_pic;
    private Button btn_register;
    private RadioGroup radioSexGroup, radioDriveGroup;
    private RadioButton radioSexButton, radioDriveButton;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    String gender;
    String driveLicence;

    final Calendar myCalendar = Calendar.getInstance();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        storageReference = FirebaseStorage.getInstance().getReference("Persons");
        databaseReference = FirebaseDatabase.getInstance().getReference("Persons");
        progressDialog = new ProgressDialog(MainActivity2.this);

        profile_pic = findViewById(R.id.profile_pic);
        btn_register = findViewById(R.id.btn_register);

        edit_text_nume = findViewById(R.id.edit_text_nume);
        edit_text_prenume = findViewById(R.id.edit_text_prenume);
        edit_text_finance = findViewById(R.id.edit_text_finante);
        edit_text_cazier = findViewById(R.id.edit_text_cazier);
        edit_text_medical = findViewById(R.id.edit_text_medical);

        edit_text_studii = findViewById(R.id.edit_text_studii);
        edit_text_adress = findViewById(R.id.edit_text_adress);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioDriveGroup = (RadioGroup) findViewById(R.id.radioDrive);

        edit_text_bDay = (EditText) findViewById(R.id.edit_text_bDay);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edit_text_bDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity2.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selectati o imagine"), Image_Request_Code);

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });


    }@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                profile_pic.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {


        if (FilePathUri != null) {

            progressDialog.setTitle("Imaginea se incarca...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String nume = edit_text_nume.getText().toString().trim();
                            String prenume = edit_text_prenume.getText().toString().trim();
                            String medical = edit_text_medical.getText().toString().trim();
                            String address = edit_text_adress.getText().toString().trim();
                            String police = edit_text_cazier.getText().toString().trim();
                            String studies = edit_text_studii.getText().toString().trim();
                            String bDay = edit_text_bDay.getText().toString().trim();
                            String finance = edit_text_finance.getText().toString().trim();
                            String idKey = UUID.randomUUID().toString();
                            String gender = getGender();
                            String driverLicences = getDriverLicences();


                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Persoana inregistrata cu succes !", Toast.LENGTH_SHORT).show();
                            Person imageUploadInfo = new Person(nume, prenume, address, medical, finance, police, gender,bDay,driverLicences,idKey, studies,taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    });
        }
        else {

            Toast.makeText(MainActivity2.this, "Va rugam alegeti o imagine !", Toast.LENGTH_SHORT).show();

        }
    }
private String getGender(){
    // get selected radio button from radioGroup
    int selectedId = radioSexGroup.getCheckedRadioButtonId();

    // find the radiobutton by returned id
    radioSexButton = (RadioButton) findViewById(selectedId);
    gender = radioSexButton.getText().toString().trim();
    return gender;
}
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_text_bDay.setText(sdf.format(myCalendar.getTime()));
    }

    private String getDriverLicences(){
        int selectedOption = radioDriveGroup.getCheckedRadioButtonId();
        radioDriveButton = (RadioButton) findViewById(selectedOption);
        driveLicence = radioDriveButton.getText().toString().trim();
        return driveLicence;
    }

}


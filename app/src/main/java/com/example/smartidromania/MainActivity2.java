package com.example.smartidromania;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private Button btn_register, btn_scan;
    private RadioGroup radioSexGroup, radioDriveGroup;
    private RadioButton radioSexButton, radioDriveButton;
    TextView edit_text_ID;
    Uri FilePathUri;
    ProgressBar uploadProgressBar;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    String gender;
    String driveLicence;

    // Initialization for NFC
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    String ID_CARD;

    final Calendar myCalendar = Calendar.getInstance();

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mUploadImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Persons");
        progressDialog = new ProgressDialog(MainActivity2.this);

        profile_pic = findViewById(R.id.profile_pic);
        btn_register = findViewById(R.id.btn_register);
        btn_scan = (Button) findViewById(R.id.btn_scan);

        edit_text_ID = (TextView)findViewById(R.id.edit_text_ID);

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


        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_text_ID.setText(ID_CARD);
            }
        });

        // CALENDAR

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
        }); //CALENDAR


        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selectati o imagine"), Image_Request_Code);

            }
        });
//REGISTER USER
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });


        // NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        //Create a PendingIntent object so the Android system can
        //populate it with the details of the tag when it is scanned.
        //PendingIntent.getActivity(Context,requestcode(identifier for
        //                           intent),intent,int)
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
//NFC
    }

    @Override
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


    // UPLOAD PERSON IN DATABASE

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
                            String idKey = ID_CARD;
                            String gender = getGender();
                            String driverLicences = getDriverLicences();

                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();

                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUri = uri.toString();
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Persoana inregistrata cu succes !", Toast.LENGTH_SHORT).show();
                                    Person person = new Person(nume, prenume, address, medical, finance, police, gender,bDay,driverLicences,idKey, studies,downloadUri);
                                    String ImageUploadId = databaseReference.push().getKey();
                                    databaseReference.child(ImageUploadId).setValue(person);
                                }});


                        }
                    });
        }
        else {

            Toast.makeText(MainActivity2.this, "Va rugam alegeti o imagine !", Toast.LENGTH_SHORT).show();

        }
    }
// rest of your code


//GET GENDER FROM RADIO BUTTONS
private String getGender(){
    // get selected radio button from radioGroup
    int selectedId = radioSexGroup.getCheckedRadioButtonId();

    // find the radiobutton by returned id
    radioSexButton = (RadioButton) findViewById(selectedId);
    gender = radioSexButton.getText().toString().trim();
    return gender;
}
//GET BDAY DATE
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_text_bDay.setText(sdf.format(myCalendar.getTime()));
    }
//GET DRIVE LICENCE
    private String getDriverLicences(){
        int selectedOption = radioDriveGroup.getCheckedRadioButtonId();
        radioDriveButton = (RadioButton) findViewById(selectedOption);
        driveLicence = radioDriveButton.getText().toString().trim();
        return driveLicence;
    }




    //NFC
    /*    onResume(), Enable the Foreground Dispatch to listen for NFC intent (Waiting for NFC card to be tapped)
    enableForegroundDispatch allows your current (foreground) activity to intercept our NFC intent and claim priority over all other activities both within the app and other apps.*/

    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            byte[] payload = detectTagData(tag).getBytes();
        }
    }

    private String detectTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        ID_CARD = toHex(id);
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        Log.v("test",sb.toString());
        return sb.toString();
    }
    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

}


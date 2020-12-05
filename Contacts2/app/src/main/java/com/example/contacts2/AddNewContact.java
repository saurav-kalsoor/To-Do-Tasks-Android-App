package com.example.contacts2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewContact extends AppCompatActivity {

    private CircleImageView mImageViewNewImage;
    private EditText mEditNewName, mEditNewNumber;
    private RadioGroup mRadioGroupNew;
    private Button mSaveBtn;
    private Uri mImageUri;
    private DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        myDb = new DatabaseHelper(this);

        mImageViewNewImage = findViewById(R.id.imageView_newImage);
        mEditNewName = findViewById(R.id.editText_newName);
        mEditNewNumber = findViewById(R.id.editText_newNumber);
        mRadioGroupNew = findViewById(R.id.radioGroup_new);
        mSaveBtn = findViewById(R.id.button_NewSave);

        mRadioGroupNew.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mImageUri == null) {
                    if (checkedId == R.id.radio_newMale) {
                        mImageViewNewImage.setImageResource(R.mipmap.male);
                    } else {
                        mImageViewNewImage.setImageResource(R.mipmap.female);
                    }
                }
            }
        });

        mImageViewNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(AddNewContact.this);
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewContact();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            if (mImageUri == null) {
                Toast.makeText(AddNewContact.this, "No image", Toast.LENGTH_SHORT).show();
            }
            mImageViewNewImage.setImageURI(mImageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.icon_save) {
            saveNewContact();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveNewContact() {
        String newName, newNumber, newImageUrl;
        boolean isMale;

        newName = mEditNewName.getText().toString().trim();
        newNumber = mEditNewNumber.getText().toString().trim();
        isMale = mRadioGroupNew.getCheckedRadioButtonId() == R.id.radio_newMale;

        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newNumber)) {
            Toast.makeText(AddNewContact.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mImageUri == null) {
            newImageUrl = "";
        } else {
            newImageUrl = mImageUri.toString() + "";
        }

        Contact contact = new Contact(newName, newNumber, newImageUrl, null, isMale);

        boolean result = myDb.addNewContact(contact);

        if (result) {
            Toast.makeText(AddNewContact.this, "Contact Added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddNewContact.this, "Add Failed", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(AddNewContact.this, MainActivity.class));
    }
}

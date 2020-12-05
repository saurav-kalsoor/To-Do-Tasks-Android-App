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

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContact extends AppCompatActivity {

    private EditText mEditName,mEditNumber;
    private CircleImageView mEditImage;
    private RadioGroup mEditRadioGroup;
    private Uri mImageUri;
    private String editName,editNumber,editImageUrl,id;
    private boolean sex;
    private DatabaseHelper myDb;
    private Button mSaveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        myDb = new DatabaseHelper(this);

        mEditName = findViewById(R.id.editText_editName);
        mEditNumber = findViewById(R.id.editText_editNumber);
        mEditImage = findViewById(R.id.imageView_editImage);
        mEditRadioGroup = findViewById(R.id.radioGroup_edit);
        mSaveBtn = findViewById(R.id.button_editSave);

        editName = getIntent().getStringExtra("name");
        editNumber = getIntent().getStringExtra("number");
        editImageUrl = getIntent().getStringExtra("imageUrl") + "";
        sex = getIntent().getBooleanExtra("sex",true);
        id = getIntent().getStringExtra("id");

        mEditName.setText(editName);
        mEditNumber.setText(editNumber);

        if(editImageUrl.equals("")){
            if(sex){
                mEditImage.setImageResource(R.mipmap.male);
            }
            else {
                mEditImage.setImageResource(R.mipmap.female);
            }
        }
        else{
            Picasso.get().load(editImageUrl).placeholder(R.mipmap.male).into(mEditImage);
        }
        if(sex){
            mEditRadioGroup.check(R.id.radio_editMale);
        }else{
            mEditRadioGroup.check(R.id.radio_editFemale);
        }


        mEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(EditContact.this);
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            mEditImage.setImageURI(mImageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.edit_contact_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.icon_edit_done){
            updateContact();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateContact() {
        String name,number,imageUrl;
        boolean isMale;

        name = mEditName.getText().toString();
        number = mEditNumber.getText().toString();
        isMale = mEditRadioGroup.getCheckedRadioButtonId() == R.id.radio_editMale;

        if(mImageUri != null){
            imageUrl = mImageUri.toString();
        }else{
            imageUrl = editImageUrl;
        }


        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(number)){
            Toast.makeText(EditContact.this,"Fields are Empty",Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = myDb.updateData(name,number,imageUrl,isMale,id);

        if(result){
            Toast.makeText(EditContact.this,"Contact Updated",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditContact.this,MainActivity.class));
        }else{
            Toast.makeText(EditContact.this,"Contact Update Failed",Toast.LENGTH_SHORT).show();
        }

    }

}

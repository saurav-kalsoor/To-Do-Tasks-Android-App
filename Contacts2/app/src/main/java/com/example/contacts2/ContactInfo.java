package com.example.contacts2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactInfo extends AppCompatActivity {

    private TextView mTextInfoName,mTextInfoNumber;
    private CircleImageView mInfoImage;
    private String name,number,imageUrl,id;
    private boolean sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        mTextInfoName = findViewById(R.id.editText_InfoName);
        mTextInfoNumber = findViewById(R.id.editText_InfoNumber);
        mInfoImage = findViewById(R.id.imageView_InfoImage);

        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        imageUrl = getIntent().getStringExtra("imageUrl") + "";
        sex = getIntent().getBooleanExtra("sex",true);
        id = getIntent().getStringExtra("id");

        mTextInfoName.setText(name);
        mTextInfoNumber.setText(number);

        if(imageUrl.equals("")){
            if(sex){
                mInfoImage.setImageResource(R.mipmap.male);
            }else {
                mInfoImage.setImageResource(R.mipmap.female);
            }
        }else {
            Picasso.get().load(imageUrl).placeholder(R.mipmap.male).into(mInfoImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contact_info_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.icon_edit){
            Intent intent = new Intent(ContactInfo.this,EditContact.class);
            intent.putExtra("name",name);
            intent.putExtra("number",number);
            intent.putExtra("imageUrl",imageUrl);
            intent.putExtra("sex",sex);
            intent.putExtra("id",id);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

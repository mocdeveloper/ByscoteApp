package com.moc.byscote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class NotificationActivity extends AppCompatActivity {

    ImageButton btn_close;
    ImageView big_img;
    String big_image_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        getSupportActionBar().hide();
        big_image_url = getIntent().getStringExtra("big_image_url");
        big_img = (ImageView) findViewById(R.id.big_img);

        Picasso.get().load(big_image_url).into(big_img);

        btn_close = (ImageButton) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NotificationActivity.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

    }
}

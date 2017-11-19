package android.androidVNC.arlip;

import android.androidVNC.androidVNC;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.androidVNC.R;
import android.view.View;
import android.widget.Toast;

public class Demo2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        if(getIntent() != null){
            Intent intent = new Intent(this, androidVNC.class);
            intent.setAction(Intent.ACTION_MAIN);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

}

package android.androidVNC.ui;

import android.androidVNC.R;
import android.androidVNC.androidVNC;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity{

    Button presentationBut, timetableBut, askQuestionBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presentationBut = findViewById(R.id.join_presentation);
        timetableBut = findViewById(R.id.control);
        askQuestionBut =findViewById(R.id.ask_question);

    }

    public void organise(View view){
        HomeActivity.this.startActivity(new Intent(HomeActivity.this,
               MainActivity.class));
    }
    public void ask(View view){

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(HomeActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.enter_ip_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText ipaddress =  mView.findViewById(R.id.ip_address);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Connect ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        if(ipaddress.getText().toString().isEmpty()){
                            Toast.makeText(HomeActivity.this, "IP Address can't be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(HomeActivity.this,
                                ChatActivity.class);
                        intent.putExtra("ip", ipaddress.getText().toString());
                        HomeActivity.this.startActivity(intent);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        }

    public void join(View view){
        Intent intent = new Intent(this, androidVNC.class);
        intent.setAction(Intent.ACTION_MAIN);
        startActivity(intent);
        finish();
    }
}

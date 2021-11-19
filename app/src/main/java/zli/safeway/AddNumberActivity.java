package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddNumberActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private TextView name;
    private TextView firstname;
    private TextView phonenumber;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);

        saveButton = findViewById(R.id.editButton);
        cancelButton = findViewById(R.id.cancelAddButton);
        name = findViewById(R.id.name);
        firstname = findViewById(R.id.firstname);
        phonenumber = findViewById(R.id.phone);

    }

    public void saveToDB(View v){
        db = new DBHelper(this);
        String nameString = name.getText().toString();
        String firstnameString = firstname.getText().toString();
        String phone = phonenumber.getText().toString();
        db.insertContact(nameString, firstnameString, phone);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelAdd(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
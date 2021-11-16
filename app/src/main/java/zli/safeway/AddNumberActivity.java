package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddNumberActivity extends AppCompatActivity {

    private Button saveButton;
    private TextView name;
    private TextView firstname;
    private TextView phonenumber;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);

        saveButton = findViewById(R.id.editButton);
        name = findViewById(R.id.name);
        firstname = findViewById(R.id.firstname);
        phonenumber = findViewById(R.id.phone);

    }

    public void saveToDB(View v){
        db = new DBHelper(this);
        String nameString = (String) name.getText();
        String firstnameString = (String) firstname.getText();
        String phone = (String) phonenumber.getText();
        db.insertContact(nameString, firstnameString, phone);
    }
}
package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNumberActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private TextView name;
    private TextView firstname;
    private TextView phonenumber;
    DBHelper db;
    private Pattern pattern = Pattern.compile("[0-9]{10}");
    private Matcher matcher;
    boolean allFieldsChecked = false;

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

        matcher = pattern.matcher(phone);

        allFieldsChecked = checkFields();

        if(allFieldsChecked){
            db.insertContact(nameString, firstnameString, phone);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void cancelAdd(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean checkFields(){
        if(firstname.length() == 0){
            firstname.setError("Please fill in the firstname");
            return false;
        }
        if(name.length() == 0){
            name.setError("Please fill in the lastname");
            return false;
        }
        if(phonenumber.length() == 0){
            phonenumber.setError("Please fill in the phonenumber without whitespaces and code");
            return false;
        }else if(!matcher.matches()){
            phonenumber.setError("Please fill in the phonenumber without whitespaces and code ");
            return false;
        }
        return true;
    }
}
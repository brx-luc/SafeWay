package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class UpdateNumberActivity extends AppCompatActivity {

    private Button saveButton;
    private TextInputEditText name;
    private TextInputEditText firstname;
    private TextInputEditText phonenumber;

    String nameString;
    String firstnameString;
    String phoneString;
    Integer id;

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_number);

        Intent intent = getIntent();
        nameString = intent.getStringExtra("name");
        firstnameString = intent.getStringExtra("firstname");
        phoneString = intent.getStringExtra("phone");
        id = Integer.parseInt(intent.getStringExtra("id"));

        name = findViewById(R.id.name);
        firstname = findViewById(R.id.firstname);
        phonenumber = findViewById(R.id.phone);

        name.setText(nameString);
        firstname.setText(firstnameString);
        phonenumber.setText(phoneString);

    }

    public void updateContact(View v){
        db = new DBHelper(this);
        db.updateContact(id, Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(firstname.getText()).toString(), Objects.requireNonNull(phonenumber.getText()).toString());

        Intent intent = new Intent(this, EditNumberActivity.class);
        startActivity(intent);
    }


}
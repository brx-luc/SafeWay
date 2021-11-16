package zli.safeway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class UpdateNumberActivity extends AppCompatActivity {

    private Button saveButton;
    private String name;
    private String firstname;
    private String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_number);
    }
}
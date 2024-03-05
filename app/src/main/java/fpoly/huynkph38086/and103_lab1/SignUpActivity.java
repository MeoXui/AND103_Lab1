package fpoly.huynkph38086.and103_lab1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    EditText edUN, edPW;
    Button btnSignup;

    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edUN = findViewById(R.id.edUN);
        edPW = findViewById(R.id.edPW);
        btnSignup = findViewById(R.id.btnSignup);

        Services services = new Services(this);

        btnSignup.setOnClickListener(v -> {
            email = edUN.getText().toString();
            password = edPW.getText().toString();
            services.signup(email, password);
        });
    }
}
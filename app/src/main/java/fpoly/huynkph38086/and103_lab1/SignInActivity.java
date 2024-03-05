package fpoly.huynkph38086.and103_lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity {
    EditText edUN, edPW;
    Button btnLogin;
    TextView tvForget, tvSignup;
    Services services;

    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edUN = findViewById(R.id.edUN);
        edPW = findViewById(R.id.edPW);
        btnLogin = findViewById(R.id.btnLogin);
        tvForget = findViewById(R.id.tvForget);
        tvSignup = findViewById(R.id.tvSignup);

        services = new Services(this);

        btnLogin.setOnClickListener(v -> {
            email = edUN.getText().toString();
            password = edPW.getText().toString();
            services.signin(email, password, SignOutActivity.class);
        });

        tvForget.setOnClickListener(v -> {
            email = edUN.getText().toString();
            services.sendforget(email);
        });

        tvSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }
}
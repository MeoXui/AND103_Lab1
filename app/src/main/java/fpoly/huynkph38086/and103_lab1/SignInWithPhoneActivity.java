package fpoly.huynkph38086.and103_lab1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInWithPhoneActivity extends AppCompatActivity {
    EditText edPhone, edOTP;
    Button btnGet, btnLogin;
    Services services;

    String phone, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_with_phone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edPhone = findViewById(R.id.edPhone);
        edOTP = findViewById(R.id.edOTP);
        btnGet = findViewById(R.id.btnGet);
        btnLogin = findViewById(R.id.btnLogin);

        services = new Services(this);

        btnGet.setOnClickListener(v -> {
            phone = edPhone.getText().toString();
            services.getOTP(phone);
            edOTP.setText(services.Callbacks());
        });

        btnLogin.setOnClickListener(v -> {
            otp = edOTP.getText().toString();
            services.verifyOTP(otp, SignOutActivity.class);
        });
    }
}
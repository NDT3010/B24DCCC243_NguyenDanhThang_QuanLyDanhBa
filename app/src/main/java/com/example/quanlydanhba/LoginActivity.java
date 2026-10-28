package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Màn hình 1: Đăng nhập.
 * Tài khoản được cố định (hard-code) ngay trong code: admin / 123456.
 */
public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "123456";

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        findViewById(R.id.btnLogin).setOnClickListener(v -> attemptLogin());

        // Nhấn "Done" trên bàn phím cũng đăng nhập
        edtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });

        // Ẩn thông báo lỗi khi người dùng bắt đầu sửa lại
        TextWatcher hideErrorWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        edtUsername.addTextChangedListener(hideErrorWatcher);
        edtPassword.addTextChangedListener(hideErrorWatcher);
    }

    private void attemptLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // 1. Không cho đăng nhập khi bỏ trống
        if (username.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.login_error_empty));
            return;
        }

        // 2. So sánh với tài khoản cố định
        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            // Đúng: sang MainActivity và finish() để nút Back không quay lại đăng nhập
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Sai: báo lỗi và ở lại màn hình đăng nhập
            showError(getString(R.string.login_error_invalid));
            edtPassword.setText("");
        }
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }
}

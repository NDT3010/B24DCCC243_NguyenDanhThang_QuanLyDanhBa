package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Màn hình 3: Thêm liên hệ.
 * Kiểm tra dữ liệu, tạo đối tượng Contact và trả về cho MainActivity.
 */
public class AddContactActivity extends AppCompatActivity {

    public static final String EXTRA_EXISTING_PHONES = "extra_existing_phones";
    public static final String EXTRA_CONTACT = "extra_contact";

    /** Số điện thoại hợp lệ: đúng 10 chữ số và bắt đầu bằng 0. */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");

    private EditText edtName;
    private EditText edtPhone;
    private EditText edtNote;
    private Spinner spnGroup;
    private RadioGroup rgGender;
    private CheckBox cbFavorite;

    private ArrayList<String> existingPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtNote = findViewById(R.id.edtNote);
        spnGroup = findViewById(R.id.spnGroup);
        rgGender = findViewById(R.id.rgGender);
        cbFavorite = findViewById(R.id.cbFavorite);

        existingPhones = getIntent().getStringArrayListExtra(EXTRA_EXISTING_PHONES);
        if (existingPhones == null) {
            existingPhones = new ArrayList<>();
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> saveContact());
    }

    private void saveContact() {
        String name = edtName.getText().toString().trim().replaceAll("\\s+", " ");
        String phone = edtPhone.getText().toString().trim();
        String note = edtNote.getText().toString().trim();

        edtName.setError(null);
        edtPhone.setError(null);

        // 1. Họ tên không được để trống
        if (name.isEmpty()) {
            edtName.setError(getString(R.string.error_name_empty));
            edtName.requestFocus();
            return;
        }

        // 2. Số điện thoại: đủ 10 chữ số, bắt đầu bằng 0
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            edtPhone.setError(getString(R.string.error_phone_invalid));
            edtPhone.requestFocus();
            return;
        }

        // 3. Số điện thoại không được trùng
        if (existingPhones.contains(phone)) {
            edtPhone.setError(getString(R.string.error_phone_duplicate));
            edtPhone.requestFocus();
            return;
        }

        String group = (String) spnGroup.getSelectedItem();
        Contact contact = new Contact(name, phone, group, getSelectedGender(),
                cbFavorite.isChecked(), note);

        // Trả đối tượng Contact về MainActivity qua Intent
        Intent result = new Intent();
        result.putExtra(EXTRA_CONTACT, contact);
        setResult(RESULT_OK, result);

        Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getSelectedGender() {
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbFemale) {
            return getString(R.string.gender_female);
        } else if (checkedId == R.id.rbOther) {
            return getString(R.string.gender_other);
        }
        return getString(R.string.gender_male);
    }

    /** Nút mũi tên trên thanh tiêu đề: đóng màn hình, không tạo lại MainActivity. */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

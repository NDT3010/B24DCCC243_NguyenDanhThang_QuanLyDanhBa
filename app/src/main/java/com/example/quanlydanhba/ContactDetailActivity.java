package com.example.quanlydanhba;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Màn hình 4: Chi tiết liên hệ.
 * Nhận Contact qua Intent, cho phép gọi điện, đổi trạng thái yêu thích hoặc xóa.
 * Kết quả được trả về MainActivity bằng setResult().
 */
public class ContactDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "extra_contact";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_FAVORITE = "extra_favorite";
    public static final String EXTRA_DELETED = "extra_deleted";

    private Contact contact;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Nhận dữ liệu từ MainActivity qua Intent
        contact = (Contact) getIntent().getSerializableExtra(EXTRA_CONTACT);
        if (contact == null) {
            finish();
            return;
        }

        bindViews();
    }

    private void bindViews() {
        TextView tvAvatar = findViewById(R.id.tvAvatar);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvGroup = findViewById(R.id.tvGroup);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvNote = findViewById(R.id.tvNote);
        CheckBox cbFavorite = findViewById(R.id.cbFavorite);

        UiUtils.bindAvatar(tvAvatar, contact);
        tvName.setText(contact.getFullName());
        tvPhone.setText(UiUtils.formatPhone(contact.getPhone()));
        tvGroup.setText(contact.getGroup());
        tvGender.setText(contact.getGender());

        String note = contact.getNote();
        tvNote.setText(note == null || note.isEmpty() ? getString(R.string.note_empty) : note);

        // (5) Đổi trạng thái yêu thích: gán trạng thái ban đầu TRƯỚC khi đặt listener
        cbFavorite.setChecked(contact.isFavorite());
        cbFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contact.setFavorite(isChecked);
            // Trả kết quả ngay để dù nhấn Back hay "Quay lại" thì danh sách vẫn được cập nhật
            Intent result = new Intent();
            result.putExtra(EXTRA_PHONE, contact.getPhone());
            result.putExtra(EXTRA_FAVORITE, isChecked);
            setResult(RESULT_OK, result);
        });

        // (6) Gọi điện
        findViewById(R.id.btnCall).setOnClickListener(v -> dial());

        // (7) Quay lại
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // (8) Xóa liên hệ
        findViewById(R.id.btnDelete).setOnClickListener(v -> confirmDelete());
    }

    /** Intent ngầm định ACTION_DIAL: mở màn hình quay số với số điện thoại của liên hệ. */
    private void dial() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contact.getPhone()));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.toast_no_dialer, Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(getString(R.string.dialog_delete_message, contact.getFullName()))
                .setPositiveButton(R.string.dialog_delete_yes, (dialog, which) -> deleteContact())
                .setNegativeButton(R.string.dialog_delete_no, null)
                .show();
    }

    private void deleteContact() {
        Intent result = new Intent();
        result.putExtra(EXTRA_PHONE, contact.getPhone());
        result.putExtra(EXTRA_DELETED, true);
        setResult(RESULT_OK, result);

        Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

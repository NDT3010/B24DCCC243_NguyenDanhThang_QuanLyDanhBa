package com.example.quanlydanhba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Màn hình 2: Danh sách liên hệ.
 * Danh sách được quản lý bằng ArrayList<Contact> (chỉ lưu trong bộ nhớ khi app đang chạy).
 */
public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private static final String STATE_CONTACTS = "state_contacts";

    private ArrayList<Contact> contacts;
    private ContactAdapter adapter;

    private RecyclerView rvContacts;
    private TextView tvTotal;
    private TextView tvFavorite;
    private TextView tvEmpty;

    /** Nhận kết quả từ màn hình Thêm liên hệ. */
    private final ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                    return;
                }
                @SuppressWarnings("deprecation")
                Contact newContact = (Contact) result.getData()
                        .getSerializableExtra(AddContactActivity.EXTRA_CONTACT);
                if (newContact != null) {
                    contacts.add(newContact);
                    sortContacts();
                    adapter.notifyDataSetChanged();
                    int newIndex = contacts.indexOf(newContact);
                    if (newIndex >= 0) {
                        rvContacts.scrollToPosition(newIndex);
                    }
                    updateUi();
                }
            });

    /** Nhận kết quả từ màn hình Chi tiết (đổi yêu thích / xóa). */
    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                    return;
                }
                Intent data = result.getData();
                String phone = data.getStringExtra(ContactDetailActivity.EXTRA_PHONE);
                int index = indexOfPhone(phone);
                if (index < 0) {
                    return;
                }

                if (data.getBooleanExtra(ContactDetailActivity.EXTRA_DELETED, false)) {
                    contacts.remove(index);
                    adapter.notifyItemRemoved(index);
                } else {
                    boolean favorite = data.getBooleanExtra(ContactDetailActivity.EXTRA_FAVORITE, false);
                    contacts.get(index).setFavorite(favorite);
                    adapter.notifyItemChanged(index);
                }
                updateUi();
            });

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvContacts = findViewById(R.id.rvContacts);
        tvTotal = findViewById(R.id.tvTotal);
        tvFavorite = findViewById(R.id.tvFavorite);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Khôi phục danh sách khi xoay màn hình; lần đầu thì nạp dữ liệu mẫu
        if (savedInstanceState != null) {
            contacts = (ArrayList<Contact>) savedInstanceState.getSerializable(STATE_CONTACTS);
        }
        if (contacts == null) {
            contacts = createSampleData();
        }
        sortContacts();

        adapter = new ContactAdapter(contacts, this);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(adapter);

        // (3) Nút Thêm liên hệ -> mở AddContactActivity, gửi kèm danh sách số điện thoại hiện có
        findViewById(R.id.btnAddContact).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            ArrayList<String> phones = new ArrayList<>();
            for (Contact c : contacts) {
                phones.add(c.getPhone());
            }
            intent.putStringArrayListExtra(AddContactActivity.EXTRA_EXISTING_PHONES, phones);
            addLauncher.launch(intent);
        });

        updateUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CONTACTS, contacts);
    }

    /** Nhấn vào một dòng -> mở màn hình chi tiết, truyền Contact qua Intent. */
    @Override
    public void onContactClick(Contact contact) {
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.EXTRA_CONTACT, contact);
        detailLauncher.launch(intent);
    }

    /** Cập nhật khối thống kê và trạng thái "danh sách trống". */
    private void updateUi() {
        int favoriteCount = 0;
        for (Contact c : contacts) {
            if (c.isFavorite()) {
                favoriteCount++;
            }
        }
        tvTotal.setText(String.valueOf(contacts.size()));
        tvFavorite.setText(String.valueOf(favoriteCount));
        tvEmpty.setVisibility(contacts.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private int indexOfPhone(String phone) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getPhone().equals(phone)) {
                return i;
            }
        }
        return -1;
    }

    /** Dữ liệu mẫu ban đầu theo đề bài. */
    private ArrayList<Contact> createSampleData() {
        ArrayList<Contact> list = new ArrayList<>();
        list.add(new Contact("Nguyễn Văn An", "0901234567", "Bạn bè", "Nam", false,
                "Bạn học cấp 3"));
        list.add(new Contact("Trần Thị Bình", "0912345678", "Gia đình", "Nữ", true,
                "Chị gái, hay gọi buổi tối"));
        list.add(new Contact("Lê Minh Châu", "0987654321", "Công việc", "Nam", false,
                "Đồng nghiệp phòng kinh doanh"));
        return list;
    }

    /** Sắp xếp danh sách liên hệ theo thứ tự từ điển (hỗ trợ tiếng Việt). */
    private void sortContacts() {
        java.text.Collator collator = java.text.Collator.getInstance(new java.util.Locale("vi", "VN"));
        collator.setStrength(java.text.Collator.PRIMARY);
        java.util.Collections.sort(contacts, (c1, c2) -> {
            String name1 = c1.getFullName() != null ? c1.getFullName() : "";
            String name2 = c2.getFullName() != null ? c2.getFullName() : "";
            return collator.compare(name1, name2);
        });
    }
}

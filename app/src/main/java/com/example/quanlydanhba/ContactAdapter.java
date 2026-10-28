package com.example.quanlydanhba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter hiển thị danh sách Contact trong RecyclerView.
 * Mỗi dòng: ảnh đại diện (chữ cái đầu), họ tên, số điện thoại, nhóm, dấu sao yêu thích.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    /** Callback báo cho Activity biết người dùng vừa nhấn vào một liên hệ. */
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    private final List<Contact> contacts;
    private final OnContactClickListener listener;

    public ContactAdapter(List<Contact> contacts, OnContactClickListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        UiUtils.bindAvatar(holder.tvAvatar, contact);
        holder.tvName.setText(contact.getFullName());
        holder.tvPhone.setText(UiUtils.formatPhone(contact.getPhone()));
        holder.tvGroup.setText(contact.getGroup());
        holder.tvStar.setVisibility(contact.isFavorite() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onContactClick(contacts.get(adapterPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        final TextView tvAvatar;
        final TextView tvName;
        final TextView tvPhone;
        final TextView tvGroup;
        final TextView tvStar;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            tvStar = itemView.findViewById(R.id.tvStar);
        }
    }
}

package com.example.quanlydanhba;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import java.util.Locale;

/**
 * Các hàm tiện ích dùng chung để hiển thị liên hệ (ảnh đại diện, định dạng số điện thoại).
 */
public final class UiUtils {

    private static final int[] AVATAR_COLORS = {
            0xFF00796B, // teal
            0xFFD84315, // cam đậm
            0xFF5E35B1, // tím
            0xFF1565C0, // xanh dương
            0xFFAD1457, // hồng đậm
            0xFF2E7D32  // xanh lá
    };

    private UiUtils() {
    }

    /**
     * Lấy chữ cái đầu của TÊN (từ cuối cùng trong họ tên).
     * Ví dụ: "Nguyễn Văn An" -> "A", "Trần Thị Bình" -> "B".
     */
    public static String getInitial(String fullName) {
        if (fullName == null) {
            return "?";
        }
        String trimmed = fullName.trim();
        if (trimmed.isEmpty()) {
            return "?";
        }
        int lastSpace = trimmed.lastIndexOf(' ');
        String givenName = lastSpace >= 0 ? trimmed.substring(lastSpace + 1) : trimmed;
        int len = Character.charCount(givenName.codePointAt(0));
        return givenName.substring(0, len).toUpperCase(Locale.forLanguageTag("vi"));
    }

    /** Định dạng 0901234567 -> "0901 234 567" để dễ đọc. */
    public static String formatPhone(String phone) {
        if (phone != null && phone.length() == 10) {
            return phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7);
        }
        return phone;
    }

    /** Gán chữ cái đầu + màu nền (ổn định theo số điện thoại) cho ảnh đại diện. */
    public static void bindAvatar(TextView avatar, Contact contact) {
        avatar.setText(getInitial(contact.getFullName()));
        int index = Math.floorMod(contact.getPhone().hashCode(), AVATAR_COLORS.length);
        Drawable background = avatar.getBackground().mutate();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(AVATAR_COLORS[index]);
        }
    }
}

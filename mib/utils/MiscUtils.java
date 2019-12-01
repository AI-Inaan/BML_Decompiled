package mv.com.bml.mib.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.models.AccDetailItem;

public class MiscUtils {

    public static class hackyCompare implements Comparator<String> {
        public int compare(String str, String str2) {
            String str3 = "my accounts";
            if (str.equalsIgnoreCase(str3)) {
                return -3;
            }
            String str4 = "my cards";
            if (str.equalsIgnoreCase(str4)) {
                return -2;
            }
            String str5 = "my loans";
            if (str.equalsIgnoreCase(str5)) {
                return -1;
            }
            if (str2.equalsIgnoreCase(str3)) {
                return 1;
            }
            if (str2.equalsIgnoreCase(str4)) {
                return 2;
            }
            if (str2.equalsIgnoreCase(str5)) {
                return 3;
            }
            return str.compareToIgnoreCase(str2);
        }
    }

    public static void addTxRowItem(ArrayList<AccDetailItem> arrayList, String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(new AccDetailItem(str, str2));
        }
    }

    public static void addTxRowItem(ArrayList<AccDetailItem> arrayList, String str, String str2, int i) {
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(new AccDetailItem(str, str2, i));
        }
    }

    public static String capitalize(String str) {
        return capitalize(str, null);
    }

    public static String capitalize(String str, char[] cArr) {
        int length = cArr == null ? -1 : cArr.length;
        if (str == null || str.length() == 0 || length == 0) {
            return str;
        }
        int length2 = str.length();
        StringBuffer stringBuffer = new StringBuffer(length2);
        boolean z = true;
        for (int i = 0; i < length2; i++) {
            char charAt = str.charAt(i);
            if (isDelimiter(charAt, cArr)) {
                stringBuffer.append(charAt);
                z = true;
            } else if (z) {
                stringBuffer.append(Character.toTitleCase(charAt));
                z = false;
            } else {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    public static String capitalizeFully(String str) {
        return capitalizeFully(str, null);
    }

    public static String capitalizeFully(String str, char... cArr) {
        return (TextUtils.isEmpty(str) || (cArr == null ? -1 : cArr.length) == 0) ? str : capitalize(str.toLowerCase(), cArr);
    }

    public static String formatCurrency(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,##0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        return decimalFormat.format(d);
    }

    public static int getColor(Resources resources, double d) {
        int i = d < 0.0d ? R.color.primary_red : d == 0.0d ? R.color.secondary_grey_dark : R.color.primary_green;
        return resources.getColor(i);
    }

    public static int getColor(Resources resources, int i) {
        return getColor(resources, (double) i);
    }

    public static int getColor(Resources resources, String str) {
        return getColor(resources, TextUtils.isEmpty(str) ? 0.0d : Double.parseDouble(str.replace(",", "")));
    }

    private static boolean isDelimiter(char c, char[] cArr) {
        if (cArr == null) {
            return Character.isWhitespace(c);
        }
        for (char c2 : cArr) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }

    public static String maskAccNum(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("*");
        sb.append(str.substring(str.length() - 4));
        return sb.toString();
    }

    public static String nullFreeConcat(String str, String... strArr) {
        StringBuilder sb = new StringBuilder();
        for (String str2 : strArr) {
            if (!TextUtils.isEmpty(str2)) {
                sb.append(str2);
                sb.append(str);
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter != null) {
            int paddingTop = listView.getPaddingTop() + listView.getPaddingBottom();
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, listView);
                if (view instanceof ViewGroup) {
                    view.setLayoutParams(new LayoutParams(-2, -2));
                }
                view.measure(0, 0);
                paddingTop += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = paddingTop + (listView.getDividerHeight() * adapter.getCount());
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) listView.getLayoutParams();
            layoutParams.height += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            listView.setLayoutParams(layoutParams);
        }
    }
}

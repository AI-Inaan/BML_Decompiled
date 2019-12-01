package mv.com.bml.mib.models;

import android.support.annotation.NonNull;

public class HeaderItem implements Comparable<HeaderItem> {
    public String section_title;
    public String subtitle;
    public String subtitle_sub1;
    public String subtitle_sub2;
    public int subtitle_sub2_color;

    private int getComparableInteger() {
        if (this.section_title.equalsIgnoreCase("current & savings")) {
            return 1;
        }
        if (this.section_title.equalsIgnoreCase("credit cards")) {
            return 2;
        }
        if (this.section_title.equalsIgnoreCase("prepaid cards")) {
            return 3;
        }
        return this.section_title.equalsIgnoreCase("loans") ? 4 : -10;
    }

    public int compareTo(@NonNull HeaderItem headerItem) {
        return getComparableInteger() - headerItem.getComparableInteger();
    }

    public boolean hasSubtitle() {
        return this.subtitle != null;
    }
}

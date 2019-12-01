package mv.com.bml.mib.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import mv.com.bml.mib.R;
import mv.com.bml.mib.fragments.BeneficiaryListFragment.TMComparator;
import mv.com.bml.mib.utils.CircleTransform;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.MerchantInfo;
import mv.com.bml.mibapi.models.payments.Payable;

public class BeneficiariesAdapter extends BaseExpandableListAdapter implements Filterable {
    Context _c;
    TreeMap<String, ArrayList<Payable>> _data;
    TreeMap<String, ArrayList<Payable>> _filtereddata;
    Filter filter;

    public class BeneficiaryFilter extends Filter {
        public BeneficiaryFilter() {
        }

        private boolean compare(String str, CharSequence charSequence) {
            return (str == null || charSequence == null || !str.toLowerCase().contains(charSequence.toString().toLowerCase())) ? false : true;
        }

        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence charSequence) {
            TreeMap treeMap = new TreeMap(new TMComparator());
            FilterResults filterResults = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                filterResults.values = BeneficiariesAdapter.this._data;
                filterResults.count = BeneficiariesAdapter.this._data.size();
                return filterResults;
            }
            for (Entry entry : BeneficiariesAdapter.this._data.entrySet()) {
                ArrayList arrayList = new ArrayList();
                Iterator it = ((ArrayList) entry.getValue()).iterator();
                while (it.hasNext()) {
                    Payable payable = (Payable) it.next();
                    if (payable instanceof MerchantInfo) {
                        MerchantInfo merchantInfo = (MerchantInfo) payable;
                        if (!compare(merchantInfo.merchantEnrolAlias, charSequence) && !compare(merchantInfo.merchantEnrolType, charSequence) && !compare(merchantInfo.accountNumber, charSequence) && !compare(merchantInfo.shelfDesc, charSequence)) {
                        }
                    } else if (payable instanceof BeneficiaryInfo) {
                        BeneficiaryInfo beneficiaryInfo = (BeneficiaryInfo) payable;
                        if (!compare(beneficiaryInfo.accountNoCr, charSequence) && !compare(beneficiaryInfo.beneficiaryAlias, charSequence) && !compare(beneficiaryInfo.shelfDesc, charSequence)) {
                        }
                    }
                    arrayList.add(payable);
                }
                if (arrayList.size() > 0) {
                    treeMap.put(entry.getKey(), arrayList);
                    Log.i("FILTER", "put some values");
                }
            }
            filterResults.count = treeMap.size();
            filterResults.values = treeMap;
            return filterResults;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence charSequence, FilterResults filterResults) {
            BeneficiariesAdapter.this._filtereddata = (TreeMap) filterResults.values;
            BeneficiariesAdapter.this.notifyDataSetChanged();
        }
    }

    public BeneficiariesAdapter(Context context, TreeMap<String, ArrayList<Payable>> treeMap) {
        this._c = context;
        this._data = treeMap;
        this._filtereddata = treeMap;
    }

    public Object getChild(int i, int i2) {
        return ((ArrayList) this._filtereddata.values().toArray()[i]).get(i2);
    }

    public long getChildId(int i, int i2) {
        return (long) i2;
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        String str;
        Object child = getChild(i, i2);
        if (view == null) {
            view = LayoutInflater.from(this._c).inflate(R.layout.listitem_benefitiary_item_padded, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.text_account_name);
        TextView textView2 = (TextView) view.findViewById(R.id.text_account_num);
        ImageView imageView = (ImageView) view.findViewById(R.id.bene_image);
        String str2 = null;
        String str3 = ")";
        String str4 = " (";
        if (child instanceof BeneficiaryInfo) {
            BeneficiaryInfo beneficiaryInfo = (BeneficiaryInfo) child;
            textView.setText(beneficiaryInfo.beneficiaryAlias);
            str = beneficiaryInfo.accountNoCr;
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(beneficiaryInfo.beneficiaryCcy)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(str4);
                sb.append(beneficiaryInfo.beneficiaryCcy);
                sb.append(str3);
                str = sb.toString();
            }
            str2 = beneficiaryInfo.imageUrl;
        } else if (child instanceof MerchantInfo) {
            MerchantInfo merchantInfo = (MerchantInfo) child;
            textView.setText(merchantInfo.merchantEnrolAlias);
            String str5 = merchantInfo.accountNumber;
            if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(merchantInfo.currency)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str5);
                sb2.append(str4);
                sb2.append(merchantInfo.currency);
                sb2.append(str3);
                str5 = sb2.toString();
            }
            str2 = merchantInfo.imageUrl;
        } else {
            str = "";
        }
        textView2.setText(str);
        if (TextUtils.isEmpty(str)) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
        }
        if (str2 != null) {
            String str6 = "/";
            if (!str2.startsWith(str6)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str6);
                sb3.append(str2);
                str2 = sb3.toString();
            }
        }
        Picasso authedPicasso = BMLApi.getInstance().getAuthedPicasso(this._c);
        StringBuilder sb4 = new StringBuilder();
        sb4.append(BMLApi.BML_API_ENDPOINT);
        sb4.append(str2);
        authedPicasso.load(sb4.toString()).placeholder((int) R.drawable.ic_person_round).error((int) R.drawable.ic_person_round).transform((Transformation) new CircleTransform()).fit().centerInside().into(imageView);
        return view;
    }

    public int getChildrenCount(int i) {
        return ((ArrayList) this._filtereddata.values().toArray()[i]).size();
    }

    public Filter getFilter() {
        if (this.filter == null) {
            this.filter = new BeneficiaryFilter();
        }
        return this.filter;
    }

    public Object getGroup(int i) {
        return this._filtereddata.keySet().toArray()[i];
    }

    public int getGroupCount() {
        return this._filtereddata.size();
    }

    public long getGroupId(int i) {
        return (long) i;
    }

    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(this._c).inflate(R.layout.listitem_expand_header, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.text1)).setText(((String) getGroup(i)).toUpperCase());
        ((ImageView) view.findViewById(R.id.group_indicator)).setImageResource(z ? R.drawable.ic_expand_less : R.drawable.ic_expand_more);
        return view;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}

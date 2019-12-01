package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import java.util.ArrayList;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.ShelfAdapter;
import mv.com.bml.mib.interfaces.OnFragmentInteractionListener;
import mv.com.bml.mib.interfaces.Titleable;
import mv.com.bml.mib.utils.MiscUtils;
import mv.com.bml.mibapi.BMLApi;
import mv.com.bml.mibapi.models.accounts.AccountCommon;
import mv.com.bml.mibapi.models.misc.ValidationResult;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.Currency;
import mv.com.bml.mibapi.models.payments.OtherBank;
import mv.com.bml.mibapi.models.payments.contacts.CreateBen;
import mv.com.bml.mibapi.models.payments.contacts.ShelfList.ShelfItem;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddBeneficiaryFragment extends Fragment implements OnClickListener, Titleable {
    private static final String ARG_EDIT = "edititem";
    private static final String ARG_PAYFROM = "payfrom";
    private static final String ARG_PAY_FLOW = "param3";
    private int acc_type = 0;
    @BindView(2131361843)
    TextView btn_acctype;
    @BindView(2131361864)
    Button btn_confirm;
    @BindView(2131361849)
    TextView btn_select_domestic_bankcode;
    @BindView(2131361850)
    TextView btn_select_domestic_currency;
    /* access modifiers changed from: private */
    public Currency currency;
    @BindView(2131361905)
    EditText edit_accountName;
    @BindView(2131361906)
    EditText edit_accountnum;
    private BeneficiaryInfo edit_item;
    @BindView(2131361923)
    TextView error_text;
    /* access modifiers changed from: private */
    public boolean flag_pay;
    private ShelfAdapter mAdapter;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    /* access modifiers changed from: private */
    public OtherBank other_bank;
    /* access modifiers changed from: private */
    public AccountCommon payfrom;
    private ArrayList<ShelfItem> shelfList;
    private TextWatcher tw;
    @BindView(2131361883)
    View view_acctype_container;
    @BindView(2131361953)
    View view_domestic_bankcode_container;
    @BindView(2131361954)
    View view_domestic_currency_container;

    private void createBeneficiary() {
        CreateBen createBen = new CreateBen(this.edit_accountName.getText().toString(), this.edit_accountnum.getText().toString());
        if (this.acc_type == 1) {
            createBen.bankCode = this.other_bank.value;
            createBen.bankDesc = this.other_bank.label;
            createBen.accountOptionCr = "D";
            createBen.accountOptionDesc = "Other Domestic Bank";
            createBen.beneficiaryName = this.edit_accountName.getText().toString();
            createBen.beneficiaryCcy = this.currency.code;
        }
        BeneficiaryInfo beneficiaryInfo = this.edit_item;
        if (beneficiaryInfo != null) {
            createBen.beneficiaryId = beneficiaryInfo.beneficiaryId;
        }
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        BMLApi.getClient().createBeneficiary(this.edit_item != null ? "update" : "create", createBen, null, false, new Callback<BeneficiaryInfo>() {
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                AddBeneficiaryFragment.this.setError("An error occured. Please try again");
                progressDialog.dismiss();
            }

            public void success(BeneficiaryInfo beneficiaryInfo, Response response) {
                BeneficiaryListFragment beneficiaryListFragment;
                OnFragmentInteractionListener onFragmentInteractionListener;
                if (beneficiaryInfo.status.equalsIgnoreCase("SUCCESS")) {
                    BMLApi.getInstance().getDataCache().invalidateCache("payments.beneficiaries");
                    Toast.makeText(AddBeneficiaryFragment.this.getContext().getApplicationContext(), "Contact added successfully", 0).show();
                    if (AddBeneficiaryFragment.this.mListener != null) {
                        if (AddBeneficiaryFragment.this.payfrom != null) {
                            onFragmentInteractionListener = AddBeneficiaryFragment.this.mListener;
                            beneficiaryListFragment = BeneficiaryListFragment.newInstance(AddBeneficiaryFragment.this.payfrom);
                        } else {
                            onFragmentInteractionListener = AddBeneficiaryFragment.this.mListener;
                            beneficiaryListFragment = BeneficiaryListFragment.newInstance(AddBeneficiaryFragment.this.flag_pay);
                        }
                        onFragmentInteractionListener.onFragmentInteraction(beneficiaryListFragment, false);
                    }
                } else if (beneficiaryInfo.validationResultList.size() > 0) {
                    AddBeneficiaryFragment.this.setError(TextUtils.join("\n", ((ValidationResult) beneficiaryInfo.validationResultList.get(0)).messages));
                } else {
                    AddBeneficiaryFragment.this.setError("An error occured. Please try again");
                }
                progressDialog.dismiss();
            }
        });
    }

    /* access modifiers changed from: private */
    public void initAccountType(int i) {
        if (i == 1) {
            this.view_domestic_bankcode_container.setVisibility(0);
        } else if (i != 2) {
            this.view_domestic_bankcode_container.setVisibility(8);
            this.view_domestic_currency_container.setVisibility(8);
            i = 0;
            this.btn_acctype.setText(getResources().getStringArray(R.array.benef_account_type_list)[i]);
            this.acc_type = i;
        } else {
            this.view_domestic_bankcode_container.setVisibility(8);
        }
        this.view_domestic_currency_container.setVisibility(0);
        this.btn_acctype.setText(getResources().getStringArray(R.array.benef_account_type_list)[i]);
        this.acc_type = i;
    }

    public static AddBeneficiaryFragment newInstance(AccountCommon accountCommon) {
        AddBeneficiaryFragment addBeneficiaryFragment = new AddBeneficiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PAYFROM, accountCommon);
        addBeneficiaryFragment.setArguments(bundle);
        return addBeneficiaryFragment;
    }

    public static AddBeneficiaryFragment newInstance(AccountCommon accountCommon, BeneficiaryInfo beneficiaryInfo) {
        AddBeneficiaryFragment addBeneficiaryFragment = new AddBeneficiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PAYFROM, accountCommon);
        bundle.putSerializable(ARG_EDIT, beneficiaryInfo);
        addBeneficiaryFragment.setArguments(bundle);
        return addBeneficiaryFragment;
    }

    public static AddBeneficiaryFragment newInstance(boolean z) {
        AddBeneficiaryFragment addBeneficiaryFragment = new AddBeneficiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PAY_FLOW, z);
        addBeneficiaryFragment.setArguments(bundle);
        return addBeneficiaryFragment;
    }

    public static AddBeneficiaryFragment newInstance(boolean z, BeneficiaryInfo beneficiaryInfo) {
        AddBeneficiaryFragment addBeneficiaryFragment = new AddBeneficiaryFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_PAY_FLOW, z);
        bundle.putSerializable(ARG_EDIT, beneficiaryInfo);
        addBeneficiaryFragment.setArguments(bundle);
        return addBeneficiaryFragment;
    }

    /* access modifiers changed from: private */
    public void selectDomesticCode() {
        String str = "accounts.domestic_banks";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            final ArrayList arrayList = (ArrayList) BMLApi.getInstance().getDataCache().getCacheItem(str);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                OtherBank otherBank = (OtherBank) it.next();
                otherBank.label = MiscUtils.capitalizeFully(otherBank.label);
            }
            new Builder(getContext()).setAdapter(new ArrayAdapter(getContext(), 17367043, arrayList), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddBeneficiaryFragment.this.other_bank = (OtherBank) arrayList.get(i);
                    AddBeneficiaryFragment.this.btn_select_domestic_bankcode.setText(AddBeneficiaryFragment.this.other_bank.bic);
                    dialogInterface.dismiss();
                    AddBeneficiaryFragment.this.updateConfirmButton();
                }
            }).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            BMLApi.getClient().getDomesticBankList(new Callback<ArrayList<OtherBank>>() {
                public void failure(RetrofitError retrofitError) {
                    retrofitError.printStackTrace();
                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddBeneficiaryFragment.this.getActivity(), "Failed", 1).show();
                    }
                }

                public void success(ArrayList<OtherBank> arrayList, Response response) {
                    BMLApi.getInstance().getDataCache().setCacheItem("accounts.domestic_banks", arrayList, true);
                    progressDialog.dismiss();
                    AddBeneficiaryFragment.this.selectDomesticCode();
                }
            });
        }
        updateConfirmButton();
    }

    /* access modifiers changed from: private */
    public void selectDomesticCurrency() {
        String str = "payments.currency";
        if (BMLApi.getInstance().getDataCache().hasValidCacheItem(str)) {
            final ArrayList arrayList = (ArrayList) BMLApi.getInstance().getDataCache().getCacheItem(str);
            new Builder(getContext()).setAdapter(new ArrayAdapter(getContext(), 17367043, arrayList), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddBeneficiaryFragment.this.currency = (Currency) arrayList.get(i);
                    AddBeneficiaryFragment.this.btn_select_domestic_currency.setText(AddBeneficiaryFragment.this.currency.code);
                    dialogInterface.dismiss();
                    AddBeneficiaryFragment.this.updateConfirmButton();
                }
            }).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            BMLApi.getClient().getCurrencyList(new Callback<ArrayList<Currency>>() {
                public void failure(RetrofitError retrofitError) {
                    retrofitError.printStackTrace();
                    try {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddBeneficiaryFragment.this.getActivity(), "Failed", 1).show();
                    }
                }

                public void success(ArrayList<Currency> arrayList, Response response) {
                    BMLApi.getInstance().getDataCache().setCacheItem("payments.currency", arrayList, true);
                    progressDialog.dismiss();
                    AddBeneficiaryFragment.this.selectDomesticCurrency();
                }
            });
        }
        updateConfirmButton();
    }

    /* access modifiers changed from: private */
    public void setError(String str) {
        this.error_text.setText(str);
        this.error_text.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void updateConfirmButton() {
        boolean z;
        Button button;
        if (TextUtils.isEmpty(this.edit_accountName.getText().toString()) || TextUtils.isEmpty(this.edit_accountnum.getText().toString()) || (this.acc_type != 0 && this.other_bank == null && this.currency == null)) {
            button = this.btn_confirm;
            z = false;
        } else {
            button = this.btn_confirm;
            z = true;
        }
        button.setEnabled(z);
    }

    public String getTitle() {
        return "Add Contact";
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(activity.toString());
            sb.append(" must implement OnFragmentInteractionListener");
            throw new ClassCastException(sb.toString());
        }
    }

    public void onClick(View view) {
        this.error_text.setVisibility(4);
        createBeneficiary();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Paper.init(getContext());
        if (getArguments() != null) {
            this.payfrom = (AccountCommon) getArguments().getSerializable(ARG_PAYFROM);
            this.flag_pay = getArguments().getBoolean(ARG_PAY_FLOW, false);
            this.edit_item = (BeneficiaryInfo) getArguments().getSerializable(ARG_EDIT);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_add_beneficiary, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.shelfList = new ArrayList<>();
        this.mAdapter = new ShelfAdapter(getActivity(), this.shelfList);
        this.tw = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                AddBeneficiaryFragment.this.updateConfirmButton();
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        };
        this.edit_accountName.addTextChangedListener(this.tw);
        this.edit_accountnum.addTextChangedListener(this.tw);
        updateConfirmButton();
        if (this.edit_item != null) {
            this.btn_confirm.setText("Update Contact");
        }
        this.btn_confirm.setOnClickListener(this);
        BeneficiaryInfo beneficiaryInfo = this.edit_item;
        if (beneficiaryInfo != null) {
            this.edit_accountnum.setText(beneficiaryInfo.accountNoCr);
            this.edit_accountName.setText(this.edit_item.beneficiaryAlias);
        }
        this.view_acctype_container.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new Builder(AddBeneficiaryFragment.this.getContext()).setItems((int) R.array.benef_account_type_list, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddBeneficiaryFragment.this.initAccountType(i);
                    }
                }).show();
            }
        });
        this.view_domestic_bankcode_container.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddBeneficiaryFragment.this.selectDomesticCode();
            }
        });
        this.view_domestic_currency_container.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddBeneficiaryFragment.this.selectDomesticCurrency();
            }
        });
        BeneficiaryInfo beneficiaryInfo2 = this.edit_item;
        if (beneficiaryInfo2 == null) {
            initAccountType(0);
        } else if (beneficiaryInfo2.accountOptionCr != null && this.edit_item.accountOptionCr.equalsIgnoreCase("d")) {
            initAccountType(1);
            this.other_bank = new OtherBank();
            this.other_bank.value = this.edit_item.bankCode;
            OtherBank otherBank = this.other_bank;
            otherBank.label = "Domestic Account";
            this.btn_select_domestic_bankcode.setText(otherBank.value);
        }
        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}

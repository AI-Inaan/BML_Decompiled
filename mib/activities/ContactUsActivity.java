package mv.com.bml.mib.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mv.com.bml.mib.R;

public class ContactUsActivity extends BasePinCodeActivity {
    @BindView(2131361858)
    Button btn_email;
    @BindView(2131361859)
    Button btn_sms;
    @BindView(2131361909)
    EditText editId;
    @BindView(2131361910)
    EditText editIssue;
    @BindView(2131361916)
    EditText editname;
    private TextWatcher tw;

    private void call(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("tel:");
        sb.append(str);
        String sb2 = sb.toString();
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse(sb2));
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void checkEnableButtons() {
        boolean z;
        Button button;
        if (TextUtils.isEmpty(this.editId.getText().toString()) || TextUtils.isEmpty(this.editname.getText().toString()) || TextUtils.isEmpty(this.editIssue.getText().toString())) {
            z = false;
            this.btn_sms.setEnabled(false);
            button = this.btn_email;
        } else {
            z = true;
            this.btn_email.setEnabled(true);
            button = this.btn_sms;
        }
        button.setEnabled(z);
    }

    private void sendSMS(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("smsto:");
        sb.append(str);
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse(sb.toString()));
        intent.putExtra("sms_body", str2);
        startActivity(intent);
    }

    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        super.onBackPressed();
    }

    @OnClick({2131361859, 2131361858, 2131361844})
    public void onClick(View view) {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("ID Card: ");
        sb.append(this.editId.getText());
        String str2 = "\n";
        sb.append(str2);
        sb.append("Name: ");
        sb.append(this.editname.getText());
        sb.append(str2);
        sb.append("Issue: ");
        sb.append(this.editIssue.getText());
        switch (view.getId()) {
            case R.id.btn_callcard /*2131361844*/:
                call("+9603330200");
                return;
            case R.id.btn_sendemail /*2131361858*/:
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.EMAIL", new String[]{"customerservice@bml.com.mv"});
                if (!TextUtils.isEmpty(this.editId.getText().toString())) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.editId.getText().toString());
                    sb2.append(" ");
                    sb2.append(this.editname.getText().toString());
                    str = sb2.toString();
                } else {
                    str = "VIA MOBILE";
                }
                intent.putExtra("android.intent.extra.SUBJECT", str);
                intent.putExtra("android.intent.extra.TEXT", sb.toString());
                startActivity(Intent.createChooser(intent, "Send Email"));
                return;
            case R.id.btn_sendsms /*2131361859*/:
                sendSMS("+9607990200", sb.toString());
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind((Activity) this);
        this.btn_sms.setEnabled(false);
        this.btn_email.setEnabled(false);
        this.tw = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                ContactUsActivity.this.checkEnableButtons();
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        };
        this.editId.addTextChangedListener(this.tw);
        this.editname.addTextChangedListener(this.tw);
        this.editIssue.addTextChangedListener(this.tw);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

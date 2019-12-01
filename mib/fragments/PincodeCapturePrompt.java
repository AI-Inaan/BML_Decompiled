package mv.com.bml.mib.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mv.com.bml.mib.R;

public class PincodeCapturePrompt extends Fragment {
    private OnFragmentInteractionListener mListener;
    @BindView(2131362090)
    TextView t;

    public interface OnFragmentInteractionListener {
        void onCaptureActionClicked();
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

    @OnClick({2131361845})
    public void onButtonPressed() {
        OnFragmentInteractionListener onFragmentInteractionListener = this.mListener;
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onCaptureActionClicked();
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_pincode_capture_prompt, viewGroup, false);
        ButterKnife.bind((Object) this, inflate);
        this.t.setText(Html.fromHtml(getString(R.string.loginheader_pincode_description_capture)));
        setHasOptionsMenu(true);
        return inflate;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}

package com.fit.vedads.appstore.SectionAplikacije;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.KreditnaKarticaAPI;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.model.KreditnaKartica;


public class PaymentFragment extends DialogFragment {
    private EditText etBrRacuna;
    private EditText etCVV;
    private TextView tvError;
    private static final String ARG_onPossitiveDismiss = "ARG_onPossitiveDismiss";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_payment,container, false);
        getDialog().setTitle("Payment");
        etBrRacuna = (EditText) view.findViewById(R.id.payment_etBrKartice);
        etCVV = (EditText) view.findViewById(R.id.payment_etCVV);
        tvError = (TextView) view.findViewById(R.id.payment_tvError);
        Button btnUredu = (Button) view.findViewById(R.id.payment_btnUredu);
        btnUredu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnUredu();
            }
        });

        return view;
    }

    private void do_btnUredu() {
        KreditnaKarticaAPI.CheckKreditnaKartica(getActivity(),etBrRacuna.getText().toString(),etCVV.getText().toString(),new MyRunnable< KreditnaKartica>()
        {
            @Override
            public void run(KreditnaKartica result) {
                if(result!=null) {
                    final MyRunnable<KreditnaKartica> onPositiveDismiss = (MyRunnable<KreditnaKartica> ) getArguments().getSerializable(ARG_onPossitiveDismiss);
                    onPositiveDismiss.run(result);
                    tvError.setText("");
                    getDialog().dismiss();
                }else
                {
                    tvError.setText("Molimo da unesete ispravne podatke");
                }
            }
        });
    }

    public static void otvoriPaymentDialog(FragmentActivity activity, MyRunnable<KreditnaKartica> runOnDismiss)
    {
        final PaymentFragment fragment = new PaymentFragment();
        final Bundle arg = new Bundle();
        arg.putSerializable(ARG_onPossitiveDismiss,runOnDismiss);
        fragment.setArguments(arg);

        FragmentManager fm = activity.getSupportFragmentManager();
        fragment.show(fm,"pay_tag");
    }
}

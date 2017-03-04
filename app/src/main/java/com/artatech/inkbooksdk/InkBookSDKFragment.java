package com.artatech.inkbooksdk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.artatech.sdk.InkBookSDK;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Goltstein on 02.03.17.
 */

public class InkBookSDKFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    @InjectView(R.id.status_text)
    TextView mStatus;
    @InjectView(R.id.text_pages)
    TextView mPagesCount;

    public InkBookSDKFragment() {
    }

    public static InkBookSDKFragment newInstance(int sectionNumber) {
        InkBookSDKFragment fragment = new InkBookSDKFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inkbooksdk_fragment, container, false);
        ButterKnife.inject(this, rootView);

        if (!InkBookSDK.isFullSupported() && !InkBookSDK.isRefreshSupport()) {
            //device is not InkBook
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTIONS");
            rootView.findViewById(R.id.v_part).setEnabled(false);
            rootView.findViewById(R.id.v_rapid).setEnabled(false);
            rootView.findViewById(R.id.v_refresh).setEnabled(false);
            rootView.findViewById(R.id.v_pages).setEnabled(false);
            mPagesCount.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");
        } else if (!InkBookSDK.isFullSupported() && InkBookSDK.isRefreshSupport()) {
            //device is InkBook Classic2
            mStatus.setText("REFRESH SUPPORT ONLY");
            rootView.findViewById(R.id.v_part).setEnabled(false);
            rootView.findViewById(R.id.v_rapid).setEnabled(false);
        } else {
            //device is InkBook Prome
            rootView.findViewById(R.id.test_view).setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @OnClick(R.id.v_part)
    public void onPart() {
        if (InkBookSDK.setPARTMode(getActivity()))
            updateStatus(true);
        else
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");
    }

    @OnClick(R.id.v_rapid)
    public void onRAPID() {
        if (InkBookSDK.setRAPIDMode(getActivity()))
            updateStatus(false);
        else
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");
    }

    @OnClick(R.id.v_refresh)
    public void onRefresh() {
        if (!InkBookSDK.refreshScreen(getActivity()))
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");

    }

    @OnClick(R.id.v_pages)
    public void onGetPagesClick() {
        mPagesCount.setText(String.valueOf(InkBookSDK.getPagesToRefresh(getActivity())));
    }

    private void updateStatus(boolean PART) {
        if (PART) {
            mStatus.setText("DISPLAY MODE: PART");
        } else {
            mStatus.setText("DISPLAY MODE: RAPID");
        }
    }
}

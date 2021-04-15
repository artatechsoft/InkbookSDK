package com.artatech.inkbooksdk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Goltstein on 02.03.17.
 */

public class InkBookSDKFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.status_text)
    TextView mStatus;
    @BindView(R.id.text_pages)
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
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.v_part)
    public void onPart() {
        if (InkBookSDK.Companion.setPARTMode(getActivity()))
            mStatus.setText(InkBookSDK.Companion.getCurrentRefreshMode(getActivity()).toString());
        else
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");
    }

    @OnClick(R.id.v_rapid)
    public void onRAPID() {
        if (InkBookSDK.Companion.setRAPIDMode(getActivity()))
            mStatus.setText(InkBookSDK.Companion.getCurrentRefreshMode(getActivity()).toString());
        else
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");
    }

    @OnClick(R.id.v_refresh)
    public void onRefresh() {
        if (!InkBookSDK.Companion.refreshScreen(getActivity()))
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");

    }

    @OnClick(R.id.v_a2)
    public void onA2() {
        if (InkBookSDK.Companion.setA2Mode(getActivity()))
            mStatus.setText(InkBookSDK.Companion.getCurrentRefreshMode(getActivity()).toString());
        else
            mStatus.setText("NOT SUPPORTED DEVICE FOR THIS ACTION");
    }

    @OnClick(R.id.v_pages)
    public void onGetPagesClick() {
        mPagesCount.setText(String.valueOf(InkBookSDK.Companion.getPagesToRefresh(getActivity())));
    }

}

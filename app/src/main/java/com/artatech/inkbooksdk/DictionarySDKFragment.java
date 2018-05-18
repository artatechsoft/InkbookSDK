package com.artatech.inkbooksdk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.artatech.dictsdk.DictSDK;
import com.artatech.dictsdk.DictViewBuilder;
import com.artatech.dictsdk.OnDismissListener;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by Goltstein on 02.03.17.
 */

public class DictionarySDKFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    DictViewBuilder widget;

    public DictionarySDKFragment() {
    }

    public static DictionarySDKFragment newInstance(int sectionNumber) {
        DictionarySDKFragment fragment = new DictionarySDKFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dictsdk_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (!DictSDK.dictAppInstalled()) {
            rootView.findViewById(R.id.v_no_dict).setVisibility(View.VISIBLE);
        }


        widget = new DictViewBuilder(getActivity())
                .search("dom")
                .setDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(getActivity(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                })
                .setContainer((ViewGroup) rootView.findViewById(R.id.container));





        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        widget.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        widget.dismiss();
    }


    @OnClick(R.id.fab)
    public void onFabClick() {
        DictViewBuilder widget1 = new DictViewBuilder(getActivity()) //creating new instance
                .search("kompania") // set query to find in dictionary
                .setDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(getActivity(), "onDismiss", Toast.LENGTH_SHORT).show();
                    }
                }) // create onDismissListener (optional)
                .setContainer((ViewGroup) getView().findViewById(R.id.container)); //set container View to show widget

        widget1.show(); // show widget in container View
    }

    @OnLongClick(R.id.fab)
    public boolean onLongClick() {
        widget.dismiss();
        return true;
    }

}

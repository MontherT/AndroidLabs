package com.example.androidlabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView nameValue = view.findViewById(R.id.nameValue);
        TextView heightValue = view.findViewById(R.id.heightValue);
        TextView massValue = view.findViewById(R.id.massValue);

        Bundle args = getArguments();
        if (args != null) {
            nameValue.setText(args.getString("name", "N/A"));
            heightValue.setText(args.getString("height", "N/A"));
            massValue.setText(args.getString("mass", "N/A"));
        } else {
            nameValue.setText("N/A");
            heightValue.setText("N/A");
            massValue.setText("N/A");
        }

        return view;
    }
}

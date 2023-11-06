package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tv_0 = convertView.findViewById(R.id.tv_0);
        TextView tv_1 = convertView.findViewById(R.id.tv_1);
        TextView tv_2 = convertView.findViewById(R.id.tv_2);
        // Populate the data into the template view using the data object
        tv_0.setText(product.getId());
        tv_1.setText(product.getName());
        tv_2.setText(product.getAmount().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
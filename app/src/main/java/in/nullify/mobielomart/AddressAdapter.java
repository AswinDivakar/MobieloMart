package in.nullify.mobielomart;

import android.app.Activity;
import android.renderscript.Short4;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aswin on 26-01-2018.
 */

public class AddressAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] names;
    private String[] addresses;
    public AddressAdapter(Activity context,String[] names, String[] addresses){
        super(context, R.layout.address_list,addresses);
        this.context=context;
        this.names = names;
        this.addresses = addresses;
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.address_list,null,true);
        TextView name = (TextView) rowView.findViewById(R.id.tv_addr_name);
        TextView address = (TextView) rowView.findViewById(R.id.tv_addr_address);
        name.setText(names[position]);
        address.setText(addresses[position]);
        return rowView;
    }
}

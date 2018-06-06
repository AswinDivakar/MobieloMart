package in.nullify.mobielomart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Aswin on 26-01-2018.
 */

public class AddressAdapter extends ArrayAdapter<String> {
    private Activity context;
    private ArrayList<String> names;
    private ArrayList<String> addresses;
    private ArrayList<String> aid;
    public AddressAdapter(Activity context, ArrayList<String> names, ArrayList<String> addresses){
        super(context, R.layout.address_list,addresses);
        this.context=context;
        this.names=names;
        this.addresses=addresses;
        this.aid=aid;
    }
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.address_list,null,true);
        TextView name = (TextView) rowView.findViewById(R.id.tv_addr_name);
        TextView address = (TextView) rowView.findViewById(R.id.tv_addr_address);
        TextView tv_addr_edit = (TextView) rowView.findViewById(R.id.tv_addr_edit);
        TextView tv_addr_delete = (TextView) rowView.findViewById(R.id.tv_addr_delete);

        name.setText(names.get(position));
        address.setText(addresses.get(position));
        tv_addr_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostDelete().execute("1");
                Toast.makeText(getContext(),"Address Deleted",Toast.LENGTH_SHORT).show();
                
            }
        });
        return rowView;
    }
    public class PostDelete extends AsyncTask<String, Void, String> {
        SharedPreferences sharedPref;
        private String CallerPM;
        Context context;

        protected void onPreExecute() {

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://www.nullify.in/mobielo_mart/php/Address/deleteAddress.php");
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("aid", arg0[0]);//post cheyyanda values ex: post..("email","a@.com")
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }

}

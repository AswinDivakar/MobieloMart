package in.nullify.mobielomart;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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

public class AddressActivity extends AppCompatActivity {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> addresses = new ArrayList<>();
    private ArrayList<String> aid = new ArrayList<>();
    private AddressAdapter adapter;
    private ListView lv_address;
    private LinearLayout ll_progress;
    private TextView tv_addr_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        lv_address= findViewById(R.id.lv_address);
        adapter=new AddressAdapter(AddressActivity.this,names,addresses);
        lv_address.setAdapter(adapter);adapter=new AddressAdapter(AddressActivity.this,names,addresses);
        Button btn=(Button)findViewById(R.id.btn_address_add);
        ll_progress=findViewById(R.id.ll_progress);
        TextView tv_addr_edit=(TextView)findViewById(R.id.tv_addr_edit);


        new GetAddress(getApplicationContext()).execute("user_id","1");

        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            }
        });

    }


    public class GetAddress extends AsyncTask<String, Void, String> {
        Context context;
        GetAddress(Context context){
            this.context=context;

        }

        protected void onPreExecute() {
            ll_progress.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL("http://www.nullify.in/mobielo_mart/php/Address/getAddress.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put(arg0[0], arg0[1]);
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
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray contacts = jsonObj.getJSONArray("result");
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    aid.add(c.getString("aid"));
                    Toast.makeText(getApplicationContext(),c.getString("aid"),Toast.LENGTH_LONG).show();
                    names.add(c.getString("name"));
                    addresses.add(c.getString("address"));

                }
                lv_address.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                ll_progress.setVisibility(View.GONE);

            } catch (final JSONException e) {

            }
        }
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
        Log.e("Tag",result.toString());
        return result.toString();
    }
}
package in.nullify.mobielomart;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    private MenuItem searchItem;
    private SearchView seach_prod;

    private ViewPager productViewpager;
    private TabLayout dotProduct;
    private MyCustomPagerAdapter pagerAdapter;

    private ArrayList<String> p_colors = new ArrayList<>();
    private ArrayList<String> p_images = new ArrayList<>();
    private int numImgs=0;

    private TextView product_name;
    private TextView product_price;
    private TextView product_off_price;
    private TextView product_rating;
    private TextView product_rating_count;

    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        pid = getIntent().getIntExtra("pid",0);

        toolbar = (Toolbar) findViewById(R.id.product_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.product_appbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout.setExpanded(false);


        productViewpager = (ViewPager) findViewById(R.id.product_viewpager);
        dotProduct = (TabLayout) findViewById(R.id.dot_product);

        product_name = (TextView) findViewById(R.id.product_name);
        product_price = (TextView) findViewById(R.id.product_price);
        product_off_price = (TextView) findViewById(R.id.product_off_price);
        product_rating = (TextView) findViewById(R.id.product_rating);
        product_rating_count = (TextView) findViewById(R.id.product_rated_count);

        dotProduct.setupWithViewPager(productViewpager, true);


        FetchServer getProduct = new FetchServer(ProductActivity.this);
        getProduct.setUrl("https://nullify.in/mobielo_mart/php/Products/getProduct.php");
        try {
            getProduct.setPostDataParams("id", String.valueOf(pid));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getProduct.setOnFetchListener(new FetchServer.OnFetchListener() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(String result) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray array = jsonObj.getJSONArray("result");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);

                        float rating = Float.parseFloat(c.getString("rating"));

                        if ( rating > 3.5)
                        {
                            product_rating.setText(String.valueOf(rating));
                            product_rating.setBackgroundColor(getResources().getColor(R.color.rating_good));
                        }
                        else if(rating > 2.5)
                        {
                            product_rating.setText(String.valueOf(rating));
                            product_rating.setBackgroundColor(getResources().getColor(R.color.rating_avg));
                        }
                        else {
                            product_rating.setText(String.valueOf(rating));
                            product_rating.setBackgroundColor(getResources().getColor(R.color.rating_bad));
                        }

                        product_name.setText(c.getString("p_name"));
                        if (c.has("off_id"))
                        {
                            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                            format.setMaximumFractionDigits(0);
                            int rs = Integer.parseInt(c.getString("off_price"));
                            product_off_price.setText(format.format(rs));

                            rs = Integer.parseInt(c.getString("p_price"));
                            product_price.setVisibility(View.VISIBLE);
                            product_price.setText((Html.fromHtml("<strike>"+format.format(rs)+"</strike>")));

                        }else {
                            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
                            format.setMaximumFractionDigits(0);
                            int rs = Integer.parseInt(c.getString("p_price"));
                            product_off_price.setText(format.format(rs));

                        }

                        JSONArray col = c.getJSONArray("p_colors");
                        numImgs = Integer.parseInt(c.getString("p_numImgs"));

                        for (int j=0;j<col.length();j++)
                        {
                            p_colors.add(col.getString(j));
                        }

                    }

                    for (int i =1;i<=numImgs;i++) {
                        p_images.add("https://nullify.in/mobielo_mart/images/products/" + String.valueOf(pid) +
                                "/" + p_colors.get(0) + "/" + String.valueOf(i) + ".png");
                    }
                    pagerAdapter = new MyCustomPagerAdapter(getApplicationContext(),p_images);
                    productViewpager.setAdapter(pagerAdapter);

                } catch (Exception e) {
                }
            }
        });

        getProduct.execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

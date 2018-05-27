package in.nullify.mobielomart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity {
    private String SIGNEDIN;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        LinearLayout llsignin=(LinearLayout)findViewById(R.id.ll_signin);
        ListView lv_account=(ListView)findViewById(R.id.lv_account);
        Button btn_acc_signinup=(Button)findViewById(R.id.btn_acc_signinup);

        final SharedPreferences mShared = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SIGNEDIN = (mShared.getString("SIGNEDIN", "NO"));
        if(SIGNEDIN.equals("NO")) {
            llsignin.setVisibility(LinearLayout.VISIBLE);
        }
        else
        {
            llsignin.setVisibility(LinearLayout.GONE);
        }
        btn_acc_signinup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        lv_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(SIGNEDIN.equals("NO")) {
                    Toast.makeText(getApplicationContext(),"Sign in/Sign up to continue",Toast.LENGTH_SHORT).show();
                }
                else{
                    switch (position){
                        case 0:Intent intent=new Intent(AccountActivity.this,OrdersActivity.class);
                            startActivity(intent);
                            break;
                        case 1:intent=new Intent(AccountActivity.this,WishlistActivity.class);
                            startActivity(intent);
                            break;
                        case 2:intent=new Intent(AccountActivity.this,TrackActivity.class);
                            startActivity(intent);
                            break;
                        case 3:intent=new Intent(AccountActivity.this,EditAccountActivity.class);
                            startActivity(intent);
                            break;
                        case 4:intent=new Intent(AccountActivity.this,AddressActivity.class);
                            startActivity(intent);
                            break;
                        case 5://Logout Code
                            break;
                    }
                }

            }
        });
    }
}

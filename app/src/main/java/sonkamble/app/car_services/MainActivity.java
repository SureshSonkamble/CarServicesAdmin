package sonkamble.app.car_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sonkamble.app.car_services.Class.SessionManager;

public class MainActivity extends AppCompatActivity {
    LinearLayout lin_add_route,lin_add_area,lin_servie_center,lin_report;
    SessionManager sessionManager;
    SharedPreferences sp;
    String user, name, id;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        //------------------------User Session------------------------------------------
        Bundle b = getIntent().getExtras();
        try {
            user = b.getString("email");
            name = b.getString("name");
            id = b.getString("id");

            //Toast.makeText(getApplicationContext(), "Welcome-" + "\n" + user + "\n" + "id :" + id + "\n" + "Name :" + name, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }

        if (user == null) {
            // Toast.makeText(getApplicationContext(),"User Id Null...",Toast.LENGTH_LONG).show();
        } else {
            sp = this.getSharedPreferences("PI", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", user);
            editor.putString("name", name);
            editor.putString("id", id);
            editor.commit();
        }
        SharedPreferences sp = getSharedPreferences("STUD_DATA", MODE_PRIVATE);
        name = sp.getString("sname", "");
        id = sp.getString("sid", "");
      //  Toast.makeText(getApplicationContext(), "sfid :" + id + "\n" + "SfName :" + name, Toast.LENGTH_LONG).show();
        //------------------------Toolbar-------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        ImageView toolbar_img = (ImageView) toolbar.findViewById(R.id.img_logout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Car Services");

        toolbar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Warning");
                builder.setIcon(R.drawable.exit);
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logoutUser();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

            }
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        lin_add_route=(LinearLayout)findViewById(R.id.lin_add_route);
        lin_add_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Add_Route.class);
                startActivity(i);

            }
        });
        lin_add_area=(LinearLayout)findViewById(R.id.lin_add_area);
        lin_add_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Add_Area.class);
                startActivity(i);

            }
        });
        lin_report=(LinearLayout)findViewById(R.id.lin_report);
        lin_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Service_Center_List.class);
                startActivity(i);

            }
        });

        lin_servie_center=(LinearLayout)findViewById(R.id.lin_servie_center);
        lin_servie_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Add_Service_Center.class);
                startActivity(i);

            }
        });
    }
}

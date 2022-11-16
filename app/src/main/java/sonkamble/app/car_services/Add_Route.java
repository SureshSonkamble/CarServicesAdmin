package sonkamble.app.car_services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Route extends AppCompatActivity {
    Toolbar toolbar;
    Button  btn_add_route;
    ProgressBar progressBar;
    EditText edit_route_from,edit_route_to;
    String str_route_from,str_route_to;

    String str_spinner_category;
    String str_yojana_id,str_yojana;
    ProgressDialog progressDoalog;
    ArrayList<HashMap<String, String>> post_arryList;
    ArrayList<String>  yojana_lists;
   // String registration_url="http://192.168.29.252/WS/Attendance/insert_test.php?";
    String add_route_url="https://codingseekho.in/APP/CAR_SERVICES/add_route.php";
   // String yojana_list_url="https://codingseekho.in/APP/SARKARI_YOJANA/sp_yojana_list.php?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        //------------------------Toolbar-------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Add Route Name");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        progressBar=(ProgressBar)findViewById(R.id.pg);
        edit_route_from=(EditText)findViewById(R.id.edit_route_from);
        edit_route_to=(EditText)findViewById(R.id.edit_route_to);

        post_arryList = new ArrayList<HashMap<String, String>>();
        yojana_lists = new ArrayList<String>();

        btn_add_route=(Button)findViewById(R.id.btn_add_route);
        btn_add_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(Add_College_Details.this, "Student Details: \n"+"Name"+str_stud_name+"\n"+"Email: "+str_stud_email+"\n"+"PWD: "+str_stud_password+"\n"+"Mobile: "+str_stud_mobile+"\n"+"\n"+"Gender: "+str_gender+"\n"+"Stream: "+str_stream, Toast.LENGTH_SHORT).show();
                progressDoalog = new ProgressDialog(Add_Route.this);
                progressDoalog.setMessage("Adding....");
                progressDoalog.show();
                // progressbar.setVisibility(View.VISIBLE);

                str_route_from=edit_route_from.getText().toString();
                str_route_to=edit_route_to.getText().toString();


                StringRequest stringRequest=new StringRequest(Request.Method.POST, add_route_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Add_Route.this, "response:"+response, Toast.LENGTH_SHORT).show();
                        try {
                            if (response != null) {
                                // progressbar.setVisibility(View.INVISIBLE);
                                progressDoalog.dismiss();
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONObject postobject = jsonObject.getJSONObject("posts");

                                String status = postobject.getString("status");
                                //String client_status = postobject.getString("client_status");
                                if (status.equals("200")) {
                                    // clear_text();
                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                    Toast.makeText(getApplicationContext(), "Added Successfully..!!", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("404")) {
                                    // english_poemList.clear();
                                    Toast.makeText(getApplicationContext(), "Error:" + status, Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "No dat found ... please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                        error.printStackTrace();

                    }
                })
                {@Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param=new HashMap<String, String>();

                        param.put("route_from",str_route_from);
                        param.put("route_to",str_route_to);

                        return param;
                    }
                };
                MySingleton.getInstance(Add_Route.this).addToRequestque(stringRequest);
            }

        });
    }

    }

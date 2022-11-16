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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Add_Area extends AppCompatActivity {
    Toolbar toolbar;
    Button  btn_add_area;
    ProgressBar progressBar;
    EditText edt_area_name;
    String str_area_name;
    Spinner spinner_area;
    String str_spinner_category;
    String str_route_id,str_route;
    ProgressDialog progressDoalog;
    ArrayList<HashMap<String, String>> post_arryList;
    ArrayList<String>  yojana_lists;
   // String registration_url="http://192.168.29.252/WS/Attendance/insert_test.php?";
   String add_area_url="https://codingseekho.in/APP/CAR_SERVICES/add_area.php";
    String route_list_url="https://codingseekho.in/APP/CAR_SERVICES/all_route_list.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);
        //------------------------Toolbar-------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Add Area Name");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        progressBar=(ProgressBar)findViewById(R.id.pg);
        edt_area_name=(EditText)findViewById(R.id.edt_area_name);

        post_arryList = new ArrayList<HashMap<String, String>>();
        yojana_lists = new ArrayList<String>();
        load_route();
        //----------Gender--------------------------------------------------------
        spinner_area=(Spinner)findViewById(R.id.spinner_area);
        //=======================================================================================


        btn_add_area=(Button)findViewById(R.id.btn_add_area);
        btn_add_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(Add_College_Details.this, "Student Details: \n"+"Name"+str_stud_name+"\n"+"Email: "+str_stud_email+"\n"+"PWD: "+str_stud_password+"\n"+"Mobile: "+str_stud_mobile+"\n"+"\n"+"Gender: "+str_gender+"\n"+"Stream: "+str_stream, Toast.LENGTH_SHORT).show();
                progressDoalog = new ProgressDialog(Add_Area.this);
                progressDoalog.setMessage("Adding....");
                progressDoalog.show();
                // progressbar.setVisibility(View.VISIBLE);

                str_area_name=edt_area_name.getText().toString();


                StringRequest stringRequest=new StringRequest(Request.Method.POST, add_area_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Add_Area.this, "response:"+response, Toast.LENGTH_SHORT).show();
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

                        param.put("r_id",str_route_id);
                        param.put("area_name",str_area_name);
                        return param;
                    }
                };
                MySingleton.getInstance(Add_Area.this).addToRequestque(stringRequest);
            }

        });
    }

    void load_route()
    {
        progressDoalog = new ProgressDialog(Add_Area.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, route_list_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.VISIBLE);
                progressDoalog.dismiss();
                // Toast.makeText(getApplicationContext(),"Responce"+response,Toast.LENGTH_LONG).show();
                try
                {
                    if(response != null){
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject postobject = jsonObject.getJSONObject("posts");
                        String status = postobject.getString("status");
                        if (status.equals("200")) {
                            // Toast.makeText(getApplicationContext(),"Success:"+status,Toast.LENGTH_LONG).show();
                            JSONArray jsonArray=postobject.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.optJSONObject(i);
                                if (c != null) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    String  ID = c.getString("ID");
                                    String  FROM = c.getString("FROM");
                                    String  TO = c.getString("TO");

                                    map.put("ID", ID);
                                    map.put("FROM", FROM);
                                    map.put("TO", TO);
                                    post_arryList.add(map);
                                    yojana_lists.add(FROM+"<-->"+TO);
                                    //json_responce.setText(""+post_arryList);
                                }
                            }
                        }
                    }
                    spinner_area.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            yojana_lists));
                    spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            str_route_id = post_arryList.get(position).get("ID");
                            str_route = post_arryList.get(position).get("FROM")+""+post_arryList.get(position).get("TO");
                            Toast.makeText(Add_Area.this, ""+str_route, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(Add_Area.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MySingleton.getInstance(Add_Area.this).addToRequestque(jsonObjectRequest);
    }
}

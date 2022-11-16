package sonkamble.app.car_services;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Service_Center_List extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt_name;
    ArrayList<HashMap<String, String>> post_arryList;
    Button btn_operation_cancle,btn_update,btn_del;
    ProgressBar progressBar;
    AlertDialog dialog;

    ProgressDialog progressDoalog;
    vehical_recyclerAdapter demo_recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager_demo;
    private RecyclerView recyclerView_demo;
    String nm,id,ontime,offtime,offday,mob,adr,str_cat;

    EditText txt_email,txt_mob,txt_addr,txt_cname,txt_duration,txt_fees,txt_eligible,txt_url;

    String update_url="https://codingseekho.in/APP/CAR_SERVICES/update_test.php?";
    String del_url="https://codingseekho.in/APP/CAR_SERVICES/delete_test.php?";
    String service_center_url="https://codingseekho.in/APP/CAR_SERVICES/all_service_center_list.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Service Center List");

        progressBar=(ProgressBar)findViewById(R.id.pg);
        post_arryList = new ArrayList<HashMap<String, String>>();

        recyclerView_demo=(RecyclerView)findViewById(R.id.recycler_vehical);
        //--------for linear layout--------------
        layoutManager_demo = new LinearLayoutManager(Service_Center_List.this, RecyclerView.VERTICAL, false);
        recyclerView_demo.setLayoutManager(layoutManager_demo);
        //---------for grid layout--------------
        // recyclerView_demo.setLayoutManager(new GridLayoutManager(View_Complaint.this,2));

        //------------------------------------------
        demo_recyclerAdapter=new vehical_recyclerAdapter(Service_Center_List.this,post_arryList);
        recyclerView_demo.setAdapter(demo_recyclerAdapter);

        load_data();
        //------------------------------------------------------------------------------------------

    }

    public void load_data()
    {
        {   progressDoalog = new ProgressDialog(Service_Center_List.this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.show();

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, service_center_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressDoalog.dismiss();
                    //Toast.makeText(getApplicationContext(),"Responce"+response,Toast.LENGTH_LONG).show();
                    try
                    {
                        if(response != null){
                            progressBar.setVisibility(View.INVISIBLE);
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONObject postobject = jsonObject.getJSONObject("posts");
                            String status = postobject.getString("status");
                            if (status.equals("200")) {
                                post_arryList.clear();
                                // Toast.makeText(getApplicationContext(),"Success:"+status,Toast.LENGTH_LONG).show();
                                JSONArray jsonArray=postobject.getJSONArray("post");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.optJSONObject(i);
                                    if (c != null) {
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        String  SID = c.getString("SID");
                                        String  AID = c.getString("AID");
                                        String  RID = c.getString("RID");
                                        String  nm = c.getString("NAME");
                                        String mob=c.getString("MOBILE");
                                        String addr=c.getString("ADDR");
                                        String ontime=c.getString("ONTIME");
                                        String offtime=c.getString("OFFTIME");
                                        String offday=c.getString("OFFDAY");

                                        map.put("SID", SID);
                                        map.put("AID", AID);
                                        map.put("RID", RID);
                                        map.put("NAME", nm);
                                        map.put("MOBILE", mob);
                                        map.put("ADDR", addr);
                                        map.put("ONTIME", ontime);
                                        map.put("OFFTIME", offtime);
                                        map.put("OFFDAY", offday);

                                        post_arryList.add(map);

                                        //json_responce.setText(""+post_arryList);
                                    }
                                }
                            }
                        }
                    }catch (Exception e){}
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MySingleton.getInstance(Service_Center_List.this).addToRequestque(jsonObjectRequest);
        }
        if (demo_recyclerAdapter != null) {
            demo_recyclerAdapter.notifyDataSetChanged();

            System.out.println("Adapter " + demo_recyclerAdapter.toString());
        }
    }

    public class vehical_recyclerAdapter extends RecyclerView.Adapter<vehical_recyclerAdapter.DemoViewHolder>
    {
        Context context;
        ArrayList<HashMap<String, String>> img_list;

        public vehical_recyclerAdapter(Context context, ArrayList<HashMap<String, String>> quans_list) {
            this.img_list = quans_list;
            this.context = context;
        }

        @Override
        public DemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_list, parent, false);
            DemoViewHolder ViewHolder = new DemoViewHolder(view);
            return ViewHolder;
        }

        @Override
        public void onBindViewHolder(DemoViewHolder merchantViewHolder, final int position)
        {

            merchantViewHolder.txt_d1.setText(img_list.get(position).get("NAME"));
            merchantViewHolder.txt_d2.setText(img_list.get(position).get("MOBILE"));
            merchantViewHolder.txt_d3.setText(img_list.get(position).get("ADDR"));
            merchantViewHolder.txt_d4.setText(img_list.get(position).get("ONTIME"));
            merchantViewHolder.txt_d5.setText(img_list.get(position).get("OFFTIME"));
            merchantViewHolder.txt_d6.setText(img_list.get(position).get("OFFDAY"));

            merchantViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    id=img_list.get(position).get("SID");
                    nm=img_list.get(position).get("NAME");
                    nm=img_list.get(position).get("NAME");
                    mob=img_list.get(position).get("MOBILE");
                    adr=img_list.get(position).get("ADDR");
                    ontime=img_list.get(position).get("ONTIME");
                    offtime=img_list.get(position).get("OFFTIME");
                    offday=img_list.get(position).get("OFFDAY");

                    //msg=img_list.get(position).get("MSG");
                    //sts=img_list.get(position).get("STATUS");
                    //Toast.makeText(context, "STUD_ID: "+sid+"\n"+"CID"+cid, Toast.LENGTH_SHORT).show();
                    college_popup_form();

                }
            });
        }

        @Override
        public int getItemCount() {
            return img_list.size();
        }

        public class DemoViewHolder extends RecyclerView.ViewHolder
        {    LinearLayout lin;
             TextView txt_d1,txt_d2,txt_d3,txt_d4,txt_d5,txt_d6;
            public DemoViewHolder(View itemView) {
                super(itemView);
                this.lin = (LinearLayout) itemView.findViewById(R.id.lin);
                this.txt_d1 = (TextView) itemView.findViewById(R.id.txt_d1);
                this.txt_d2 = (TextView) itemView.findViewById(R.id.txt_d2);
                this.txt_d3 = (TextView) itemView.findViewById(R.id.txt_d3);
                this.txt_d4 = (TextView) itemView.findViewById(R.id.txt_d4);
                this.txt_d5 = (TextView) itemView.findViewById(R.id.txt_d5);
                this.txt_d6 = (TextView) itemView.findViewById(R.id.txt_d6);


            }
        }
    }

    public void college_popup_form() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.update_popup, null);

        txt_name = (EditText) alertLayout.findViewById(R.id.txt_name);
        txt_name.setText(nm);
        txt_mob = (EditText) alertLayout.findViewById(R.id.txt_mob);
        txt_mob.setText(mob);
        txt_addr = (EditText) alertLayout.findViewById(R.id.txt_addr);
        txt_addr.setText(adr);
        txt_cname = (EditText) alertLayout.findViewById(R.id.txt_cname);
        txt_cname.setText(ontime);
        txt_duration = (EditText) alertLayout.findViewById(R.id.txt_duration);
        txt_duration.setText(offtime);
        txt_fees = (EditText) alertLayout.findViewById(R.id.txt_fees);
        txt_fees.setText(offday);


        btn_operation_cancle = (Button) alertLayout.findViewById(R.id.btn_operation_cancle);
        btn_operation_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_del = (Button) alertLayout.findViewById(R.id.btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(Service_Center_List.this);
                progressDoalog.setMessage("Deleting....");
                progressDoalog.show();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, del_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Service_Center_List.this, "response:"+response, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "Deleted Successfully..!!", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("401")) {
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

                    param.put("s_id",id);

                    return param;
                }
                };

                MySingleton.getInstance(Service_Center_List.this).addToRequestque(stringRequest);

                dialog.dismiss();
            }
        });

        btn_update = (Button) alertLayout.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDoalog = new ProgressDialog(Service_Center_List.this);
                progressDoalog.setMessage("Updating....");
                progressDoalog.show();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Service_Center_List.this, "response:"+response, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "Updated Successfully..!!", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("401")) {
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
                  //  nm,id,ontime,offtime,offday,eml,mob,adr,str_cat
                    param.put("s_id",id);
                    param.put("service_name",txt_name.getText().toString());
                    param.put("service_mobile",txt_mob.getText().toString());
                    param.put("service_addr",txt_addr.getText().toString());
                    param.put("service_open_time",txt_cname.getText().toString());
                    param.put("service_close_time",txt_duration.getText().toString());
                    param.put("service_off_day",txt_fees.getText().toString());

                    return param;
                }
                };

                MySingleton.getInstance(Service_Center_List.this).addToRequestque(stringRequest);

                dialog.dismiss();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(Service_Center_List.this);
        alert.setView(alertLayout);

        dialog = alert.create();
        dialog.show();

    }
}

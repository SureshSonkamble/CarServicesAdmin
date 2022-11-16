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

public class Faq_List extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<HashMap<String, String>> post_arryList;
    Button btn_operation_cancle,btn_ok;
    ProgressBar progressBar;
    AlertDialog dialog;
    ProgressDialog progressDoalog;
    TextView edt_faq,edt_name,edt_mob;
    EditText edt_faq_ans;
    String str_faq,str_name,str_mob,str_faq_ans;
    vehical_recyclerAdapter demo_recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager_demo;
    private RecyclerView recyclerView_demo;
    String mob,nm,id,faq;
   // String faq_update_url="https://codingseekho.in/APP/COLLEGE_SELECTOR/update_stud_faq.php?";
    String faq_update_url="https://codingseekho.in/APP/SARKARI_YOJANA/update_user_faq.php?";
   // String faq_ans_url="https://codingseekho.in/APP/COLLEGE_SELECTOR/stud_faq_list.php?";
    String faq_ans_url="https://codingseekho.in/APP/SARKARI_YOJANA/user_faq_list.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("FAQ'S List");

        progressBar=(ProgressBar)findViewById(R.id.pg);
        post_arryList = new ArrayList<HashMap<String, String>>();

        recyclerView_demo=(RecyclerView)findViewById(R.id.recycler_vehical);
        //--------for linear layout--------------
        layoutManager_demo = new LinearLayoutManager(Faq_List.this, RecyclerView.VERTICAL, false);
        recyclerView_demo.setLayoutManager(layoutManager_demo);
        //---------for grid layout--------------
        // recyclerView_demo.setLayoutManager(new GridLayoutManager(View_Complaint.this,2));

        //------------------------------------------
        demo_recyclerAdapter=new vehical_recyclerAdapter(Faq_List.this,post_arryList);
        recyclerView_demo.setAdapter(demo_recyclerAdapter);

        load_data();

    }

    //http://localhost/Attendance/update_stud_faq.php?faq_ans=donation&id=1
    public void load_data()
    {
        {   progressDoalog = new ProgressDialog(Faq_List.this);
            progressDoalog.setMessage("Loading....");
            progressDoalog.show();

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, faq_ans_url, null, new Response.Listener<JSONObject>() {
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
                                        String  id = c.getString("ID");
                                        String  nm = c.getString("NAME");
                                        String  mob = c.getString("MOBILE");
                                        String  faq = c.getString("FAQ");


                                        map.put("ID", id);
                                        map.put("NAME", nm);
                                        map.put("MOBILE", mob);
                                        map.put("FAQ", faq);

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

            MySingleton.getInstance(Faq_List.this).addToRequestque(jsonObjectRequest);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_list, parent, false);
            DemoViewHolder ViewHolder = new DemoViewHolder(view);
            return ViewHolder;
        }

        @Override
        public void onBindViewHolder(DemoViewHolder merchantViewHolder, final int position)
        {

            merchantViewHolder.txt_d1.setText(img_list.get(position).get("NAME"));
            merchantViewHolder.txt_d2.setText(img_list.get(position).get("MOBILE"));
            merchantViewHolder.txt_d3.setText(img_list.get(position).get("FAQ"));

            merchantViewHolder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id=img_list.get(position).get("ID");
                    nm=img_list.get(position).get("NAME");
                    mob=img_list.get(position).get("MOBILE");
                    faq=img_list.get(position).get("FAQ");

                    faq_ans_popup_form();

                }
            });
        }

        @Override
        public int getItemCount() {
            return img_list.size();
        }

        public class DemoViewHolder extends RecyclerView.ViewHolder
        {    LinearLayout lin;
             TextView txt_d1,txt_d2,txt_d3;
            public DemoViewHolder(View itemView) {
                super(itemView);
                this.lin = (LinearLayout) itemView.findViewById(R.id.lin);
                this.txt_d1 = (TextView) itemView.findViewById(R.id.txt_d1);
                this.txt_d2 = (TextView) itemView.findViewById(R.id.txt_d2);
                this.txt_d3 = (TextView) itemView.findViewById(R.id.txt_d3);

            }
        }
    }

    public void faq_ans_popup_form() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.faq_popup, null);

        edt_name = (TextView) alertLayout.findViewById(R.id.edt_name);
        edt_mob = (TextView) alertLayout.findViewById(R.id.edt_mob);
        edt_faq = (TextView) alertLayout.findViewById(R.id.edt_faq);
        edt_faq_ans = (EditText) alertLayout.findViewById(R.id.edt_faq_ans);
        str_faq=edt_faq.getText().toString();
        str_name=edt_name.getText().toString();
        str_mob=edt_mob.getText().toString();

        edt_name.setText(nm);
        edt_mob.setText(mob);
        edt_faq.setText(faq);

        btn_operation_cancle = (Button) alertLayout.findViewById(R.id.btn_operation_cancle);
        btn_operation_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_ok = (Button) alertLayout.findViewById(R.id.btn_add);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_faq_ans=edt_faq_ans.getText().toString();
                progressDoalog = new ProgressDialog(Faq_List.this);
                progressDoalog.setMessage("Adding....");
                progressDoalog.show();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, faq_update_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Faq_List.this, "response:"+response, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "Success.!!", Toast.LENGTH_SHORT).show();
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

                    param.put("faq_ans",str_faq_ans);
                    param.put("id",id);

                    return param;
                }
                };

                MySingleton.getInstance(Faq_List.this).addToRequestque(stringRequest);

                dialog.dismiss();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(Faq_List.this);
        alert.setView(alertLayout);

        dialog = alert.create();
        dialog.show();

    }
}

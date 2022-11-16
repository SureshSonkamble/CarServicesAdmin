package sonkamble.app.car_services;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Add_Service_Center extends AppCompatActivity {

    Spinner spinner_route,spinner_area;
    String str_route,str_area,str_route_id,str_area_id;
    ImageView img_open_time,img_close_time;
    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/TEST_CAM_PIC";
    private int GALLERY = 1, CAMERA = 2;
    Bitmap bitmap;
    TextView txt_date;
    ProgressBar progressBar;
    Toolbar toolbar;
    ProgressDialog progressDoalog;
    Button btn_take_pic,btn_add_services;
    String URL= "https://codingseekho.in/APP/CAR_SERVICES/upload_service_center.php";
    private static final int PERMISSION_REQUEST_CODE = 1;
    ByteArrayOutputStream bytes;
    ArrayList<HashMap<String, String>> area_arryList;
    ArrayList<HashMap<String, String>> route_arryList;
    ArrayList<String>  area_lists;
    ArrayList<String>  route_lists;
    String route_list_url="https://codingseekho.in/APP/CAR_SERVICES/all_route_list.php?";
    String area_list_url="https://codingseekho.in/APP/CAR_SERVICES/all_area_list.php?rid=";
    EditText   edt_service_name,edt_service_mob,edt_service_addr,edt_open_time,edt_close_time,edt_service_off_day;
    String   str_service_name,str_service_mob,str_service_addr,str_open_time,str_close_time,str_service_off_day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services_center);

        //------------------------Toolbar-------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Add Car Services Center");

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        progressBar=(ProgressBar)findViewById(R.id.pg);
        spinner_route=(Spinner) findViewById(R.id.spinner_route);
        spinner_area=(Spinner) findViewById(R.id.spinner_area);
        edt_service_name=(EditText)findViewById(R.id.edt_service_name);
        edt_service_mob=(EditText)findViewById(R.id.edt_service_mob);
        edt_service_addr=(EditText)findViewById(R.id.edt_service_addr);
        edt_open_time=(EditText)findViewById(R.id.edt_open_time);
        edt_close_time=(EditText)findViewById(R.id.edt_close_time);
        edt_service_off_day=(EditText)findViewById(R.id.edt_service_off_day);

        btn_add_services = (Button) findViewById(R.id.btn_add_services);
        btn_take_pic = (Button) findViewById(R.id.btn_take_pic);
        imageview = (ImageView) findViewById(R.id.iv);
        img_open_time = (ImageView) findViewById(R.id.img_open_time);
        img_open_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add_Service_Center.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_open_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        img_close_time = (ImageView) findViewById(R.id.img_close_time);
        img_close_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Add_Service_Center.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_close_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        // Get Current Time
        route_arryList = new ArrayList<HashMap<String, String>>();
        area_arryList = new ArrayList<HashMap<String, String>>();
        area_lists = new ArrayList<String>();
        route_lists = new ArrayList<String>();


        btn_take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        btn_add_services=(Button)findViewById(R.id.btn_add_services);
        btn_add_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });
      load_route();


    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                check_permission();
                                //takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void check_permission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                takePhotoFromCamera();

            } else {
                requestPermission(); // Code for permission
            }
        } else {
            takePhotoFromCamera();
            Toast.makeText(Add_Service_Center.this, "Below 23 API Oriented Device No Permission ....", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Add_Service_Center.this, Manifest.permission.CAMERA);
        int result1 = ContextCompat.checkSelfPermission(Add_Service_Center.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(Add_Service_Center.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED&& result1 == PackageManager.PERMISSION_GRANTED&& result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Add_Service_Center.this, Manifest.permission.CAMERA)&& ActivityCompat.shouldShowRequestPermissionRationale(Add_Service_Center.this, Manifest.permission.READ_EXTERNAL_STORAGE)&& ActivityCompat.shouldShowRequestPermissionRationale(Add_Service_Center.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Add_Service_Center.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(Add_Service_Center.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Add_Service_Center.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(Add_Service_Center.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public String saveImage(Bitmap myBitmap) {
        bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

   /* public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }*/

    public String encode_img(Bitmap bm){

        byte[] imagebyte = bytes.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }
    private void UploadImage(){
        progressDoalog = new ProgressDialog(Add_Service_Center.this);
        progressDoalog.setMessage("Uploading please wait....");
        progressDoalog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDoalog.dismiss();
                String s = response.trim();

                if(s.equalsIgnoreCase("Loi")){
                    Toast.makeText(Add_Service_Center.this, "Loi", Toast.LENGTH_SHORT).show();
                }else{
                    refresh();
                    Toast.makeText(Add_Service_Center.this, "Success", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Add_Service_Center.this, error+"", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                 String title= edt_service_name.getText().toString();
                 title=title.replaceAll("\\s+","");
                 Log.d("title",title);
                // String date= txt_date.getText().toString();
               // String Time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String image = encode_img(bitmap);
                //  String image = ""+bitmap;

                str_service_mob= edt_service_mob.getText().toString();
                str_service_addr= edt_service_addr.getText().toString();
                str_open_time= edt_open_time.getText().toString();
                str_close_time= edt_close_time.getText().toString();
                str_service_off_day= edt_service_off_day.getText().toString();
                Map<String ,String> params = new HashMap<String,String>();
                params.put("a_id",str_area_id);
                params.put("r_id",str_route_id);
                params.put("IMG",image);
                params.put("service_name",title);
                params.put("service_mobile",str_service_mob);
                params.put("service_addr",str_service_addr);
                params.put("service_open_time",str_open_time);
                params.put("service_close_time",str_close_time);
                params.put("service_off_day",str_service_off_day);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void refresh()
    {
        Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(refresh);
        finish();
    }
    //=================Route from=====================
    void load_route()
    {
        progressDoalog = new ProgressDialog(Add_Service_Center.this);
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
                            route_arryList.clear();
                            route_lists.clear();
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
                                    route_arryList.add(map);
                                    route_lists.add(FROM+"<-->"+TO);
                                    //json_responce.setText(""+post_arryList);
                                }
                            }
                        }
                    }
                    spinner_route.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            route_lists));
                    spinner_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            str_route_id = route_arryList.get(position).get("ID");
                            str_route = route_arryList.get(position).get("FROM")+""+route_arryList.get(position).get("TO");
                            load_area(str_route_id);
                            Toast.makeText(Add_Service_Center.this, ""+str_route+""+str_route_id, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(Add_Service_Center.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MySingleton.getInstance(Add_Service_Center.this).addToRequestque(jsonObjectRequest);
    }
    //=================Area=================
    void load_area(String rid)
    {
        progressDoalog = new ProgressDialog(Add_Service_Center.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, area_list_url+rid, null, new Response.Listener<JSONObject>() {
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
                            area_arryList.clear();
                            area_lists.clear();
                            JSONArray jsonArray=postobject.getJSONArray("post");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.optJSONObject(i);
                                if (c != null) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    String  AID = c.getString("AID");
                                    String  RID = c.getString("RID");
                                    String  AREA = c.getString("AREA");

                                    map.put("AID", AID);
                                    map.put("RID", RID);
                                    map.put("AREA", AREA);
                                    area_arryList.add(map);
                                    area_lists.add(AREA);
                                    //json_responce.setText(""+post_arryList);
                                }
                            }
                        }
                    }
                    spinner_area.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            area_lists));
                    spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            str_area_id = area_arryList.get(position).get("AID");
                            str_area = area_arryList.get(position).get("AREA");

                            Toast.makeText(Add_Service_Center.this, ""+str_area+" "+str_area_id, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(Add_Service_Center.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MySingleton.getInstance(Add_Service_Center.this).addToRequestque(jsonObjectRequest);
    }
}
package prototype.com.irpsv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

public class Accident2 extends Activity implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private Bitmap bitmap;
    private String UPLOAD_URL ="https://irpsv.000webhostapp.com//upload_accident.php";
    private Button photoButton,sendButton;
    private EditText plateNo,placeOfAccident,timeOfAccident,descriptionAccident,name1,phone1,nameWitness,phoneWitness;
    private AutoCompleteTextView matSacco;
    String[] Matatu_Saccos;

    private String KEY_IMAGE = "image";
    private String KEY_MATSACCO = "matatu_sacco";
    private String KEY_PLATENO = "plate_number";
    private String KEY_PACCIDENT = "place_of_accident";
    private String KEY_TACCIDENT = "time_of_accident";
    private String KEY_DESCRIPTION = "description";
    private String KEY_NAME = "name";
    private String KEY_PHONE = "phone_no";
    private String KEY_NWITNESS = "witness_name";
    private String KEY_PWITNESS = "phone_witness";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident2);
        imageView = (ImageView)this.findViewById(R.id.imageView1);
        photoButton = (Button) findViewById(R.id.takepicture);
        sendButton = (Button)findViewById(R.id.send_accident_report);

        matSacco = (AutoCompleteTextView)findViewById(R.id.matatu_sacco);
        Matatu_Saccos=getResources().getStringArray(R.array.matatu_saccos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Matatu_Saccos);
        matSacco.setAdapter(adapter);


        plateNo = (EditText) findViewById(R.id.plate_number);
        placeOfAccident = (EditText) findViewById(R.id.place_of_accident);
        timeOfAccident = (EditText) findViewById(R.id.time_of_accident);
        descriptionAccident = (EditText) findViewById(R.id.description);
        name1 = (EditText) findViewById(R.id.name);
        phone1 = (EditText) findViewById(R.id.phone_no);
        nameWitness = (EditText) findViewById(R.id.witness_name);
        phoneWitness = (EditText) findViewById(R.id.phone_witness);
        photoButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        /*Button photoButton = (Button) this.findViewById(R.id.takepicture);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });  */
    }
    //take picture
    private void take_picture(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    //convert Bitmap to base64 String
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;}

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Accident2.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Accident2.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name details
                String matatu_sacco = matSacco.getText().toString().trim();
                String plate_number = plateNo.getText().toString().trim();
                String place_of_accident = placeOfAccident.getText().toString().trim();
                String time_of_accident = timeOfAccident.getText().toString().trim();
                String description = descriptionAccident.getText().toString().trim();
                String name = name1.getText().toString();

                String phone = phone1.getText().toString().trim();
                String witness_name = nameWitness.getText().toString().trim();
                String phone_witness = phoneWitness.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters

                params.put(KEY_MATSACCO, matatu_sacco);
                params.put(KEY_PLATENO, plate_number);
                params.put(KEY_PACCIDENT, place_of_accident);
                params.put(KEY_TACCIDENT, time_of_accident);
                params.put(KEY_DESCRIPTION, description);
                params.put(KEY_NAME, name);
                params.put(KEY_PHONE, phone);

                params.put(KEY_NWITNESS, witness_name);
                params.put(KEY_PWITNESS, phone_witness);
                params.put(KEY_IMAGE, image);
                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
          //  bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
           //  bitmap = imageView.getDrawingCache();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            bitmap = drawable.getBitmap();
        }

    }

    @Override
    public void onClick(View v) {
//call showFileChooser() method
        if (v == photoButton){
            take_picture();
        }
//call uploadImage() method
        if(v == sendButton){
            uploadImage();

            //clear fields
          /*  matSacco.setText("");
            plateNo.setText("");
            placeOfAccident.setText("");
            timeOfAccident.setText("");
            descriptionAccident.setText("");
            name1.setText("");
            phone1.setText("");
            nameWitness.setText("");
            phoneWitness.setText("");
            imageView.setImageDrawable(null);
        */
        }

    }
}

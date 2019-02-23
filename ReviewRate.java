package prototype.com.irpsv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReviewRate extends AppCompatActivity {
    Button Send;
    EditText PlateNumber,Review,Name,PhoneNumber;
    RatingBar ratingBar;
    AutoCompleteTextView MatSacco;
    String[] Matatu_Saccos;

    String server_url="https://irpsv.000webhostapp.com//send_review.php";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rate);

        MatSacco = (AutoCompleteTextView)findViewById(R.id.matatu_sacco);

        MatSacco = (AutoCompleteTextView)findViewById(R.id.matatu_sacco);
        Matatu_Saccos=getResources().getStringArray(R.array.matatu_saccos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Matatu_Saccos);
        MatSacco.setAdapter(adapter);


        PlateNumber = (EditText)findViewById(R.id.plate_number);
        Review=(EditText)findViewById(R.id.review);
        Name=(EditText)findViewById(R.id.name);
        PhoneNumber=(EditText)findViewById(R.id.phone_number);
        Send=(Button)findViewById(R.id.send_review);

        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        builder = new AlertDialog.Builder(ReviewRate.this);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  matatu_sacco,plate_number,review,name,phone_number;
                final float rating_bar;

                rating_bar=ratingBar.getRating();
                matatu_sacco = MatSacco.getText().toString();
                plate_number = PlateNumber.getText().toString();
                review = Review.getText().toString();
                name = Name.getText().toString();
                phone_number = PhoneNumber.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        builder.setTitle("Server Response");
                        builder.setMessage("Response" + response);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MatSacco.setText("");
                                PlateNumber.setText("");
                                ratingBar.setRating(0F);
                                Review.setText("");
                                Name.setText("");
                                PhoneNumber.setText("");
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewRate.this, "iko shida...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("matatu_sacco", matatu_sacco);
                        params.put("plate_number", plate_number);
                        params.put("rating_bar",String.valueOf(rating_bar));
                        params.put("review", review);
                        params.put("name", name);
                        params.put("phone_number", phone_number);

                        return params;
                    }
                };

                MySingleton.getInstance(ReviewRate.this).addTorequestqueue(stringRequest);
            }
        });

    }
}

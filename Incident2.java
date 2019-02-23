package prototype.com.irpsv;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;

        import java.sql.Date;
        import java.text.SimpleDateFormat;
        import java.util.HashMap;
        import java.util.Map;
public class Incident2 extends Activity {
    Button button;
    EditText PlateNumber,PlaceOfIncident,TimeOfIncident,IncidentReport,Name,PhoneNumber;
    AutoCompleteTextView MatSacco;
    String[] Matatu_Saccos;
    String server_url="https://irpsv.000webhostapp.com//incident_post.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident2);
        button = (Button)findViewById(R.id.incident_submit);
        MatSacco = (AutoCompleteTextView)findViewById(R.id.matatu_sacco);
        Matatu_Saccos=getResources().getStringArray(R.array.matatu_saccos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Matatu_Saccos);

        PlateNumber = (EditText)findViewById(R.id.plate_number);
        PlaceOfIncident = (EditText)findViewById(R.id.place_of_incident);
        TimeOfIncident = (EditText)findViewById(R.id.time_of_incident);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd HH:MM:SS" );
        MatSacco.setAdapter(adapter);

        IncidentReport = (EditText)findViewById(R.id.incident_report);
        Name = (EditText)findViewById(R.id.name);
        PhoneNumber = (EditText)findViewById(R.id.phone_number);


        builder = new AlertDialog.Builder(Incident2.this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, matatu_sacco,plate_number,place_of_incident,time_of_incident,incident_report,phone_number;
                matatu_sacco = MatSacco.getText().toString();
                plate_number = PlateNumber.getText().toString();
                place_of_incident = PlaceOfIncident.getText().toString();
                time_of_incident = TimeOfIncident.getText().toString();
                incident_report = IncidentReport.getText().toString();
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
                                PlaceOfIncident.setText("");
                                TimeOfIncident.setText("");
                                IncidentReport.setText("");
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
                        Toast.makeText(Incident2.this, "iko shida...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name", name);
                        params.put("matatu_sacco", matatu_sacco);
                        params.put("plate_number", plate_number);
                        params.put("place_of_incident", place_of_incident);
                        params.put("time_of_incident", time_of_incident);
                        params.put("incident_report", incident_report);
                        params.put("phone_number", phone_number);

                        return params;
                    }
                };

                MySingleton.getInstance(Incident2.this).addTorequestqueue(stringRequest);
            }
        });
    }
}

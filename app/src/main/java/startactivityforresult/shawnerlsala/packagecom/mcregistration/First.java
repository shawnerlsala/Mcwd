package startactivityforresult.shawnerlsala.packagecom.mcregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class First extends AppCompatActivity {
    private Button login;
    private EditText consumerCodeTxt;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        findViews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("customer_code");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkAlphaNumeric = "[a-zA-Z0-9]+";
             if (consumerCodeTxt.getText().toString().matches(checkAlphaNumeric)) {

                 Query queryRef = databaseReference.getRef().child(consumerCodeTxt.getText().toString());
                 queryRef.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.exists()){

                             final Query newQueryRef = firebaseDatabase.getReference("customers").child(consumerCodeTxt.getText().toString());
                             valueEventListener = new ValueEventListener() {
                                 @Override
                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                     if(dataSnapshot.exists()){
                                         Intent intent = new Intent(First.this, ResultActivity.class);
                                         intent.putExtra("customerCode", consumerCodeTxt.getText().toString());
                                         startActivity(intent);
                                         newQueryRef.removeEventListener(valueEventListener);
                                     }else{
                                         Intent intent = new Intent(First.this, LoginActivity.class);
                                         intent.putExtra("customerCode", consumerCodeTxt.getText().toString());
                                         startActivity(intent);
                                         newQueryRef.removeEventListener(valueEventListener);
                                     }
                                 }

                                 @Override
                                 public void onCancelled(DatabaseError databaseError) {

                                 }
                             };
                             newQueryRef.addValueEventListener(valueEventListener);
                             finish();
                         }else{
                                i++;
                             Toast.makeText(First.this, "Consumer code does not exists! Not Allowed to Register", Toast.LENGTH_SHORT).show();
                         //    finish();


                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });

             } else {
                 Toast.makeText(First.this, "Invalid Input!", Toast.LENGTH_SHORT).show();

             }


            }
        });
    }

    private void findViews() {
        login = (Button) findViewById(R.id.loginbtn);
        consumerCodeTxt = (EditText) findViewById(R.id.consumerCodeTxt);
    }

}



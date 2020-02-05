package com.creativeshare.registerme.activities_fragments.activities.activity_payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.creativeshare.registerme.R;
import com.creativeshare.registerme.preferences.Preferences;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.response.status.StatusResponse;


public class SuccessTransationActivity extends Activity {

    private TextView mTextView;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successtransaction);
    //  SetResulltOk();
    }

    @Override
    protected void onStart() {
        super.onStart();
     //   Log.e("hhhhhhh","lllllll");
        Intent intent = getIntent();
        StatusResponse status = (StatusResponse) intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
        TextView textView = (TextView)findViewById(R.id.text_payment_result);
        textView.setText(textView.getText() +" : " + status.getTrace());
        preferences= Preferences.getInstance();
        preferences.setIspaid(this,true);
        if(status.getAuth()!= null) {
            status.getAuth().getStatus();   // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
            status.getAuth().getAvs();      /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
            status.getAuth().getCode();     // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
            status.getAuth().getMessage();  // The authorisation or processing error message.
            status.getAuth().getCa_valid();
            status.getAuth().getCardcode(); // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
            status.getAuth().getCardlast4();// The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
            status.getAuth().getCvv();      /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
            status.getAuth().getTranref(); //The payment gateway transaction reference allocated to this request.
            Log.d("hany", status.getAuth().getTranref());
            status.getAuth().getCardfirst6(); // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version

//            setTransactionDetails(status.getAuth().getTranref(), status.getAuth().getCardlast4());
        }
    }

//    private void setTransactionDetails(String ref, String last4) {
//        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("telr", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("ref", ref);
//        editor.putString("last4", last4);
//        editor.commit();
//    }

    public void closeWindow(View view){
        this.finish();
    }
   void SetResulltOk(){

                       Intent data = new Intent();
                       data.putExtra("text", getResources().getString(R.string.suc));
                       setResult(RESULT_OK, data);
                       this.finish();

   }

}

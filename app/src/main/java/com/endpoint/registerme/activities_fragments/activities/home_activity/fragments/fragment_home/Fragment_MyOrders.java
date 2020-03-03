package com.endpoint.registerme.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.endpoint.registerme.R;
import com.endpoint.registerme.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.endpoint.registerme.adapter.Order_Adapter;
import com.endpoint.registerme.models.Order_Model;
import com.endpoint.registerme.models.ServicePriceModel;
import com.endpoint.registerme.models.UserModel;
import com.endpoint.registerme.preferences.Preferences;
import com.endpoint.registerme.remote.Api;
import com.endpoint.registerme.share.Common;
import com.endpoint.registerme.tags.Tags;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.request.payment.Address;
import com.telr.mobile.sdk.entity.request.payment.App;
import com.telr.mobile.sdk.entity.request.payment.Billing;
import com.telr.mobile.sdk.entity.request.payment.MobileRequest;
import com.telr.mobile.sdk.entity.request.payment.Name;
import com.telr.mobile.sdk.entity.request.payment.Tran;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_MyOrders extends Fragment {


    private Home_Activity activity;

    private ProgressBar progBar;
    private RecyclerView rec_depart;
    private List<Order_Model.Data> dataList;
    private Order_Adapter order_adapter;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout ll_no_store;
    private Preferences preferences;
    private ServicePriceModel servicepricemodel;

    private UserModel userModel;
    public static final String KEY = "gQq9M^TFrFs~FJPr";        // TODO: Insert your Key here
    public static final String STORE_ID = "22865";    // TODO: Insert your Store ID here
    public static final String EMAIL = "al-waafi8567@hotmail.com";     // TODO: Insert the customer email here

    private TextView tv_price;
    public static final boolean isSecurityEnabled = false;
    private int order_id;
    private Order_Model.Data orders;

    public Fragment_MyOrders() {
        // Required empty public constructor
    }

    public static Fragment_MyOrders newInstance() {

        return new Fragment_MyOrders();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myorders, container, false);
        intitview(view);
        if (userModel != null) {
            getOrders();
        }
        getserviceprive();

        return view;
    }

    public void sendMessage() {


        Intent intent = new Intent(activity, WebviewActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest());
        intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.endpoint.registerme.activities_fragments.activities.activity_payment.SuccessTransationActivity");
        intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.endpoint.registerme.activities_fragments.activities.activity_payment.FailedTransationActivity");
        intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);

        startActivityForResult(intent, 1);
    }

    private MobileRequest getMobileRequest() {
        MobileRequest mobile = new MobileRequest();
        mobile.setStore(STORE_ID);                       // Store ID
        mobile.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("com.endpoint.registerme");                          // Application installation ID
        app.setName("Create Email");                    // Application name
        app.setUser(userModel.getUser().getName());                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1");                         // Application version
        app.setSdk("123");
        mobile.setApp(app);
        Tran tran = new Tran();
        tran.setTest("0");                              // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("sale");                           /* Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */
        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API");         // Transaction description
        tran.setCurrency("SAR");                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        if (orders.getType() == 3) {
            tran.setAmount(servicepricemodel.getApply_job());
        } else if (orders.getType() == 2) {
            tran.setAmount(servicepricemodel.getCreate_email());
        } else {
            tran.setAmount(servicepricemodel.getCreate_cv());
        }
        // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //tran.setRef(???);                           // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
        tran.setLangauge("en");                        // (Optinal) default is en -> English
        mobile.setTran(tran);
        Billing billing = new Billing();
        Address address = new Address();
        address.setCity("Saudi");                       // City : the minimum required details for a transaction to be processed
        address.setCountry("SA");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Saudi");                     // Region
        address.setLine1(com.endpoint.registerme.models.Address.getAddress());                 // Street address – line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address);
        Name name = new Name();
        name.setFirst(userModel.getUser().getName());                          // Forename : the minimum required details for a transaction to be processed
        name.setLast(userModel.getUser().getName());                          // Surname : the minimum required details for a transaction to be processed
        name.setTitle(userModel.getUser().getName());                           // Title
        billing.setName(name);
        billing.setEmail(EMAIL);                 // TODO: Insert your email here : the minimum required details for a transaction to be processed.
        billing.setPhone(userModel.getUser().getPhone());                // Phone number, required if enabled in your merchant dashboard.
        mobile.setBilling(billing);
        return mobile;

    }

    /* This example used for continuous authority after using the first request, it used for recurring payment without asking the user to fill again the card details  */
    private void getserviceprive() {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
       /* if (uri == null&&userModel.getLogo()!=null) {
            uri = Uri.parse(Tags.IMAGE_URL + userModel.getLogo());

        }*/


        Api.getService(Tags.base_url).getserviceprice().enqueue(new Callback<ServicePriceModel>() {
            @Override
            public void onResponse(Call<ServicePriceModel> call, Response<ServicePriceModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {


                    // Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.suc));

                    // edt_pass.setText("");
                    //  updateprofile();
                    updatesrvice(response.body());
                } else {

                    try {
                        //  Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServicePriceModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    //  Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {

                }

            }
        });

    }

    private void updatesrvice(ServicePriceModel body) {
        this.servicepricemodel = body;
    }

    private void intitview(View view) {
        dataList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        progBar = view.findViewById(R.id.progBar2);
        ll_no_store = view.findViewById(R.id.ll_no_store);
        rec_depart = view.findViewById(R.id.rec_orders);

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        progBar.setVisibility(View.GONE);
        rec_depart.setDrawingCacheEnabled(true);
        rec_depart.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_depart.setItemViewCacheSize(25);


        gridLayoutManager = new GridLayoutManager(activity, 1);
        rec_depart.setLayoutManager(gridLayoutManager);
        order_adapter = new Order_Adapter(dataList, activity, this);
        rec_depart.setAdapter(order_adapter);
    }

    public void getOrders() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        // rec_sent.setVisibility(View.GONE);

        dataList.clear();
        order_adapter.notifyDataSetChanged();

        progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getorders(userModel.getUser().getId())
                .enqueue(new Callback<Order_Model>() {
                    @Override
                    public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                ll_no_store.setVisibility(View.GONE);
                                order_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                ll_no_store.setVisibility(View.VISIBLE);

                            }
                        } else {
                            ll_no_store.setVisibility(View.VISIBLE);

                            //  Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Order_Model> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


    public void pay(int layoutPosition) {
        order_id = dataList.get(layoutPosition).getId();
        orders = dataList.get(layoutPosition);
        if (com.endpoint.registerme.models.Address.getAddress() != null) {
            sendMessage();
        } else {
            Common.CreateSuccessDialog2(activity, activity.getResources().getString(R.string.fetch_your_location_first));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("gggggg", preferences.Ispaid(activity) + "" + order_id);
        if (order_id != 0) {
            if (preferences.Ispaid(activity)) {
                paid(1);
            } else {
                paid(0);
            }
        }
    }

    private void paid(int i) {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).setpaid(order_id, i).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (i == 1) {
                        Toast.makeText(activity, getResources().getString(R.string.sucess), Toast.LENGTH_LONG).show();
                        getOrders();

                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.order_sent), Toast.LENGTH_LONG).show();
                    }
                    //  activity.Displayorder();
                    // Common.CreateSignAlertDialog(home_activity, getResources().getString(R.string.sucess));

                } else {
                    Common.CreateSignAlertDialog(activity, getString(R.string.failed));

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

}

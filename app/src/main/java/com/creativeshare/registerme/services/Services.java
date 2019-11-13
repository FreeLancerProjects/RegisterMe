package com.creativeshare.registerme.services;


import com.creativeshare.registerme.models.AllInFo_Model;
import com.creativeshare.registerme.models.AppDataModel;
import com.creativeshare.registerme.models.BankDataModel;
import com.creativeshare.registerme.models.NotificationDataModel;
import com.creativeshare.registerme.models.Order_Model;
import com.creativeshare.registerme.models.Profile_Order_Model;
import com.creativeshare.registerme.models.Slider_Model;
import com.creativeshare.registerme.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Services {
    @FormUrlEncoded
    @POST("api/contact_us")
    Call<ResponseBody> contact_us(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("message") String message,
                                  @Field("subject") String subject

    );

    @GET("api/all_slider")
    Call<Slider_Model> get_slider();

    @GET("api/all_info")
    Call<AllInFo_Model> get_Info();

    @GET("api/aboutUs")
    Call<AppDataModel> getabout(
    );

    @GET("api/condtions")
    Call<AppDataModel> getterms(
    );

    @GET("api/all_banks")
    Call<BankDataModel> getBankAccount();

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> Signup(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("phone_code") String phone_code,
            @Field("email") String email,
            @Field("gender") int gender
    );

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> Signin(
            @Field("phone") String phone,
            @Field("phone_code") String phone_code
    );

    @FormUrlEncoded
    @POST("api/email_order")
    Call<ResponseBody> create_email(

            @Field("email") String email,
            @Field("password") String password,
            @Field("user_id") int user_id,
            @Field("type_id_fk") int type_id_fk);

    @FormUrlEncoded
    @POST("api/job_order_link")
    Call<ResponseBody> send_link(


            @Field("user_id") int user_id,
            @Field("lik_job") String lik_job);

    @FormUrlEncoded
    @POST("api/job_order_company")
    Call<ResponseBody> send_company(


            @Field("user_id") int user_id,
            @Field("campany_id_fk") int campany_id_fk);

    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> Logout(@Field("id") String id

    );

    @FormUrlEncoded
    @POST("api/my_orders")
    Call<Order_Model> getorders(

            @Field("user_id") int user_id


    );

    @FormUrlEncoded
    @POST("api/my_end_order")
    Call<Profile_Order_Model> getorders_type(

            @Field("user_id") int user_id,
            @Field("type") int type


    );

    @FormUrlEncoded
    @POST("api/my_notifications ")
    Call<NotificationDataModel> getnotifications(

            @Field("user_id") int user_id,
            @Field("page") int page


    );

    @Multipart
    @POST("api/user_image")
    Call<UserModel> editUserImage(@Part("user_id") RequestBody user_id,
                                  @Part MultipartBody.Part image);
}

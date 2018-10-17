package com.yudapratama.contact_apps_jenius_test.res.group;

import android.support.annotation.Nullable;

import com.yudapratama.contact_apps_jenius_test.res.response.ContactDetailResponse;
import com.yudapratama.contact_apps_jenius_test.res.response.ContactResponse;
import com.yudapratama.contact_apps_jenius_test.res.response.StatusResponse;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface ContactGroup {

    @POST("contact")
    @Multipart
    Observable<StatusResponse> addContact(@Field("firstName") String firstname,
                                          @Field("lastName") String lastname,
                                          @Field("age") int age,
                                          @Part @Nullable MultipartBody.Part photo);

    @GET("contact")
    Observable<ContactResponse> getAllContact();

    @GET("contact/{id}")
    Observable<ContactDetailResponse> getContactDetail(@Path(value = "id") String id);

//    @GET("contact")
//    @Headers("X-Requested-With:XMLHttpRequest")
//    Observable<ContactResponse> getAllContact(@NonNull @Query("page") Integer page);
}

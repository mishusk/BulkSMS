package bulksms.tdd.tddbulksms.retrofit;

import bulksms.tdd.tddbulksms.model.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by y34h1a on 3/16/17.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("addnumber.php")
    Call<ServerResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );
}

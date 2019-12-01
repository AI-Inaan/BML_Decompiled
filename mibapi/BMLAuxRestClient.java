package mv.com.bml.mibapi;

import java.util.ArrayList;
import mv.com.bml.mibapi.models.misc.ExchangeRateResponse;
import mv.com.bml.mibapi.models.misc.LocationData;
import mv.com.bml.mibapi.models.misc.ScamData;
import retrofit.http.GET;
import retrofit.http.Headers;

public interface BMLAuxRestClient {
    public static final String AUX_API_KEY = "58d91da955996b00018cdd70e0ea1ab222a64fe846e0ec5cfb2d9cd1";

    @Headers({"Authorization:58d91da955996b00018cdd70e0ea1ab222a64fe846e0ec5cfb2d9cd1"})
    @GET("/bms/auxiliary/v2/service/rates")
    ExchangeRateResponse getExchangeData();

    @Headers({"Authorization:58d91da955996b00018cdd70e0ea1ab222a64fe846e0ec5cfb2d9cd1"})
    @GET("/bms/auxiliary/v2/service/locations")
    ArrayList<LocationData> getLocationData();

    @Headers({"Authorization:58d91da955996b00018cdd70e0ea1ab222a64fe846e0ec5cfb2d9cd1"})
    @GET("/bms/auxiliary/v2/service/scam/numbers")
    ScamData getScamGuardBlacklist();
}

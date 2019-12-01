package mv.com.bml.mibapi;

import java.util.ArrayList;
import mv.com.bml.mibapi.models.accounts.AccountCASA;
import mv.com.bml.mibapi.models.accounts.AccountCardList;
import mv.com.bml.mibapi.models.accounts.AccountLoanDetails;
import mv.com.bml.mibapi.models.accounts.AccountSummary;
import mv.com.bml.mibapi.models.auth.AuthResponse;
import mv.com.bml.mibapi.models.auth.FirstTimeCheck;
import mv.com.bml.mibapi.models.auth.UsernamePojo;
import mv.com.bml.mibapi.models.payments.BeneficiaryInfo;
import mv.com.bml.mibapi.models.payments.Currency;
import mv.com.bml.mibapi.models.payments.MerchantInfoList;
import mv.com.bml.mibapi.models.payments.OtherBank;
import mv.com.bml.mibapi.models.payments.Payment;
import mv.com.bml.mibapi.models.payments.Transfer;
import mv.com.bml.mibapi.models.payments.contacts.CreateBen;
import mv.com.bml.mibapi.models.payments.contacts.ShelfList;
import mv.com.bml.mibapi.models.payments.mobile.RechargeDenominations;
import mv.com.bml.mibapi.models.session.SessionParameters;
import mv.com.bml.mibapi.models.transactions.LoanScheduleList;
import mv.com.bml.mibapi.models.transactions.Txhistory;
import mv.com.bml.mibapi.models.transactions.TxhistoryLocked;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

public interface BMLRestClient {
    @POST("/m/resources/beneficiary/{op}/")
    @Multipart
    void createBeneficiary(@Path("op") String str, @Part("payLoad") CreateBen createBen, @Part("uploadImage1") String str2, @Part("uploadImage1_delete") boolean z, Callback<BeneficiaryInfo> callback);

    @POST("/m/resources/beneficiary/delete/{ben_id}")
    void deleteBeneficiary(@Path("ben_id") String str, @Body BeneficiaryInfo beneficiaryInfo, Callback<BeneficiaryInfo> callback);

    @FormUrlEncoded
    @POST("/m/login")
    void doLogin(@Field("j_username") String str, @Field("j_password") String str2, Callback<AuthResponse> callback);

    @Headers({"_2FAServiceRequest:true", "Accept:application/json, text/javascript, */*; q=0.01", "Content-Type:application/json"})
    @POST("/m/resources/payment/doPayment")
    void doPayment(@Header("2FA-Command") String str, @Header("2FA-Supp-Data") String str2, @Header("Authorization") String str3, @Body Payment payment, Callback<Payment> callback);

    @Headers({"_2FAServiceRequest:true", "Accept:application/json, text/javascript, */*; q=0.01", "Content-Type:application/json"})
    @POST("/m/resources/transfer/doTransfer")
    void doTransfer(@Header("2FA-Command") String str, @Header("2FA-Supp-Data") String str2, @Header("Authorization") String str3, @Body Transfer transfer, Callback<Transfer> callback);

    @GET("/m/resources/accountsummary")
    void getAccountSummary(Callback<AccountSummary> callback);

    @GET("/m/resources/beneficiary/queryAll")
    void getBeneficiaryList(Callback<ArrayList<BeneficiaryInfo>> callback);

    @GET("/m/resources/casaAccount/acctNo/{acc_num}")
    void getCASAAccount(@Path("acc_num") String str, Callback<AccountCASA> callback);

    @GET("/m/resources/cardAccount/creditCardList")
    void getCardList(Callback<AccountCardList> callback);

    @GET("/m/resources/txhistory/card/{account_num}/billingstatement")
    void getCardTxHistory(@Path("account_num") String str, @Query("primaryCardNo") String str2, @Query("type") String str3, Callback<Txhistory> callback);

    @GET("/m/resources/currency")
    void getCurrencyList(Callback<ArrayList<Currency>> callback);

    @GET("/m/resources/beneficiary/findAllDomesticBankFiltered")
    void getDomesticBankList(Callback<ArrayList<OtherBank>> callback);

    @POST("/m/resources/predashboard/firstTimeLogin/checkFirstTimeLogin")
    void getFirstTimeLogin(@Body UsernamePojo usernamePojo, Callback<FirstTimeCheck> callback);

    @GET("/m/resources/loanacctdetails/loanno/{acc_num}")
    void getLoanAccount(@Path("acc_num") String str, Callback<AccountLoanDetails> callback);

    @GET("/m/resources/loanrepaymentschedule/loanno/{account_num}/?_search=false&nd=&rows=20&sidx&sord=asc")
    void getLoanSchedule(@Path("account_num") String str, @Query("page") int i, Callback<LoanScheduleList> callback);

    @GET("/m/resources/merchantEnrol/findAll")
    void getMerchantList(Callback<MerchantInfoList> callback);

    @GET("/m/resources/security/otpchannels")
    void getOTPChannels(Callback<ArrayList<String>> callback);

    @GET("/m/resources/mobileRecharge/findByMerchant")
    void getRechargeDenominations(@Query("merchantId") String str, Callback<ArrayList<RechargeDenominations>> callback);

    @GET("/m/resources/sessionparameters")
    void getSessionParameters(Callback<SessionParameters> callback);

    @GET("/m/resources/shelf/findAll?type=BEN&withDefaultYn=n")
    void getShelfList(Callback<ShelfList> callback);

    @GET("/history/pending/{account}")
    void getTxHistorLocked(@Path("account") String str, Callback<TxhistoryLocked> callback);

    @GET("/m/resources/txhistory/acct")
    void getTxHistory(@Query("filter") String str, @Query("period") String str2, @Query("startDate") String str3, @Query("endDate") String str4, Callback<Txhistory> callback);
}

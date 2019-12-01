package mv.com.bml.mibapi.models.auth;

public class AuthRequest {
    public String j_password;
    public String j_username;

    public AuthRequest(String str, String str2) {
        this.j_username = str;
        this.j_password = str2;
    }
}

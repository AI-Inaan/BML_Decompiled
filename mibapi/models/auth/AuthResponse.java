package mv.com.bml.mibapi.models.auth;

public class AuthResponse {
    public boolean authenticated;
    public String message;
    public String targetUrl;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthResponse<authenticated: ");
        sb.append(this.authenticated);
        sb.append(" message: ");
        sb.append(this.message);
        sb.append(">");
        return sb.toString();
    }
}

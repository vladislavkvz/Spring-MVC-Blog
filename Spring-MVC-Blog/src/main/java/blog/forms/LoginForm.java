package blog.forms;

import javax.validation.constraints.Size;

public class LoginForm {

    @Size(min=3,max=25,message="Username should be between 3 and 15 symbols!")
    private String username;

    @Size(min=6,max=30,message="password should be between 6 and 30 symbols!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

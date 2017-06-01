package blog.forms;

import javax.validation.constraints.Size;

public class RegisterForm {

    @Size(min=3,max=25,message="Username should be between 3 and 15 symbols!")
    private String username;

    @Size(min=10,max=100,message="Fullname should be between 10 and 100 symbols!")
    private String fullname;

    @Size(min=6,max=30,message="password should be between 6 and 30 symbols!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

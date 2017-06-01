package blog.forms;

import javax.validation.constraints.Size;

public class CreatePostForm {

    @Size(min=3,max=100,message="Title should be between 3 and 100 symbols!")
    private String title;

    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

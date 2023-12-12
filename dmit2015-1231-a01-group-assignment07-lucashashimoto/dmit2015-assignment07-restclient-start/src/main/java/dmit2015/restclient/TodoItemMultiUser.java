package dmit2015.restclient;

import lombok.Data;

@Data
public class TodoItemMultiUser {

    private Long id;

    private String name;

    private boolean complete;

    private int version;

}

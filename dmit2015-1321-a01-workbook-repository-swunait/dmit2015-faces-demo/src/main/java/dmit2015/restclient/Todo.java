package dmit2015.restclient;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Todo {

    private String key;

    private String task;

    private boolean done;

    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();
}

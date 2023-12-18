package cn.edu.seu.historycontest.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SubmitRequest {

    @NotNull
    private List<Integer> choice;
    @NotNull
    private List<Integer> judge;

}

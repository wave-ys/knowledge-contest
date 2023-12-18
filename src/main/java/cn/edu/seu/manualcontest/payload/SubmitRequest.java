package cn.edu.seu.manualcontest.payload;

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

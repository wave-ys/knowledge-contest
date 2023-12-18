package cn.edu.seu.historycontest.payload;

import lombok.Data;

import java.util.List;

@Data
public class GetPageResponse {

    private Long total;
    private List<?> list;

}

package cn.edu.seu.manualcontest.payload;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageResponse<E> {

    private Long total;
    private List<E> list;

    public static <T> PageResponse<T> ofPage(Page<T> page) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(page.getTotal());
        pageResponse.setList(page.getRecords());
        return pageResponse;
    }

}

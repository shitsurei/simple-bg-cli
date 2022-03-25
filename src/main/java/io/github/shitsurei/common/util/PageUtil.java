package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.vo.system.BasePageVO;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/18 18:15
 */
public class PageUtil {
    /**
     * 自动分页泛型对象
     *
     * @param page
     * @param dataList
     * @return
     */
    public static <T, K> BasePageVO<T> buildPageVO(Page<K> page, List<T> dataList) {
        return BasePageVO.<T>builder()
                .totalPage(page.getTotalPages())
                .totalElement((int) page.getTotalElements())
                .dataList(dataList)
                .build();
    }

    /**
     * 手动分页泛型对象（每页条数少于1时直接返回全部数据）
     *
     * @param dataList
     * @param pageSize
     * @param currentPage
     * @return
     */
    public static <T> BasePageVO<T> buildPageVO(List<T> dataList, int pageSize, int currentPage) {
        if (currentPage < 1) {
            throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, "当前页参数不能小于1");
        }
        if (CollectionUtils.isEmpty(dataList)) {
            return BasePageVO.<T>builder()
                    .totalPage(0)
                    .totalElement(0)
                    .dataList(Lists.newArrayList())
                    .build();
        }
        if (pageSize < 1) {
            return BasePageVO.<T>builder()
                    .totalPage(1)
                    .totalElement(dataList.size())
                    .dataList(dataList)
                    .build();
        }
        int totalPage = (int) Math.ceil(dataList.size() / (double) pageSize);
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(currentPage * pageSize, dataList.size());
        return BasePageVO.<T>builder()
                .totalPage(totalPage)
                .totalElement(dataList.size())
                .dataList(dataList.subList(startIndex, endIndex))
                .build();
    }
}

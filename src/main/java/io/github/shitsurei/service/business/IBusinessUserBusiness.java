package io.github.shitsurei.service.business;

import io.github.shitsurei.dao.pojo.dto.business.UserModifyDTO;
import io.github.shitsurei.dao.pojo.po.business.BusinessUser;
import io.github.shitsurei.dao.pojo.vo.business.BusinessUserVO;

/**
 * @author zgr
 * @Description 业务用户业务层
 * @createTime 2022年02月26日 20:30:00
 */
public interface IBusinessUserBusiness {

    /**
     * 根据id获取业务用户（带校验）
     *
     * @param businessUserId
     * @return
     */
    BusinessUser checkBusinessUserById(String businessUserId);

    /**
     * 更新用户信息
     *
     * @param userModifyDTO
     * @return
     */
    boolean update(UserModifyDTO userModifyDTO);

    /**
     * 获取登录用户详情
     *
     * @return
     */
    BusinessUserVO getLoginUserDetail();

    /**
     * 实体转换VO
     *
     * @param businessUser
     * @return
     */
    BusinessUserVO transVO(BusinessUser businessUser);
}

package io.github.shitsurei.service.system;

import io.github.shitsurei.dao.enumerate.system.DataOperate;
import io.github.shitsurei.dao.pojo.po.system.SystemFile;
import io.github.shitsurei.dao.pojo.po.system.SystemFileConfig;
import io.github.shitsurei.dao.pojo.vo.system.FileConfigVO;
import io.github.shitsurei.dao.pojo.vo.system.FileVO;
import io.github.shitsurei.dao.repository.system.SystemFileConfigRepository;
import io.github.shitsurei.dao.repository.business.SystemFileRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统文件业务类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/7 11:37
 */
public interface ISystemFileBusiness {
    /**
     * 查询文件配置列表
     *
     * @return
     */
    List<FileConfigVO> configList();

    /**
     * 检查上传文件是否合法，并组装文件实体类
     *
     * @param uploadFiles
     * @param configCode
     * @param businessId
     * @return
     */
    List<SystemFile> checkFileValid(ArrayList<MultipartFile> uploadFiles, String configCode, String businessId);

    /**
     * 存储文件
     *
     * @param systemFiles 文件实体
     * @param uploadFiles 文件内容
     * @return
     */
    List<String> save(List<SystemFile> systemFiles, ArrayList<MultipartFile> uploadFiles);

    /**
     * 根据绑定的业务主键获取文件列表
     *
     * @param bindIdList
     * @return
     */
    List<SystemFile> rawFileList(List<String> bindIdList);

    /**
     * 根据绑定的业务主键获取文件列表
     *
     * @param businessId
     * @return
     */
    List<FileVO> fileList(String businessId);

    /**
     * 根据绑定的业务主键列表获取文件列表
     *
     * @param businessIdList
     * @return
     */
    List<FileVO> fileList(List<String> businessIdList);

    /**
     * 通过主键查询文件
     *
     * @param fileId
     * @return
     */
    SystemFile checkFileById(String fileId);

    /**
     * 校验用户是否具备操作权限
     *
     * @param file
     * @param operate
     * @return
     */
    boolean checkOperateValid(SystemFile file, DataOperate operate);

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    boolean delete(SystemFile file);

    /**
     * 输出文件到前端
     *
     * @param file
     * @param request
     * @param response
     */
    void output(SystemFile file, HttpServletRequest request, HttpServletResponse response);

    /**
     * 新增文件配置
     *
     * @param fileConfig
     * @return
     */
    boolean createFileConfig(SystemFileConfig fileConfig);

    /**
     * 修改文件配置
     *
     * @param fileConfig
     * @return
     */
    boolean updateFileConfig(SystemFileConfig fileConfig);

    /**
     * 删除文件配置
     *
     * @param fileCode
     * @return
     */
    boolean deleteFileConfig(String fileCode);

    /**
     * 文件换绑业务数据
     *
     * @param systemFileId
     * @param bindId
     * @return
     */
    boolean bind(String systemFileId, String bindId);

    /**
     * 获取系统文件（带校验）
     *
     * @param userProfileId
     * @return
     */
    SystemFile checkSystemFileById(String userProfileId);

    /**
     * 获取系统文件持久层
     *
     * @return
     */
    SystemFileRepository getDao();

    /**
     * 获取系统文件配置持久层
     *
     * @return
     */
    SystemFileConfigRepository getConfigDao();
}

package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.lrs.common.constant.Result;
import com.lrs.common.dto.PageDTO;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  ${table.comment!} 服务类
 * </p>
 *
 * @author rstyro
 * @since ${.now?date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
    public Result getList(PageDTO dto) throws  Exception;
    public Result add(${entity} item, HttpSession session) throws  Exception;
    public Result edit(${entity} item, HttpSession session) throws  Exception;
    public Result del(Long id, HttpSession session) throws  Exception;
    public Result getDetail(Long id) throws  Exception;
}

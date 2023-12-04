package ${package.Service};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lrs.core.system.dto.BaseDto;
import ${package.Entity}.${entity};
import java.util.List;


/**
 * <p>
 *  ${table.comment!} 服务类
 * </p>
 *
 * @author rstyro
 * @since ${.now?date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    Page<${entity}> getPage(Page page, BaseDto dto);
    boolean add(${entity} item);
    boolean edit(${entity} item);
    boolean del(Long id);
    boolean batchDel(List<Long> ids);
}

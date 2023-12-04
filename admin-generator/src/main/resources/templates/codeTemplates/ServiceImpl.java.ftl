package ${package.ServiceImpl};


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrs.core.system.dto.BaseDto;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName}{


    @Override
    public Page<${entity}> getPage(Page page, BaseDto dto) {
        LambdaQueryWrapper<${entity}> queryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(dto.getKeyword())) {
            queryWrapper.like(${entity}::getRemark, dto.getKeyword());
        }
        queryWrapper.orderByAsc(${entity}::getId);
            return page(page, queryWrapper);
        }

        @Override
        public boolean add(${entity} item) {
            return save(item);
        }

        @Override
        public boolean edit(${entity} item) {
            return updateById(item);
        }

        @Override
        public boolean del(Long id) {
            return removeById(id);
        }

        @Override
        public boolean batchDel(List<Long> ids) {
            return removeBatchByIds(ids);
         }

}

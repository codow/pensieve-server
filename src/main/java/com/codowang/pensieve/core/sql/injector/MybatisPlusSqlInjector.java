package com.codowang.pensieve.core.sql.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.jeffreyning.mybatisplus.base.DeleteByMultiIdMethod;
import com.github.jeffreyning.mybatisplus.base.UpdateByMultiIdMethod;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 使用盈天自己的查询方法替代Mybatis-Plus和MybatisPlus-Plus的查询方法
 * 使支持sql子查询的方式格式化数据，增加对虚拟字段查询的支持
 *
 * @author wangyb
 * @since 2022-07-07
 */
public class MybatisPlusSqlInjector extends AbstractSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        Stream.Builder<AbstractMethod> builder = Stream.<AbstractMethod>builder()
                .add(new Insert())
                .add(new Delete())
                .add(new DeleteByMap())
                .add(new Update())
                .add(new com.codowang.pensieve.core.sql.method.SelectByMap())
                .add(new SelectCount())
                .add(new com.codowang.pensieve.core.sql.method.SelectMaps())
                .add(new com.codowang.pensieve.core.sql.method.SelectMapsPage())
                .add(new com.codowang.pensieve.core.sql.method.SelectObjs())
                .add(new com.codowang.pensieve.core.sql.method.SelectList())
                .add(new com.codowang.pensieve.core.sql.method.SelectPage());
        if (tableInfo.havePK()) {
            builder.add(new DeleteById())
                    .add(new DeleteBatchByIds())
                    .add(new UpdateById())
                    .add(new com.codowang.pensieve.core.sql.method.UpdateFromMapById())
                    // 使用盈天的selectById替代Mybatis-plus自带的selectById
                    .add(new com.codowang.pensieve.core.sql.method.SelectById())
                    .add(new com.codowang.pensieve.core.sql.method.SelectBatchByIds());
        } else {
            logger.warn(String.format("%s ,Not found @TableId annotation, Cannot use Mybatis-Plus 'xxById' Method.",
                    tableInfo.getEntityType()));
        }
        // 增加对多主键的支持
        builder.add(new com.codowang.pensieve.core.sql.method.SelectByMultiIdMethod());
        builder.add(new UpdateByMultiIdMethod());
        builder.add(new com.codowang.pensieve.core.sql.method.UpdateFromMapByMultiId());
        builder.add(new DeleteByMultiIdMethod());
        return builder.build().collect(toList());
    }
}

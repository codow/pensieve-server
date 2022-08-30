package ${package.ServiceImpl};

<#assign primaryKeyCount = 0 />
<#if table.havePrimaryKey>
 <#list table.fields as field>
  <#if field.keyFlag>
   <#assign primaryKeyCount = primaryKeyCount + 1 />
  </#if>
 </#list>
</#if>
<#assign multiplePrimaryKey = (primaryKeyCount > 1) />
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
<#if multiplePrimaryKey>
import com.codowang.pensieve.core.service.MppServiceImpl;
<#else>
import ${superServiceImplClassPackage};
</#if>
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if multiplePrimaryKey>
public class ${table.serviceImplName} extends ElasticMppServiceImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

}
<#elseif kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

}
</#if>

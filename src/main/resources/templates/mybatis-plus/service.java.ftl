package ${package.Service};

import ${package.Entity}.${entity};
<#assign primaryKeyCount = 0 />
<#if table.havePrimaryKey>
 <#list table.fields as field>
  <#if field.keyFlag>
   <#assign primaryKeyCount = primaryKeyCount + 1 />
  </#if>
 </#list>
</#if>
<#assign multiplePrimaryKey = (primaryKeyCount > 1) />
<#if multiplePrimaryKey>
import com.codowang.pensieve.core.service.IMppService;
<#else>
import ${superServiceClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if multiplePrimaryKey>
public interface ${table.serviceName} extends IElasticMppService<${entity}> {

}
<#elseif kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

}
</#if>

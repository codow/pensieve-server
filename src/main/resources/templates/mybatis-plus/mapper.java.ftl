package ${package.Mapper};

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
<#if multiplePrimaryKey>
import com.codowang.pensieve.core.sql.mapper.IMppBaseMapper;
<#else>
import ${superMapperClassPackage};
</#if>
<#if mapperAnnotationClass??>
import ${mapperAnnotationClass.name};
</#if>

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if mapperAnnotationClass??>
@${mapperAnnotationClass.simpleName}
</#if>
<#if multiplePrimaryKey>
public interface ${table.mapperName} extends IElasticMppBaseMapper<${entity}> {

}
<#elseif kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>

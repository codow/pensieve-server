package ${package.Controller};

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
<#if springdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif swagger>
import io.swagger.annotations.ApiOperation;
</#if>
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
<#list controllerImports as item>
import ${item};
</#list>
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if springdoc>
@Schema(name = "${table.comment!table.name}", description = "$!{table.comment}")
<#elseif swagger>
@ApiOperation(value = "${table.comment!table.name}前端控制器", notes = "${table.comment!}")
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/${controllerMapping}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private final ${table.serviceName} service;

    public ${table.controllerName} (${table.serviceName} service) {
        this.service = service;
    }

    <#if springdoc>
    @Schema(description = "新增一条记录")
    <#elseif swagger>
    @ApiOperation("新增一条记录")
    <#else>
    /**
     * 新增一条记录
     */
    </#if>
    @BaseResetDataResult
    @PostMapping("/insert")
    public Object insert(@RequestBody ${entity} entity) {
        service.save(entity);
        <#if onlyPrimaryKey>
        return entity.get${primaryField.capitalName}().toString();
        <#else>
        return true;
        </#if>
    }

    <#if onlyPrimaryKey>
        <#if springdoc>
    @Schema(description = "根据ID更新当前行数据")
        <#elseif swagger>
    @ApiOperation("根据ID更新当前行数据")
        <#else>
    /**
     * 根据ID更新当前行数据
     */
        </#if>
    @BaseResetDataResult
    @PutMapping("/update/{id}")
    public Object update(@PathVariable ${primaryField.propertyType} id, @RequestBody Map${r"<"}String, Object> params) {
        ${entity} entity = new ${entity}();
        // 设置主键
        entity.set${primaryField.capitalName}(id);
        return service.updateFromMapById(entity, params);
    }

        <#if hasLogicDeletedField?? && hasLogicDeletedField>
            <#if springdoc>
    @Schema(description = "根据ID逻辑删除当前行数据")
            <#elseif swagger>
    @ApiOperation("根据ID逻辑删除当前行数据")
            <#else>
    /**
     * 根据ID逻辑删除当前行数据
     */
            </#if>
    @BaseResetDataResult
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable ${primaryField.propertyType} id) {
        ${entity} entity = new ${entity}();
        entity.set${primaryField.capitalName}(id);
        entity.setIsDeleted(1);
        return service.updateById(entity);
    }

            <#if springdoc>
    @Schema(description = "根据ID物理删除当前行数据")
            <#elseif swagger>
    @ApiOperation("根据ID物理删除当前行数据")
            <#else>
    /**
     * 根据ID物理删除当前行数据
     */
            </#if>
    @BaseResetDataResult
    @DeleteMapping("/destroy/{id}")
    public Object destroy(@PathVariable ${primaryField.propertyType} id) {
        return service.removeById(id);
    }
        <#else>
            <#if springdoc>
    @Schema(description = "根据ID物理删除当前行数据")
            <#elseif swagger>
    @ApiOperation("根据ID物理删除当前行数据")
            <#else>
    /**
     * 根据ID物理删除当前行数据
     */
            </#if>
    @BaseResetDataResult
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable ${primaryField.propertyType} id) {
        return service.removeById(id);
    }
        </#if>

        <#if springdoc>
    @Schema(description = "根据ID查询当前行数据")
        <#elseif swagger>
    @ApiOperation("根据ID查询当前行数据")
        <#else>
    /**
     * 根据ID查询当前行数据
     */
        </#if>
    @BaseResetDataResult
    @GetMapping("/get/{id}")
    public Object get(@PathVariable ${primaryField.propertyType} id) {
        return service.getById(id);
    }
    <#elseif multiplePrimaryKey>
        <#-- 生成多组件请求参数 -->
        <#assign idIndex = 0/>
        <#assign entityParams = "" />
        <#assign entityCreate = "${entity} entity = new ${entity}();"/>
        <#list table.fields as field>
            <#if field.keyFlag>
                <#if idIndex gt 0>
                    <#assign entityParams += ", " />
                </#if>
                <#assign entityParams += "@RequestParam(\"${field.annotationColumnName}\") ${field.propertyType} ${field.propertyName}" />
                <#assign entityCreate += "\n        entity.set${field.capitalName}(${field.propertyName});" />
                <#assign idIndex++ />
            </#if>
        </#list>

        <#if springdoc>
    @Schema(description = "根据多主键ID更新当前行数据")
        <#elseif swagger>
    @ApiOperation("根据多主键ID更新当前行数据")
        <#else>
    /**
     * 根据多主键ID更新当前行数据
     */
        </#if>
    @BaseResetDataResult
    @PutMapping("/update")
    public Object update(${entityParams}, @RequestBody Map${r"<"}String, Object> params) {
        ${entityCreate}
        return service.updateFromMapByMultiId(entity, params);
    }

        <#if hasLogicDeletedField?? && hasLogicDeletedField>
            <#if springdoc>
    @Schema(description = "根据多主键ID逻辑删除当前行数据")
            <#elseif swagger>
    @ApiOperation("根据多主键ID逻辑删除当前行数据")
            <#else>
    /**
     * 根据多主键ID逻辑删除当前行数据
     */
            </#if>
    @BaseResetDataResult
    @DeleteMapping("/delete")
    public Object delete(${entityParams}) {
        ${entityCreate}
        entity.setIsDeleted(1);
        return service.updateById(entity);
    }

            <#if springdoc>
    @Schema(description = "根据多主键ID物理删除当前行数据")
            <#elseif swagger>
    @ApiOperation("根据多主键ID物理删除当前行数据")
            <#else>
    /**
     * 根据多主键ID物理删除当前行数据
     */
            </#if>
    @BaseResetDataResult
    @DeleteMapping("/destroy")
    public Object destroy(${entityParams}) {
        ${entityCreate}
        return service.deleteByMultiId(entity);
    }
        <#else>
            <#if springdoc>
    @Schema(description = "根据多主键ID物理删除当前行数据")
            <#elseif swagger>
    @ApiOperation("根据多主键ID物理删除当前行数据")
            <#else>
    /**
     * 根据多主键ID物理删除当前行数据
     */
            </#if>
    @BaseResetDataResult
    @DeleteMapping("/delete")
    public Object delete(${entityParams}) {
        ${entityCreate}
        return service.deleteByMultiId(entity);
    }
        </#if>

        <#if springdoc>
    @Schema(description = "根据多主键ID查询当前行数据")
        <#elseif swagger>
    @ApiOperation("根据多主键ID查询当前行数据")
        <#else>
    /**
     * 根据多主键ID查询当前行数据
     */
        </#if>
    @BaseResetDataResult
    @GetMapping("/get")
    public Object get(${entityParams}) {
        ${entityCreate}
        return service.selectByMultiId(entity);
    }
    </#if>

    <#if springdoc>
    @Schema(description = "根据条件查询所有数据")
    <#elseif swagger>
    @ApiOperation("根据条件查询所有数据")
    <#else>
    /**
     * 根据条件查询所有数据
     */
    </#if>
    @BaseResetDataResult
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Object list(@RequestBody(required = false) SqlQueryParams sqlQueryParams) {
        // 需要自定义的sql参数的构造
        return service.list(new ElasticQueryWrapper<>(sqlQueryParams));
    }

    <#if springdoc>
    @Schema(description = "根据条件查询分页数据")
    <#elseif swagger>
    @ApiOperation("根据条件查询分页数据")
    <#else>
    /**
     * 根据条件查询分页数据
     */
    </#if>
    @BaseResetDataResult
    @PostMapping("/page")
    public Object page(@RequestBody SqlQueryParams sqlQueryParams) {
        ElasticQueryWrapper${r"<"}${entity}> queryWrapper = new ElasticQueryWrapper<>(sqlQueryParams);
        BasePage${r"<"}${entity}> page = new BasePage<>(sqlQueryParams.getPagination());
        return service.page(page, queryWrapper);
    }
}
</#if>

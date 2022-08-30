package com.codowang.pensieve.core.sql.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.codowang.pensieve.core.utils.MapUtils;
import com.codowang.pensieve.core.utils.StringUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 通用的生成器
 *
 * @author wangyb
 */
public class MybatisPlusMvcGenerator {

    private static final String DEFAULT_CONTROLLER_SUPER_CLASS = "com.codowang.pensieve.core.controller.BaseController";

    private static final String DEFAULT_ENTITY_SUPER_CLASS = "com.codowang.pensieve.core.entity.BaseEntity";

    private static final String DEFAULT_MAPPER_SUPER_CLASS = "com.codowang.pensieve.core.sql.mapper.IBaseMapper";

    private static final String DEFAULT_SERVICE_SUPER_CLASS = "com.codowang.pensieve.core.service.IService";

    private static final String DEFAULT_SERVICE_IMPL_SUPER_CLASS = "com.codowang.pensieve.core.service.ServiceImpl";

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    private static String scanner(String tip, String defaultVal) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            } else if (StringUtils.isNotBlank(defaultVal)) {
                return defaultVal;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    private static void generate(Map<String, Object> config) {
        // 数据源配置
        String dbUrl = MapUtils.getString(config, "dbUrl");
        String dbUsername = MapUtils.getString(config, "dbUsername");
        String dbPassword = MapUtils.getString(config, "dbPassword");
        String packagePath = MapUtils.getString(config, "packagePath");
        FastAutoGenerator
                .create(dbUrl, dbUsername, dbPassword)
                .globalConfig(builder -> {
                    builder.author(MapUtils.getString(config, "packageAuthor")) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()
                            .outputDir(packagePath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder
                            // 设置父包名
                            .parent(MapUtils.getString(config, "packageParent"))
                            // 设置父包模块名
                            .moduleName(MapUtils.getString(config, "packageModuleName"));
                    // 设置mapperXml生成路径
                    // .pathInfo(Collections.singletonMap(OutputFile.mapperXml, filePath));
                })
                .templateConfig(builder -> {
                    // 设置Controller模板
                    builder.controller(MapUtils.getString(config, "templateControllerJava"));
                    // 设置Entity模板
                    builder.entity(MapUtils.getString(config, "templateEntityJava"));
                    // 设置Mapper模板
                    builder.mapper(MapUtils.getString(config, "templateMapperJava"));
                    // 设置MapperXml模板
                    builder.xml(MapUtils.getString(config, "templateMapperXml"));
                    // 设置Service接口模板
                    builder.service(MapUtils.getString(config, "templateServiceJava"));
                    // 设置Service实现模板
                    builder.serviceImpl(MapUtils.getString(config, "templateServiceImplJava"));
                })
                .strategyConfig(builder -> {
                    // 设置需要的创建的表
                    builder.addInclude(StringUtils.join((List<String>) config.get("tableIncludes")));
                    // 控制层配置
                    builder.controllerBuilder()
                            .superClass(MapUtils.getString(config, "controllerSuperClass"))
                            .enableHyphenStyle()
                            // 默认rest风格的控制层
                            .enableRestStyle();
                    // 实体类配置
                    builder.entityBuilder()
                            .superClass(MapUtils.getString(config, "entitySuperClass"))
                            // 链式参数设置
                            .enableChainModel()
                            // 字段名注解
                            .enableTableFieldAnnotation()
                            // lombok注解
                            .enableLombok();
                    // mapper配置, 增加@Mapper注解
                    builder.mapperBuilder()
                            .superClass(MapUtils.getString(config, "mapperSuperClass"))
                            .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class);

                    builder.serviceBuilder()
                            .superServiceClass(MapUtils.getString(config, "serviceSuperClass"))
                            .superServiceImplClass(MapUtils.getString(config, "serviceImplSuperClass"));

                    // 文件覆盖控制
                    if (StringUtils.isTrue(MapUtils.getString(config, "controllerFileOverride"))) {
                        builder.controllerBuilder().enableFileOverride();
                    }
                    if (StringUtils.isTrue(MapUtils.getString(config, "entityFileOverride"))) {
                        builder.entityBuilder().enableFileOverride();
                    }
                    if (StringUtils.isTrue(MapUtils.getString(config, "mapperFileOverride"))) {
                        builder.mapperBuilder().enableFileOverride();
                    }
                    if (StringUtils.isTrue(MapUtils.getString(config, "serviceFileOverride"))) {
                        builder.serviceBuilder().enableFileOverride();
                    }
                })
                // 注入个性化配置
                .injectionConfig(builder -> {
                    builder.beforeOutputFile((tableInfo, map) -> {
                        // 处理配置文件
                        handleTableContext(tableInfo, map, config);
                    });
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    private static Map<String, Object> parseConfigFile(String configFilePath) throws Exception {
        Map<String, Object> configMap = new HashMap<>();
        if (StringUtils.isEmpty(configFilePath)) {
            return configMap;
        }
        Properties configProperties = getConfig(configFilePath);
        // 解析数据库连接配置
        parseDbConfig(configMap, configProperties);
        // 解析打包相关的配置
        parsePackageConfig(configMap, configProperties);
        // 解析模板配置
        parseTemplateConfig(configMap, configProperties);
        // 解析表相关的配置
        parseTableConfig(configMap, configProperties);
        // controller个性化配置
        parseControllerConfig(configMap, configProperties);
        // entity个性化配置
        parseEntityConfig(configMap, configProperties);
        // mapper个性化配置
        parseMapperConfig(configMap, configProperties);
        // service个性化配置
        parseServiceConfig(configMap, configProperties);

        return configMap;
    }

    private static void parseDbConfig(Map<String, Object> configMap, Properties configProperties) {
        // 解析常用的配置
        configMap.put("dbUrl", configProperties.getProperty("db.url"));
        configMap.put("dbUsername", configProperties.getProperty("db.username"));
        configMap.put("dbPassword", configProperties.getProperty("db.password"));
    }

    private static void parsePackageConfig(Map<String, Object> configMap, Properties configProperties) {
        // 生成的目录
        configMap.put("packagePath", configProperties.getProperty(
                "package.path",
                System.getProperty("user.dir") + "/src/main/java"
                )
        );
        //
        configMap.put("packageAuthor", configProperties.getProperty("package.author"));
        configMap.put("packageOverride", configProperties.getProperty("package.override", "false"));
        configMap.put("packageParent", configProperties.getProperty("package.parent"));
        configMap.put("packageModuleName", configProperties.getProperty("package.module-name"));
        configMap.put("packageEntity", configProperties.getProperty("package.entity"));
        configMap.put("packageService", configProperties.getProperty("package.service"));
        configMap.put("packageServiceImpl", configProperties.getProperty("package.serviceImpl"));
        configMap.put("packageMapper", configProperties.getProperty("package.mapper"));
        configMap.put("packageXml", configProperties.getProperty("package.xml"));
        configMap.put("packageController", configProperties.getProperty("package.controller"));
    }

    private static void parseTemplateConfig(Map<String, Object> configMap, Properties configProperties) {
        configMap.put("templateControllerJava", configProperties.getProperty(
                "template.controller-java",
                "generator/templates/controller.java"
                )
        );
        configMap.put("templateEntityJava", configProperties.getProperty(
                "template.entity-java",
                "generator/templates/entity.java"
                )
        );
        configMap.put("templateMapperJava", configProperties.getProperty(
                "template.mapper-java",
                "generator/templates/mapper.java"
                )
        );
        configMap.put("templateMapperXml", configProperties.getProperty(
                "template.mapper-xml",
                "generator/templates/mapper.xml"
                )
        );
        configMap.put("templateServiceJava", configProperties.getProperty(
                "template.service-java",
                "generator/templates/service.java"
                )
        );
        configMap.put("templateServiceImplJava", configProperties.getProperty(
                "template.service-impl-java",
                "generator/templates/serviceImpl.java"
                )
        );
    }

    private static void parseTableConfig(Map<String, Object> configMap, Properties configProperties) {
        configMap.put("tableIncludes", getListProperties(configProperties, "table.includes"));
        configMap.put("tablePrefix", configProperties.getProperty("table.prefix"));
    }

    @SuppressWarnings("unchecked")
    private static void parseControllerConfig(Map<String, Object> configMap, Properties configProperties) {
        configMap.put("controllerEnable", configProperties.getProperty("controller.enable"));
        configMap.put("controllerFileOverride", configProperties.getProperty("controller.file-override"));
        configMap.put("controllerSuperClass", configProperties.getProperty("controller.super-class", DEFAULT_CONTROLLER_SUPER_CLASS));
        Map<String, Object> controllerMapping = new LinkedHashMap<>();
        boolean enableFormatMapping = StringUtils.isTrue(configProperties.getProperty("controller.format-mapping.enable"));
        if (enableFormatMapping && configMap.get("tableIncludes") != null) {
            List<String> tableIncludes = (List<String>) configMap.get("tableIncludes");
            List<String> ignorePrefixList = getListProperties(configProperties, "controller.format-mapping.ignore-prefix");
            List<String> ignoreSuffixList = getListProperties(configProperties, "controller.format-mapping.ignore-suffix");
            // 把所有表名
            for (String tableName : tableIncludes) {
                controllerMapping.put(tableName, formatMapping(tableName, ignorePrefixList, ignoreSuffixList));
            }
        }
        // 接受自定义的controller-mapping
        controllerMapping.putAll(getMapProperties(configProperties, "controller.mapping"));
        configMap.put("controllerMapping", controllerMapping);
    }

    private static void parseEntityConfig(Map<String, Object> configMap, Properties configProperties) {
        configMap.put("entityEnable", configProperties.getProperty("entity.enable"));
        configMap.put("entityFileOverride", configProperties.getProperty("entity.file-override"));
        configMap.put("entitySuperClass", configProperties.getProperty("entity.super-class", DEFAULT_ENTITY_SUPER_CLASS));
    }

    private static void parseMapperConfig(Map<String, Object> configMap, Properties configProperties) {
        configMap.put("mapperEnable", configProperties.getProperty("mapper.enable"));
        configMap.put("mapperFileOverride", configProperties.getProperty("mapper.file-override"));
        configMap.put("mapperSuperClass", configProperties.getProperty("mapper.super-class", DEFAULT_MAPPER_SUPER_CLASS));
    }

    private static void parseServiceConfig(Map<String, Object> configMap, Properties configProperties) {
        configMap.put("serviceEnable", configProperties.getProperty("service.enable"));
        configMap.put("serviceFileOverride", configProperties.getProperty("service.file-override"));
        configMap.put("serviceSuperClass", configProperties.getProperty("service.super-class", DEFAULT_SERVICE_SUPER_CLASS));
        configMap.put("serviceImplSuperClass", configProperties.getProperty("service.impl-super-class", DEFAULT_SERVICE_IMPL_SUPER_CLASS));
    }

    private static List<String> getListProperties(Properties configProperties, String field) {
        List<String> list = new ArrayList<>();
        if (configProperties.containsKey(field)) {
            list.add(configProperties.getProperty(field));
            return list;
        }
        Pattern keyPattern = Pattern.compile(field + "\\[(\\d+)\\]");
        List<Integer> indexList = new ArrayList<>();
        configProperties.keySet().forEach(key -> {
            String keyStr = key.toString();
            Matcher matcher = keyPattern.matcher(keyStr);
            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));
                indexList.add(index);
            }
        });
        indexList.sort(Comparator.comparingInt(a -> a));
        indexList.forEach(i -> {
            list.add(configProperties.getProperty(field + "[" + i + "]"));
        });
        return list;
    }

    private static Map<String, Object> getMapProperties(Properties configProperties, String field) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (configProperties.containsKey(field)) {
            map.put(field, configProperties.get(field));
        }
        // 遍历收集参数
        Pattern keyPattern = Pattern.compile(field + "\\[(\\d+)\\].name");
        configProperties.keySet().forEach(key -> {
            String keyStr = key.toString();
            Matcher matcher = keyPattern.matcher(keyStr);
            if (matcher.find()) {
                int index = Integer.parseInt(matcher.group(1));
                // 获取key-value
                String name = configProperties.getProperty(keyStr);
                Object value = configProperties.getProperty(field + "[" + index + "].value");
                map.put(name, value);
            }
        });
        return map;
    }

    private static String formatMapping(String tableName, List<String> ignorePrefixList, List<String> ignoreSuffixList) {
        for (String ignorePrefix : ignorePrefixList) {
            tableName = tableName.replaceAll(ignorePrefix, "");
        }
        for (String ignoreSuffix : ignoreSuffixList) {
            tableName = tableName.replaceAll(ignoreSuffix, "");
        }
        tableName = tableName.replaceAll("_", "/");
        return tableName;
    }

    private static Properties getConfig(String configFilePath) throws Exception {
        Resource configFileResource;
        configFileResource = new ClassPathResource(configFilePath);
        if (!configFileResource.exists()) {
            configFileResource = new FileUrlResource(configFilePath);
        }
        // 检查时yml文件，还是properties文件
        if (configFilePath.endsWith(".yml")) {
            YamlPropertiesFactoryBean configFactory = new YamlPropertiesFactoryBean();
            configFactory.setResources(configFileResource);
            return configFactory.getObject();
        } else {
            PropertiesFactoryBean configFactory = new PropertiesFactoryBean();
            configFactory.setLocation(configFileResource);
            return configFactory.getObject();
        }
    }

    private static String getArgKey(String arg) {
        // 参数类型 -p package -c 配置文件地址
        switch (arg) {
            case "-p":
            case "--package":
                return "packageParent";
            case "-c":
            case "--configFile":
                return "configFile";
            case "-y":
                return "directlyGenerated";
            default:
                return null;
        }
    }

    private static Object getArgValue(String[] args, String argName, int i) throws Exception {
        // 默认值参数
        if ("directlyGenerated".equals(argName)) {
            return true;
        }
        if (args == null || args.length <= i + 1) {
            throw new Exception("[" + argName + "]参数值不存在");
        }
        return args[i + 1];
    }

    /**
     * 根据表数据, 处理生成代码的上下文信息
     * 包括导入的包, controllerMapping 等信息
     *
     * @param tableInfo 表信息
     * @param context   生成模板的上下文
     * @param config    配置
     */
    private static void handleTableContext(TableInfo tableInfo, Map<String, Object> context, Map<String, Object> config) {
        // 判断是否多主键
        List<TableField> fieldList = tableInfo.getFields();
        List<TableField> keyFieldList = fieldList.stream().filter(TableField::isKeyFlag).collect(Collectors.toList());
        boolean onlyPrimaryKey = keyFieldList.size() == 1;
        context.put("onlyPrimaryKey", onlyPrimaryKey);
        if (onlyPrimaryKey) {
            context.put("primaryField", keyFieldList.get(0));
        }
        boolean multiplePrimaryKey = keyFieldList.size() > 1;
        context.put("multiplePrimaryKey", multiplePrimaryKey);
        Set<String> controllerImports = new LinkedHashSet<>();
        controllerImports.add("com.codowang.pensieve.core.annotation.BaseResetDataResult");
        controllerImports.add("com.codowang.pensieve.core.jdbc.common.SqlQueryParams");
        controllerImports.add("com.codowang.pensieve.core.sql.entity.BasePage");
        controllerImports.add("com.codowang.pensieve.core.sql.entity.QueryWrapper");
        controllerImports.add("java.util.Map");
        context.put("controllerImports", controllerImports);
        Set<String> entityImports = new LinkedHashSet<>();
        if (multiplePrimaryKey) {
            entityImports.add("com.github.jeffreyning.mybatisplus.anno.MppMultiId");
        }
        entityImports.add("com.fasterxml.jackson.annotation.JsonProperty");
        context.put("entityImports", entityImports);
        // 定义字段配置
        Map<String, Map<String, Object>> fieldConfigMap = new HashMap<>();
        context.put("fieldConfig", fieldConfigMap);
        // 处理每个字段的配置
        fieldList.forEach(field -> {
            setFieldSerializeConfig(field, onlyPrimaryKey, context, fieldConfigMap, entityImports);
        });
        // 唯一主键
        if (onlyPrimaryKey) {

        }
        if (DEFAULT_ENTITY_SUPER_CLASS.equals(config.get("entitySuperClass"))) {
            entityImports.add("java.util.Map");
            context.put("entityConstructorType", "MAP");
        } else {
            context.put("entityConstructorType", "DEFAULT");
        }
        // 获取controllerMapping
        context.put("controllerMapping", StringUtils.getString(
                MapUtils.getMap(config, "controllerMapping").get(tableInfo.getName()),
                tableInfo.getName()
        ));
    }

    /**
     * 计算每个字段的序列化配置
     *
     * @param tableField     字段信息
     * @param onlyPrimaryKey 唯一主键
     * @param context        生成文件的上下文
     * @param fieldConfigMap 字段配置
     * @param entityImports  实体导入包配置
     */
    private static void setFieldSerializeConfig(TableField tableField,
                                                boolean onlyPrimaryKey,
                                                Map<String, Object> context,
                                                Map<String, Map<String, Object>> fieldConfigMap,
                                                Set<String> entityImports) {
        String propertyType = tableField.getPropertyType();
        String propertyName = tableField.getPropertyName();
        Map<String, Object> fieldConfig = new HashMap<>();
        boolean hasCustomSerializer = false;
        if ("LocalDate".equals(propertyType)) {
            hasCustomSerializer = true;
            fieldConfig.put("serializeUsing", "JacksonSerializerImpl.LocalDateSerializer.class");
            fieldConfig.put("deserializeUsing", "JacksonDeserializerImpl.LocalDateDeserializer.class");
        } else if ("LocalDateTime".equals(propertyType)) {
            hasCustomSerializer = true;
            fieldConfig.put("serializeUsing", "JacksonSerializerImpl.LocalDateTimeSerializer.class");
            fieldConfig.put("deserializeUsing", "JacksonDeserializerImpl.LocalDateTimeDeserializer.class");
        } else if ("Long".equals(propertyType)) {
            hasCustomSerializer = true;
            fieldConfig.put("serializeUsing", "JacksonSerializerImpl.LongSerializer.class");
            fieldConfig.put("deserializeUsing", "JacksonDeserializerImpl.LongDeserializer.class");
        }
        if (hasCustomSerializer) {
            entityImports.add("com.fasterxml.jackson.databind.annotation.JsonSerialize");
            entityImports.add("com.fasterxml.jackson.databind.annotation.JsonDeserialize");
            entityImports.add("com.codowang.pensieve.core.converter.JacksonSerializerImpl");
            entityImports.add("com.codowang.pensieve.core.converter.JacksonDeserializerImpl");
        }
        if ("isDeleted".equals(propertyName)) {
            context.put("hasLogicDeletedField", true);
        }
        boolean hasInsertFillField = false;
        boolean hasUpdateFillField = false;
        if ("isDeleted".equals(propertyName)
                || "createUserId".equals(propertyName)
                || "createTime".equals(propertyName)) {
            fieldConfig.put("fill", "INSERT");
            fieldConfig.put("insertFillUsing", getDefaultFieldFill(propertyType, propertyName));
            hasInsertFillField = true;
        }
        if ("modifyUserId".equals(propertyName)
                || "modifyTime".equals(propertyName)) {
            fieldConfig.put("fill", "INSERT_UPDATE");
            fieldConfig.put("insertFillUsing", getDefaultFieldFill(propertyType, propertyName));
            fieldConfig.put("updateFillUsing", getDefaultFieldFill(propertyType, propertyName));
            hasInsertFillField = true;
            hasUpdateFillField = true;
        }

        if (hasInsertFillField) {
            entityImports.add("com.baomidou.mybatisplus.annotation.FieldFill");
            entityImports.add("com.codowang.pensieve.core.sql.fill.FieldFillImpl");
            entityImports.add("com.codowang.pensieve.core.annotation.FieldInsertFill");
        }

        if (hasUpdateFillField) {
            entityImports.add("com.baomidou.mybatisplus.annotation.FieldFill");
            entityImports.add("com.codowang.pensieve.core.sql.fill.FieldFillImpl");
            entityImports.add("com.codowang.pensieve.core.annotation.FieldUpdateFill");
        }
        boolean hasIdType = false;
        if (tableField.isKeyFlag() && onlyPrimaryKey) {
            if ("Integer".equals(propertyType)) {
                context.put("idType", "INPUT");
                fieldConfig.put("fill", "INSERT");
                fieldConfig.put("insertFillUsing", "FieldFillImpl.IntegerSnowflakeIdFill.class");
                hasIdType = true;
            } else if ("Long".equals(propertyType)) {
                context.put("idType", "ASSIGN_ID");
                hasIdType = true;
            } else if ("String".equals(propertyType)) {
                context.put("idType", "ASSIGN_UUID");
                hasIdType = true;
            }
        }
        if (hasIdType) {
            entityImports.add("com.baomidou.mybatisplus.annotation.IdType");
            entityImports.add("com.baomidou.mybatisplus.annotation.FieldFill");
            entityImports.add("com.codowang.pensieve.core.sql.fill.FieldFillImpl");
            entityImports.add("com.codowang.pensieve.core.annotation.FieldInsertFill");
        }
        fieldConfigMap.put(propertyName, fieldConfig);
    }

    private static String getDefaultFieldFill(String propertyType, String propertyName) {
        if ("isDeleted".equals(propertyName)) {
            return "FieldFillImpl.IsDeletedFill.class";
        }
        if (propertyName.endsWith("UserId")
                || propertyName.endsWith("user_id")
                || propertyName.endsWith("userId")) {
            if ("Integer".equals(propertyType)) {
                return "FieldFillImpl.LoginUserIdIntegerFill.class";
            } else if ("Long".equals(propertyType)) {
                return "FieldFillImpl.LoginUserIdLongFill.class";
            } else {
                return "FieldFillImpl.LoginUserIdFill.class";
            }
        }
        if ("LocalDateTime".equals(propertyType)) {
            return "FieldFillImpl.LocalDateTimeNowFill.class";
        }

        if ("LocalDate".equals(propertyType)) {
            return "FieldFillImpl.LocalDateNowFill.class";
        }

        if ("Date".equals(propertyType)) {
            return "FieldFillImpl.DateNowFill.class";
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // 参数解析
        // 对参数进行分组
        Map<String, Object> params = new HashMap<>();
        int i = 0;
        String paramKey;
        for (String arg : args) {
            if (arg != null && arg.startsWith("-")) {
                paramKey = getArgKey(arg);
                params.put(paramKey, getArgValue(args, arg, i));
            }
            i++;
        }
        // 如果有配置文件地址，则对配置文件进行解析
        String configFilePath = MapUtils.getString(params, "configFile");
        params.remove("configFile");
        // 加载配置文件，并进行解析
        Map<String, Object> config = parseConfigFile(configFilePath);
        config.putAll(params);
        // 增加手动确认，避免误触
        if (StringUtils.isTrue(MapUtils.getString(config, "directlyGenerated"))
                || "Y".equals(scanner("是否生成（Y/N）？", "N"))) {
            generate(config);
        }
    }
}

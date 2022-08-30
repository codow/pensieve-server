package com.codowang.pensieve.core.sql.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.codowang.pensieve.core.utils.MapUtils;
import com.codowang.pensieve.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 继承mybatis-plus的queryWrapper，用来转换SqlQueryParams为QueryWrapper
 *
 * @param <T> 实体类泛型
 * @author wangyb
 * @since 2022-07-06
 */
public class BasePage<T> implements IPage<T> {

    /**
     * 排序字段信息
     */
    @Setter
    protected List<OrderItem> orders = new ArrayList<>();
    /**
     * 自动优化 COUNT SQL
     */
    @JsonIgnore
    protected boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    @JsonIgnore
    protected boolean searchCount = true;
    /**
     * {@link #optimizeJoinOfCountSql()}
     */
    @JsonIgnore
    @Setter
    protected boolean optimizeJoinOfCountSql = true;
    /**
     * countId
     */
    @JsonIgnore
    @Setter
    protected String countId;
    /**
     * maxLimit
     */
    @JsonIgnore
    @Setter
    protected Long maxLimit;
    /**
     * 保存查询结果
     */
    private List<T> list = new ArrayList<>();
    /**
     * 分页参数
     */
    private SqlPagination pagination;

    public BasePage() {
        this(new SqlPagination());
    }

    public BasePage(SqlPagination pagination) {
        this.setPagination(pagination);
    }

    public BasePage(SqlQueryParams queryParams) {
        if (queryParams == null) {
            return;
        }
        this.setPagination(queryParams.getPagination());
        this.initOrder(queryParams);
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public BasePage(long current, long size) {
        this(current, size, 0);
    }

    public BasePage(long current, long size, long total) {
        this(current, size, total, true);
    }

    public BasePage(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    public BasePage(long current, long size, long total, boolean searchCount) {
        if (current > 1) {
            this.setCurrent(current);
        }
        this.setSize(size);
        this.setTotal(total);
        this.searchCount = searchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.getCurrent() > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.getCurrent() < this.getPages();
    }

    @Override
    public List<OrderItem> orders() {
        return this.orders;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    @Override
    public boolean optimizeJoinOfCountSql() {
        return optimizeJoinOfCountSql;
    }

    public BasePage<T> setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }

    public BasePage<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    @JsonIgnore
    @Override
    public List<T> getRecords() {
        return this.list;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.list = records;
        return this;
    }

    @JsonIgnore
    @Override
    public long getTotal() {
        return this.getPagination().getTotal();
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.getPagination().setTotal(Long.valueOf(total).intValue());
        return this;
    }

    @JsonIgnore
    @Override
    public long getSize() {
        return this.getPagination().getSize();
    }

    @Override
    public IPage<T> setSize(long size) {
        this.getPagination().setSize(Long.valueOf(size).intValue());
        return this;
    }

    @JsonIgnore
    @Override
    public long getCurrent() {
        return this.getPagination().getIndex();
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.getPagination().setIndex(Long.valueOf(current).intValue());
        return this;
    }

    /**
     * 当前分页总页数
     */
    @JsonIgnore
    @Override
    public long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    @Override
    public String countId() {
        return this.countId;
    }

    @Override
    public Long maxLimit() {
        return this.maxLimit;
    }

    @Override
    public boolean searchCount() {
        if (this.getTotal() < 0) {
            return false;
        }
        return searchCount;
    }

    public List<T> getList() {
        return this.list;
    }

    public BasePage<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public SqlPagination getPagination() {
        if (this.pagination == null) {
            this.pagination = new SqlPagination();
        }
        return this.pagination;
    }

    public BasePage<T> setPagination(SqlPagination pagination) {
        this.pagination = pagination;
        return this;
    }

    /**
     * 使用sqlQueryParams初始化排序条件
     *
     * @param sqlQueryParams sql查询参数
     */
    private void initOrder(SqlQueryParams sqlQueryParams) {
        // 初始化sort条件
        Map<String, Object> sortInfo = sqlQueryParams.getSort();
        if (sortInfo != null) {
            List<OrderItem> newOrders = new ArrayList<>();
            sortInfo.keySet().forEach(sortField -> {
                String sortType = StringUtils.getString(
                        MapUtils.getString(sortInfo, sortField),
                        "ASC"
                ).toUpperCase(Locale.ROOT);
                if ("DESC".equals(sortType)) {
                    newOrders.add(OrderItem.desc(sortField));
                } else {
                    newOrders.add(OrderItem.asc(sortField));
                }
            });
            this.orders = newOrders;
        }
    }
}

package com.codowang.pensieve.core.sql.entity;

import java.io.Serializable;

/**
 * Elastic 系列之分页处理参数实现类，不允许继承。
 *
 * @author lvhonglun
 */
public final class SqlPagination implements Serializable {

    //1.获取或设置分页的开始位
    private int startIndex = 0;

    //2.获取或设置分页的结束位
    private int endIndex = 10;

    //3.获取或设置分页的当前页
    private int index = 1;

    //4.获取或设置分页的每页数据条数
    private int size = 20;

    //5.获取或设置分页的数据总数
    private int total = -1;

    //6.获取或设置分页总数
    private int pageCount = 1;

    //7.获取或设置限制的数据总数
    private int limitTotal = 50;

    public int getStartIndex() {
        startIndex = (this.index < 1 ? 1 : this.index - 1) * this.size;
        return startIndex;
    }

    public int getEndIndex() {
        endIndex = this.index * this.size;
        return endIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIndex(Object index) {
        // 1、如果index不为空，转换为int
        int nIndex = 1;
        if (index != null) {
            nIndex = Integer.parseInt(index.toString());
        }

        // 2、调用this.setIndex()赋值
        this.setIndex(nIndex);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSize(Object size) {
        int nSize = 20;
        if (size != null) {
            nSize = Integer.parseInt(size.toString());
        }
        this.setSize(nSize);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getLimitTotal() {
        return limitTotal;
    }

    public void setLimitTotal(int limitTotal) {
        this.limitTotal = limitTotal;
    }
}

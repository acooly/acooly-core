package com.acooly.core.common.domain;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.ie.anno.ExportColumn;
import com.acooly.core.utils.mapper.BeanCopier;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 统一定义id的entity基类
 *
 * <p>实体基类
 *
 * @author zhangpu
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity implements Entityable {
    /**
     * UID
     */
    private static final long serialVersionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExportColumn(header = "ID", order = 0)
    private Long id;

    /**
     * 创建时间
     */
    @Column(
            name = "create_time",
            columnDefinition = " timestamp  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'"
    )
    @ExportColumn(header = "创建时间", order = 999)
    private Date createTime = new Date();

    @Column(
            name = "update_time",
            columnDefinition =
                    "timestamp  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'"
    )
    /** 修改时间 */
    @ExportColumn(header = "更新时间", order = 1000)
    private Date updateTime = new Date();

    public static <T, S extends AbstractEntity> List<T> to(List<S> list, Class<T> clazz) {
        if (list == null || list.isEmpty()) {
            return Lists.newArrayList();
        }
        List<T> ts = new ArrayList<>(list.size());
        for (S s : list) {
            ts.add(s.to(clazz));
        }
        return ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;

        AbstractEntity that = (AbstractEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public <T> T to(Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            BeanCopier.copy(this, t, BeanCopier.CopyStrategy.IGNORE_NULL, BeanCopier.NoMatchingRule.IGNORE);
            return t;
        } catch (Exception e) {
            throw new BusinessException("OBJECT_FILED_COPY_ERROR", "对象属性拷贝失败", e);
        }
    }

    public void from(Object dto) {
        BeanCopier.copy(
                dto, this, BeanCopier.CopyStrategy.IGNORE_NULL, BeanCopier.NoMatchingRule.IGNORE);
    }

    public void fromContainNUll(Object dto) {
        BeanCopier.copy(dto, this, BeanCopier.CopyStrategy.CONTAIN_NULL, BeanCopier.NoMatchingRule.EXCEPTION);
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}

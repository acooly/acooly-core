/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-02-22 05:00
 */
package com.acooly.core.utils.validate.predicate;

/**
 * 迁移时，为不引入guava包依赖的替代接口定义
 *
 * @author zhangpu
 * @date 2020-02-22 05:00
 */
public interface Predicate<T> {


    /**
     * Returns the result of applying this predicate to {@code input} (Java 8 users, see notes in the
     * class documentation above). This method is <i>generally expected</i>, but not absolutely
     * required, to have the following properties:
     *
     * <ul>
     * <li>Its execution does not cause any observable side effects.
     * <li>The computation is <i>consistent with equals</i>; that is, {@link Objects#equal
     * Objects.equal}{@code (a, b)} implies that {@code predicate.apply(a) ==
     * predicate.apply(b))}.
     * </ul>
     *
     * @throws NullPointerException if {@code input} is null and this predicate does not accept null
     *                              arguments
     */
    boolean apply(T input);

    /**
     * Indicates whether another object is equal to this predicate.
     *
     * <p>Most implementations will have no reason to override the behavior of {@link Object#equals}.
     * However, an implementation may also choose to return {@code true} whenever {@code object} is a
     * {@link Predicate} that it considers <i>interchangeable</i> with this one. "Interchangeable"
     * <i>typically</i> means that {@code this.apply(t) == that.apply(t)} for all {@code t} of type
     * {@code T}). Note that a {@code false} result from this method does not imply that the
     * predicates are known <i>not</i> to be interchangeable.
     */
    @Override
    boolean equals(Object object);


}

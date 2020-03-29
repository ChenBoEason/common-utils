package com.github.x4096.common.utils.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/2 16:36
 * @description 实现深度的BeanOfClasssA<->BeanOfClassB复制
 * @readme
 */
public class BeanMapperUtils {

    private BeanMapperUtils() {

    }

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();


    /**
     * 简单的复制出新类型对象.
     */
    public static <S, D> D copyProperties(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 简单的复制出新对象ArrayList
     */
    public static <S, D> List<D> copyProperties(Iterable<S> sourceList, Class<D> destinationClass) {
        if (null == sourceList) {
            return Collections.emptyList();
        }

        Preconditions.checkNotNull(destinationClass, "目标对象不能为 null");

        List<D> destinationList = new ArrayList<>();
        for (S source : sourceList) {
            if (source != null) {
                destinationList.add(mapper.map(source, destinationClass));
            }
        }
        return destinationList;
    }

    /**
     * 简单复制出新对象数组
     */
    // public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
    //     D[] destinationArray = (D[]) Array.newInstance(destinationClass, sourceArray.length);
    //
    //     int i = 0;
    //     for (S source : sourceArray) {
    //         if (source != null) {
    //             destinationArray[i] = mapper.map(sourceArray[i], destinationClass);
    //             i++;
    //         }
    //     }
    //
    //     return destinationArray;
    // }

}

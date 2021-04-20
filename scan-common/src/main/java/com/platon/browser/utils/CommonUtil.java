package com.platon.browser.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 公共工具类
 *
 * @author huangyongpeng@matrixelements.com
 * @date 2021/4/17
 */
public class CommonUtil {

    /**
     * 获取trace-id
     *
     * @param
     * @return java.lang.String
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/17
     */
    public static String getTraceId() {
        return StrUtil.removeAll(UUID.randomUUID().toString(), "-");
    }

    /**
     * 支持lamda的链式判空
     * 用法：ofNullable(() -> obj.getObj1().getObj2().getObj3()).ifPresent(res -> System.out.println(res));
     * 解释：会自动判断getObj1()、getObj2()、getObj3()是否为空，如果getObj3()的值不为空，则打印。例如getObj2()为空，结果返回null，而不是报空指针。
     *
     * @param resolver
     * @return java.util.Optional<T>
     * @author huangyongpeng@matrixelements.com
     * @date 2021/4/19
     */
    public static <T> Optional<T> ofNullable(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

}

package com.github.x4096.common.utils.base;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/12 21:55
 * @description 引入一个简简单单的Pair, 用于返回值返回两个元素
 * @readme link https://github.com/vipshop/vjtools/blob/master/vjkit/src/main/java/com/vip/vjtools/vjkit/base/type/Pair.java
 */
public class Pair<L, R> {

    private final L left;

    private final R right;

    /**
     * Creates a new pair.
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }


    public L getLeft() {
        return left;
    }


    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        return prime * result + ((right == null) ? 0 : right.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Pair other = (Pair) obj;

        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }

        if (right == null) {
            return other.right == null;
        } else{
            return right.equals(other.right);
        }
    }

    @Override
    public String toString() {
        return "Pair [left=" + left + ", right=" + right + ']';
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Pair
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<L, R>(left, right);
    }

}
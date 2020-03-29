package com.github.x4096.common.utils.base;

/**
 * @author 0x4096.peng@gmail.com
 * @project common-utils
 * @datetime 2020/2/12 21:56
 * @description 引入一个简简单单的Triple, 用于返回值返回三个元素
 * @readme link https://github.com/vipshop/vjtools/blob/master/vjkit/src/main/java/com/vip/vjtools/vjkit/base/type/Triple.java
 */
public class Triple<L, M, R> {

    private final L left;

    private final M middle;

    private final R right;

    /**
     * Creates a new Triple.
     */
    public Triple(L left, M middle, R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }


    public L getLeft() {
        return left;
    }


    public M getMiddle() {
        return middle;
    }


    public R getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((left == null) ? 0 : left.hashCode());
        result = prime * result + ((middle == null) ? 0 : middle.hashCode());
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
        Triple other = (Triple) obj;
        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }
        if (middle == null) {
            if (other.middle != null) {
                return false;
            }
        } else if (!middle.equals(other.middle)) {
            return false;
        }
        if (right == null) {
            if (other.right != null) {
                return false;
            }
        } else if (!right.equals(other.right)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Triple [left=" + left + ", middle=" + middle + ", right=" + right + ']';
    }

    /**
     * 根据等号左边的泛型，自动构造合适的Triple
     */
    public static <L, M, R> Triple<L, M, R> of(L left, M middle, R right) {
        return new Triple<L, M, R>(left, middle, right);
    }

}
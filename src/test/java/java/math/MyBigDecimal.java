package java.math;

/**
 * 相同包名测试
 * @author wangyb
 */
public class MyBigDecimal extends BigDecimal {

    public MyBigDecimal () {
        super(1);
    }

    public static void main(String[] args) {
        System.out.println(MyBigDecimal.INFLATED);
    }
}

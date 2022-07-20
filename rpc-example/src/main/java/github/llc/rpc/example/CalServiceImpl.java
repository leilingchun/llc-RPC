package github.llc.rpc.example;

/**
 * @author llc
 * @Description
 * @Version
 * @date 2022/7/18 21:54
 */
public class CalServiceImpl implements CalService{
    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public int minus(int a, int b) {
        return a-b;
    }
}

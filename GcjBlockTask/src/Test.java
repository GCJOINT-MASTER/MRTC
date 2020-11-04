import cn.sh.qianyi.coinspider.main.CertificationGcj3j;
import cn.sh.qianyi.coinspider.util.ConfigUtil;
import cn.sh.qianyi.coinspider.util.SysConstants;
import org.gcj3j.crypto.Credentials;

import java.math.BigInteger;

import static cn.sh.qianyi.coinspider.main.CertificationGcj3j.systemAddress;
import static cn.sh.qianyi.coinspider.util.Gcj3jSignTest.sendMoney;

public class Test {

    public static void main(String[] args) throws Exception {
        String privateKey = "b15fd54fee4ee85137374cac529f79d22a3286f5b6ae5683aa89019f87064a1c";
        Credentials credentials = Credentials.create(privateKey);
        System.out.println(credentials.getAddress());
        sendMoney(privateKey, new BigInteger("200000000000000000000"), "0xC5be1d52bebE0d3b14b6F928b742e678A8569Ee8");
    }

}

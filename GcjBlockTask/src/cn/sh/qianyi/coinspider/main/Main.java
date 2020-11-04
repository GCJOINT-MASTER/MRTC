package cn.sh.qianyi.coinspider.main;

import cn.sh.qianyi.coinspider.task.CertificationTask;
import cn.sh.qianyi.coinspider.task.CoinTask;
import cn.sh.qianyi.coinspider.task.CompanyCertificationTask;

public class Main {
    public static void main(final String[] args) {
        try {

            //扫描区块
            new Thread(() -> CoinTask.start()).start();
//            个人实名上链
            new Thread(() -> CertificationTask.start()).start();
            //机构实名上链
            new Thread(() -> CompanyCertificationTask.start()).start();

//            实名后将预创建合同改为正常合同   ，这里可以在每次上链成功后 调用，  不需要定时来启动调用。 这里的功能在其他地方已经实现
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    AuthAfterProcessPreContractTask.start();
//                }
//            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

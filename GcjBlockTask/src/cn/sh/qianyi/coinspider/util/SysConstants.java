package cn.sh.qianyi.coinspider.util;

public class SysConstants {
    public static final String BTC_NETWORK = "https://www.blockchain.com";
    public static final String ETH_NETWORK = "https://etherscan.io";
    public static final String USDT_NETWORK = "https://api.omniexplorer.info";
    //    ***************************************CT
    public static final String GCJ_NETWORK = ConfigUtil.getConfig("gcj.netwowk");
    public static final String GCJ_PUBLIC_SERVER = ConfigUtil.getConfig("gcj.public.server");
    public static final byte GCJ_CHAIN_ID = (byte) Integer.parseInt(ConfigUtil.getConfig("gcj.chain.id"));
    public static final String CONTRAGCJ_ADDRESS = "0x7833ea847fe0784658037ed3afdaee4ab7b25021";
    //****************************gcj  email
    public static final String GCJ_EMAOL_PORT = ConfigUtil.getConfig("gcj.email.port");
    public static final String GCJ_EMAOL_FROM_USER_NAME = ConfigUtil.getConfig("gcj.email.from.user.name");
    public static final String GCJ_EMAOL_FROM_USER_PASSWORD = ConfigUtil.getConfig("gcj.email.from.user.password");
    public static final String GCJ_EMAOL_SMTP_SERVER = ConfigUtil.getConfig("gcj.email.smtp.server");

    /************************************BTC*/
    public static final String BTC_NETWORK_CONFIRM = ConfigUtil.getConfig("btc.network.confirm");
    public static final String BTC_SERVER_MAINNET = ConfigUtil.getConfig("btc.server.mainnet");
    public static final String BTC_RECHARGE_NETWORK = ConfigUtil.getConfig("btc.recharge.network");
    public static final String BTC_SERVER_USERNAME = ConfigUtil.getConfig("btc.server.username");
    public static final String BTC_SERVER_PASSWORD = ConfigUtil.getConfig("btc.server.password");
    public static final String BTC_WALLET_PASSWORD = ConfigUtil.getConfig("btc.wallet.password");

    /************************************USDT*/
    public static final String USDT_NETWORK_CONFIRM = ConfigUtil.getConfig("usdt.network.confirm");
    public static final String USDT_SYSTEM_ADDRESS = ConfigUtil.getConfig("usdt.system.address");
    public static final String USDT_SERVER_MAINNET = ConfigUtil.getConfig("usdt.server.mainnet");
    public static final String USDT_RECHARGE_NETWORK = ConfigUtil.getConfig("usdt.recharge.network");
    public static final String USDT_SERVER_USERNAME = ConfigUtil.getConfig("usdt.server.username");
    public static final String USDT_SERVER_PASSWORD = ConfigUtil.getConfig("usdt.server.password");
    public static final String USDT_WALLET_PASSWORD = ConfigUtil.getConfig("usdt.wallet.password");

    /************************************ETH*/
    public static final String ETH_NETWORK_CONFIRM = ConfigUtil.getConfig("eth.network.confirm");
    public static final String ETH_SERVER_MAINNET = ConfigUtil.getConfig("eth.server.mainnet");
    public static final String ETH_PRIVATE_KEY = ConfigUtil.getConfig("eth.private.key");
    public static final String ETH_RECHARGE_NETWORK = ConfigUtil.getConfig("eth.recharge.network");

    /************************************ERC20*/
    public static final String ERC20_NETWORK_CONFIRM = ConfigUtil.getConfig("erc20.network.confirm");
    public static final String ERC20_PRIVATE_KEY = ConfigUtil.getConfig("erc20.private.key");
    public static final String ERC20_RECHARGE_NETWORK = ConfigUtil.getConfig("erc20.recharge.network");
    public static final String ERC20_CONTRAGCJ_ADDRESS = ConfigUtil.getConfig("erc20.contract.address");
    /************************************C2C*/
    public static final String C2C_TIMEOUT = ConfigUtil.getConfig("c2c.timeout");
}

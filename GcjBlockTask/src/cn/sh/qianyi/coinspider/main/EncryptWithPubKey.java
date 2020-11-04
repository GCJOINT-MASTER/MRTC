package cn.sh.qianyi.coinspider.main;
import java.math.BigInteger;
import java.security.Security;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.bouncycastle.math.ec.custom.sec.SecP256K1FieldElement;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Point;


public class EncryptWithPubKey {
	//˽Կ
	static BigInteger privateKeyValue = new BigInteger("", 16);
	//��Կ
	static String publicKeyValue = "c983d271b023c951ca488c15cd2e7ffd4de564d02cf36c368d797aaa270638f9b16568b37e00c140499a50dbf379de60af86c8757b49501bf8138b92344d2cc0";
    
	
    public static void main(String [] args) throws Exception {
    	System.out.println("04ea10283379985677a58dac91dcf4fa3ee06245b12022df4ae1221370bf13908ebe078d058ff2d633fb2df9bb0cbff40d9efa127e0b1bf87be4782f3212be8ec6b3955a725b66f15e05663d3413992a6ff7a85ae81a073f86f4f5f2248dd0d4d2479a3ec0ba986aa9f9863df7b39b56c160a74fcc910c0df207cc2c68e0e9c8c2bd5c14c3a1c9e5fee16d9631a578168489723f77f40ea1bc4a2254aeb8e07dc55dd765df048907b26cc6241a9dc930181595d764f2c7668c15f8cf1beb8e1d5f0cd767bba6a77b93fa240766e61b619a70b58022".toString().length());
    	String name = "c983d271b023c951ca488c15cd2e7ffd4de564d02cf36c368d797aaa270638f9b16568b37e00c140499a50dbf379de60af86c8757b49501bf8138b92344d2cc0";
    	//������������ȡ����Ҫ�����ַ�
    	Map<String, Object> map = getNameVal(name);
    	String name1 = name.substring(new Integer(map.get("index").toString()), name.length());
    	//�������ַ�secp256k1����
    	String encrypt = encrypt(name, publicKeyValue);
    	//�������������
    	System.out.println(encrypt);
    	//ed("11", publicKeyValue, privateKeyValue);
    	
    	
    	/*String name = "91310112MA1GBDJJ5B";
    	//����֤���ų���ȡ����Ҫ�����ַ�
    	String content = name.substring(4,14);
    	String number1 = name.substring(0,4);
    	String number2 = name.substring(14,name.length());
    	String encrypt = encrypt(content, publicKeyValue);
    	
    	
    	System.out.println(number1 +"&"+ encrypt+ "&"+number2);*/
    
    }
    
    //������������ȡ����Ҫ�����ַ�
    public static Map<String,Object> getNameVal(String name){
    	String content = "";
    	Map<String, Object> map =  new HashMap<String,Object>();
    	if (name.length() == 1) {
		    content = name.substring(0,name.length());
		    map.put("index", 0);
	    } else if (name.length() == 2) {
	    	content = name.substring(0,1);
	    	map.put("index", 1);
	    } else if (name.length() >= 3  && name.length() <= 4) {
	    	content = name.substring(0,2);
	    	map.put("index", 2);
	    } else if(name.length() > 4) {
	    	content = name.substring(0,3);
	    	map.put("index", 3);
	    }
    	map.put("content", content);
    	return map;
    }
    
    //����֤���ų���ȡ����Ҫ�����ַ�
    public static Map<String,Object> getDocumentVal(String name){
    	String content = "";
    	Map<String, Object> map =  new HashMap<String,Object>();
    	if (name.length() == 1) {
		    content = name.substring(0,name.length());
		    map.put("index", 0);
	    } else if (name.length() == 2) {
	    	content = name.substring(0,1);
	    	map.put("index", 1);
	    } else if (name.length() >= 3  && name.length() <= 4) {
	    	content = name.substring(0,2);
	    	map.put("index", 2);
	    } else if(name.length() > 4) {
	    	content = name.substring(0,3);
	    	map.put("index", 3);
	    }
    	map.put("content", content);
    	return map;
    }
    
    //secp256k1����
    public static String encrypt(String content,String publicKeyValue) throws Exception{
   	 // ECDSA secp256k1 algorithm constants
       BigInteger pointGPre = new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16);
       BigInteger pointGPost = new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16);
       BigInteger factorN = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
       BigInteger fieldP = new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f", 16);

       Security.addProvider(new BouncyCastleProvider());
       Cipher cipher = Cipher.getInstance("ECIES", "BC");
       IESParameterSpec iesParams = new IESParameterSpec(null, null, 64);

       //----------------------------
       // Encrypt with public key
       //----------------------------

       // public key for test
       //System.out.println(new BigInteger(publicKeyValue);
       String prePublicKeyStr = publicKeyValue.substring(0, 64);
       String postPublicKeyStr = publicKeyValue.substring(64);

       EllipticCurve ellipticCurve = new EllipticCurve(new ECFieldFp(fieldP), new BigInteger("0"), new BigInteger("7"));
       ECPoint pointG = new ECPoint(pointGPre, pointGPost);
       ECNamedCurveSpec namedCurveSpec = new ECNamedCurveSpec("secp256k1", ellipticCurve, pointG, factorN);

       // public key
       SecP256K1Curve secP256K1Curve = new SecP256K1Curve();
       SecP256K1Point secP256K1Point = new SecP256K1Point(secP256K1Curve, new SecP256K1FieldElement(new BigInteger(prePublicKeyStr, 16)), new SecP256K1FieldElement(new BigInteger(postPublicKeyStr, 16)));
       SecP256K1Point secP256K1PointG = new SecP256K1Point(secP256K1Curve, new SecP256K1FieldElement(pointGPre), new SecP256K1FieldElement(pointGPost));
       ECDomainParameters domainParameters = new ECDomainParameters(secP256K1Curve, secP256K1PointG, factorN);
       ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(secP256K1Point, domainParameters);
       BCECPublicKey publicKeySelf = new BCECPublicKey("ECDSA", publicKeyParameters, namedCurveSpec, BouncyCastleProvider.CONFIGURATION);

       // begin encrypt

       cipher.init(Cipher.ENCRYPT_MODE, publicKeySelf, iesParams);
       //��������
       byte[] e = cipher.doFinal(content.getBytes("UTF-8"));
       //��byteתΪ16�����ַ���
       return bytesToHexString(e);
   }
    
    //ͨ����Կ���ܣ�˽Կ����ʾ��
    public static void ed(String content,String publicKeyValue,BigInteger privateKeyValue) throws Exception{
    	 // ECDSA secp256k1 algorithm constants
        BigInteger pointGPre = new BigInteger("79be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798", 16);
        BigInteger pointGPost = new BigInteger("483ada7726a3c4655da4fbfc0e1108a8fd17b448a68554199c47d08ffb10d4b8", 16);
        BigInteger factorN = new BigInteger("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16);
        BigInteger fieldP = new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f", 16);

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        IESParameterSpec iesParams = new IESParameterSpec(null, null, 64);

        //----------------------------
        // Encrypt with public key
        //----------------------------

        // public key for test
        //System.out.println(new BigInteger(publicKeyValue);
        String prePublicKeyStr = publicKeyValue.substring(0, 64);
        String postPublicKeyStr = publicKeyValue.substring(64);

        EllipticCurve ellipticCurve = new EllipticCurve(new ECFieldFp(fieldP), new BigInteger("0"), new BigInteger("7"));
        ECPoint pointG = new ECPoint(pointGPre, pointGPost);
        ECNamedCurveSpec namedCurveSpec = new ECNamedCurveSpec("secp256k1", ellipticCurve, pointG, factorN);

        // public key
        SecP256K1Curve secP256K1Curve = new SecP256K1Curve();
        SecP256K1Point secP256K1Point = new SecP256K1Point(secP256K1Curve, new SecP256K1FieldElement(new BigInteger(prePublicKeyStr, 16)), new SecP256K1FieldElement(new BigInteger(postPublicKeyStr, 16)));
        SecP256K1Point secP256K1PointG = new SecP256K1Point(secP256K1Curve, new SecP256K1FieldElement(pointGPre), new SecP256K1FieldElement(pointGPost));
        ECDomainParameters domainParameters = new ECDomainParameters(secP256K1Curve, secP256K1PointG, factorN);
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(secP256K1Point, domainParameters);
        BCECPublicKey publicKeySelf = new BCECPublicKey("ECDSA", publicKeyParameters, namedCurveSpec, BouncyCastleProvider.CONFIGURATION);

        // begin encrypt

        cipher.init(Cipher.ENCRYPT_MODE, publicKeySelf, iesParams);
        String text = "This is a test";
        //��������
        byte[] e = cipher.doFinal(text.getBytes("UTF-8"));
        //��byteתΪ16�����ַ���
        System.out.println(bytesToHexString(e));

        //----------------------------
        // Decrypt with private key
        //----------------------------

        // private key for test, match with public key above
        
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(privateKeyValue, namedCurveSpec);
        BCECPrivateKey privateKeySelf = new BCECPrivateKey("ECDSA", privateKeySpec, BouncyCastleProvider.CONFIGURATION);

        // begin decrypt
        cipher.init(Cipher.DECRYPT_MODE, privateKeySelf, iesParams);
    	byte[] de = cipher.doFinal(e);
		String result = new String(de, "UTF-8");
		System.out.println(result);
    }
    public static String bytesToHexString(byte[] src){   
        StringBuilder stringBuilder = new StringBuilder("");   
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < src.length; i++) {   
            int v = src[i] & 0xFF;   
            String hv = Integer.toHexString(v);   
            if (hv.length() < 2) {   
                stringBuilder.append(0);   
            }   
            stringBuilder.append(hv);   
        }   
        return stringBuilder.toString();   
    }   
    /**  
     * Convert char to byte  
     * @param c char  
     * @return byte  
     */  
    private static byte charToByte(char c) {   
        return (byte) "0123456789ABCDEF".indexOf(c);   
    }  
    public static byte[] hexStringToBytes(String hexString) {   
        if (hexString == null || hexString.equals("")) {   
            return null;   
        }   
        hexString = hexString.toUpperCase();   
        int length = hexString.length() / 2;   
        char[] hexChars = hexString.toCharArray();   
        byte[] d = new byte[length];   
        for (int i = 0; i < length; i++) {   
            int pos = i * 2;   
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
        }   
        return d;   
    }   
}
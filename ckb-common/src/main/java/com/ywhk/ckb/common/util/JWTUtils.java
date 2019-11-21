//package com.ywhk.ckb.common.util;
//
//import com.ywhk.ckb.enums.user.UserTypeEnum;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.apache.commons.codec.binary.Base64;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class JWTUtils {
//
//	// 用于生成secret key的stingKey
//	private static String JWT_SECRET = "WXPLATFORMC607E7C1DFC9277DE7C02C02F6815A60";
//
//	public final static String USER_ID_KEY = "userId";
//	public final static String ROLE_ID_KEY = "roleId";
//	public final static String USER_TYPE_KEY = "userType";
//
//	/**
//	 * @description 创建JWT Token
//	 * @param: claims jwt的所含的用于校验的信息
//	 * @param: subject 用户唯一标识
//	 * @param: ttlMillis 过期时间（毫秒）
//	 */
//	public static String createJWT(Map<String, Object> claims, String subject, long ttlMillis) {
//		// 指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
//		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//		// 生成JWT的时间
//		Long nowMillis = System.currentTimeMillis();
//		// 将long型的时间毫秒转为日期时间
//		Date now = new Date(nowMillis);
//		// 生成签名的时候使用的秘钥secret
//		SecretKey key = generalKey();
//
//		// 下面就是在为payload添加各种标准声明和私有声明了
//		JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
//				.setClaims(claims) // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
//				.setIssuedAt(now) // iat: jwt的签发时间
//				.setSubject(subject)// 一个json格式的字符串作为用户的唯一标志。
//				.signWith(signatureAlgorithm, key);// 设置签名使用的签名算法和签名使用的秘钥
//
//		if (ttlMillis >= 0) {
//			long expMillis = nowMillis + ttlMillis;
//			Date exp = new Date(expMillis);
//			builder.setExpiration(exp); // 设置过期时间戳
//		}
//
//		return builder.compact();
//
//	}
//
//	/**
//	 * @description 通过jwt解析得到claims数据描述对象
//	 */
//	public static Claims parseJWT(String jwt){
//		// 得到原来的签名秘钥，用其才能解析JWT
//		SecretKey key = generalKey();
//		// 得到 DefaultJwtParser
//		Claims claims = Jwts.parser().setSigningKey(key) // 设置签名的秘钥
//				.parseClaimsJws(jwt).getBody();// 设置需要解析的jwt
//
//		return claims;
//	}
//
//	/**
//	 * @param: 生成secret  key
//	 * @return javax.crypto.SecretKey
//	 */
//	private static SecretKey generalKey() {
//		// stringKey
//		String stringKey = JWT_SECRET;
//		// 使用base64解码
//		byte[] encodedKey = Base64.decodeBase64(stringKey);
//		// 根据给定的字节数组使用AES加密算法构造一个密钥
//		SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
//		return secretKey;
//	}
//
//
//	public static void main(String[] args) throws Exception {
//		Map<String, Object> claims = new HashMap<String, Object>();
//		claims.put("userId", "402881466c9433e8016c9447c68e0003");
//		claims.put("role", "user");
//		claims.put(JWTUtils.USER_TYPE_KEY, UserTypeEnum.WX_USER.getCode());
//		long expires = 8*60*60*1000;
//		//unix时间戳，有效时间8小时
//		System.out.println(createJWT(claims, "wxplatform", expires));
//
//		System.out.println(System.currentTimeMillis());
//	}
//}

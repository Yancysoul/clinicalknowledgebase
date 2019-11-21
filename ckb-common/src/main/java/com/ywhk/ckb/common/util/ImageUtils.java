package com.ywhk.ckb.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageUtils {
	public static String fileToBase64(String url) {
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		try {
			String type = "jpg";
			if (url.endsWith(".jpeg") || url.endsWith(".jpg") || url.endsWith(".JPEG") || url.endsWith(".JPG")) {
				type = "jpg";

			} else if (url.endsWith(".png") || url.endsWith(".PNG")) {
				type = "png";
			}

			File file = new File(url);
			BufferedImage img = ImageIO.read(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(img, type, out);
			String res = new String("data:image/" + type + ";base64," + Base64.encodeBase64String(out.toByteArray()));
			return res;
		} catch (IOException e) {
			log.error("图片转base64失败:", e);
		}
		return null;
	}
	
	
	public static String getSuffixName(String url) {
		String pri= url.split(";")[0];
		return pri.replace("data:image/", "");
	}

	public static void base64StringToImage(String base64String, String destPath,String filename,String suffixName) {
		try {
			mkdirs(destPath);
			base64String= base64String.split(";")[1];
			base64String = base64String.substring(7);
			byte[] bytes1 = Base64.decodeBase64(base64String);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			BufferedImage bi1 = ImageIO.read(bais);
			File f1 = new File(destPath+"//"+filename+"."+suffixName);
			ImageIO.write(bi1, suffixName, f1);
		} catch (IOException e) {
			log.error("保存图片失败:", e);
		}
	}
	
	 /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．
     * (mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }
}

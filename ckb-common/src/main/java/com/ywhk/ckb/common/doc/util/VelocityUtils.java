package com.ywhk.ckb.common.doc.util;

import lombok.SneakyThrows;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class VelocityUtils {

    private static Properties properties=new Properties();

    static {
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
    }
	@SneakyThrows
	public static String getContent(String template,VelocityContext context){
		VelocityEngine velocityEngine = new VelocityEngine(properties);
        StringWriter content = new StringWriter();
        velocityEngine.mergeTemplate(template, "UTF-8", context, content);
        return content.toString();
	}

	public static String getContent(String template,Object object){
	    if(object == null){
            return template;
        }
        VelocityContext context = new VelocityContext();
        Map<String,Object> map = ObjectUtils.objectToMap(object,true);
	    for (Map.Entry<String,Object> entry : map.entrySet()){
            context.put(entry.getKey(),entry.getValue());
        }
	    return getContent(template,context);
    }
}

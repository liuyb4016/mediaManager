package com.eshore.yxt.media.core.mvc.json;

import java.io.IOException;
import java.text.SimpleDateFormat; 
import java.util.Date; 

import org.codehaus.jackson.JsonGenerator; 
import org.codehaus.jackson.JsonProcessingException; 
import org.codehaus.jackson.map.JsonSerializer; 
import org.codehaus.jackson.map.SerializerProvider; 
import org.springframework.stereotype.Component;


/**
 * 描述:日期时间格式化
 *
 * @author majintao
 * @version 1.0
 * @see 参考的JavaDoc
 */
public class JsonDTimeSerializer extends JsonSerializer<Date>{ 

   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   @Override 
   public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) 
           throws IOException, JsonProcessingException { 

       String formattedDate = dateFormat.format(date); 

       gen.writeString(formattedDate); 
   } 

} 
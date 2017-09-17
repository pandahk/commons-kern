package com.snowstone.commons.kern;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
    	BigDecimal breaksAmt = new BigDecimal("0.000000");
    	BigDecimal sum=new BigDecimal("112.5677");
 
    	BigDecimal ret=breaksAmt.add(sum);
    	System.out.println(ret);
    	System.out.println(ret.setScale(2, RoundingMode.HALF_UP));
    	
    	
    	System.out.println(ret.doubleValue());
    	System.out.println(BigDecimal.ZERO);
    	
    	System.out.println(new BigDecimal(18).divide(new BigDecimal(100)));
    	
    	System.out.println("-------------");
    	System.out.println(ret.intValue());
    	
//    	System.out.println(sum.intValue());
//    	System.out.println(sum.floatValue());
//    	
//    	System.out.println(sum.compareTo(new BigDecimal("22.12")));
//    	
//    	System.out.println(sum.setScale(2, RoundingMode.HALF_UP));
//    	
//    	System.out.println(sum.abs());
    	System.out.println(sum.setScale(2, RoundingMode.HALF_UP).add(new BigDecimal("34.12")).divide(new BigDecimal(100)));
//    	System.out.println(sum.floatValue());
//    	String os = System.getProperty("os.name");
//    	String cmd = "rundll32 url.dll,FileProtocolHandler http://baidu.com" ;
//			try {
//				Runtime.getRuntime().exec(cmd);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	
    	
    	
    	Map<String,String> m=new HashMap<>();
    	m.put("100", "lisi");
//    	m.put("100", "zhangsan");
    	
    	System.out.println(m.get("100"));
    		
    }
}

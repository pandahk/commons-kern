/*
 * Copyright (C), 2015-2016, 上海睿民互联网科技有限公司
 * Package com.ruim.ifsp.batch.util 
 * FileName: AmountUtils.java
 * Author:   shrm_tyzf004
 * Date:     2016年11月23日 下午2:47:28
 * Description: //模块目的、功能描述      
 * History: //修改记录
 *===============================================================================================
 *   author：          time：                             version：           desc：
 *   shrm_tyzf004           2016年11月23日下午2:47:28                     1.0                  
 *===============================================================================================
 */
package cn.jszhan.commons.kern.apiext.math;


import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;


public class AmountUtils {
    /**
     * 分转元
     * @param cent
     * @return
     */
    public static BigDecimal centToYuan(String cent){
        if(StringUtils.isNotBlank(cent)){
            BigDecimal amount = new BigDecimal(cent);
            return amount.divide(new BigDecimal("100"));
        }
        return null;
    }
    /**
     *元转分字符串
     * @param yuan
     * @return
     */
    public static BigDecimal yuanToCent(String yuan){
        if(StringUtils.isNotBlank(yuan)){
        	BigDecimal ret = new BigDecimal(yuan).multiply(new BigDecimal("100")).setScale(0);
            return ret;
        }
        return null;
    }
    
    public  static void main(String[] args ){
        System.out.println(yuanToCent("100.00").toString());
        System.out.println(centToYuan("100").toString());
       
    }
}

package com.lixingyong.meneusoft.common.utils;

import java.util.Collection;
import java.util.Iterator;

public class SetUtil{
    public static String toPatterString(Collection set, String patter){
        if (set == null) {
            return null;
        } else {
            Iterator it = set.iterator();
            StringBuffer sb = new StringBuffer();
            while(it.hasNext()){
                sb.append(it.next()).append(patter);
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }
}

package com.thinkgem.jeesite.modules.pdd.email.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
	private static ObjectMapper mapper;
	  
    public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if (createNew) {   
            return new ObjectMapper();
        } else if (mapper == null) {   
            mapper = new ObjectMapper();
        }   
        return mapper;   
    }   
}

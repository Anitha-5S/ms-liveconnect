package com.c2.lc.lib.base;

import com.c2.lc.lib.utils.SystemHelper;
import com.google.gson.annotations.Expose;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseSuper {
    
    @Expose(serialize = false, deserialize = false)
    @Autowired public SystemHelper helper;

}

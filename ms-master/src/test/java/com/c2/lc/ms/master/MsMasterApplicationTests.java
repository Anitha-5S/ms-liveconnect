package com.c2.lc.ms.master;

import com.c2.lc.ms.master.models.ItemSellersList;
import com.c2.lc.lib.utils.SystemHelper;
import org.junit.jupiter.api.Test;

//@SpringBootTest
class MsMasterApplicationTests {

    @Test
    public void testPo() {
        ItemSellersList o = new ItemSellersList();
        System.out.println(new SystemHelper().toJson(o));
    }

}

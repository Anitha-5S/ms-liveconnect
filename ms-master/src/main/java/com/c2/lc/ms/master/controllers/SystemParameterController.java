package com.c2.lc.ms.master.controllers;

import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = { "/lc/ms/mst/sp", "${api.base.path}/sp"})
public class SystemParameterController extends MasterBaseController {
/*

    @Autowired private MasterT catalogueTransaction;

    @PostMapping(value = "", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSystemParameter(@RequestBody SystemParameterEntity payload) {
        ApiResponse apiResponse = this.initializeResponse("/lc/ms/mst/sp");
        try {
            BannerModel bannerModel = helper.fromJson(helper.toJson(payload), BannerModel.class);
            this.validateInputPayload(bannerModel);
            //MasterSystemParameter masterSystemParameter

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
*/

}

package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.entities.mongo.LcMolecule;
import com.c2.lc.ms.master.transactions.interfaces.MoleculeTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping( value = "${api.base.path}/mol")
public class MoleculeController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired private MoleculeTransaction moleculeTransaction;

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveMolecule(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/mol/molecule" + " ->" + payload);
        try {
            LcMolecule lcMolecule = helper.fromJson(payload, LcMolecule.class);
            this.validateInputPayload(lcMolecule);
            moleculeTransaction.saveMolecule(lcMolecule);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
/*    @DeleteMapping(value = "/{moleculeId}",produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteMolecule(@PathVariable ("moleculeId") String moleculeId) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/mol/molecule" + moleculeId);
        try {
            moleculeTransaction.deleteMolecule(moleculeId);
            this.addMessage(apiResponse, MsMessages.MOLECULE_DELETE);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }*/

    /**
     * API Id :
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> searchMolecule(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/mol/list " +" ->"+headers.toString()+" ->"+ payload);

        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            SearchBO searchBO = this.getValidatedSearchBO(payload);

            JsonArray jsonArray = moleculeTransaction.list(searchBO);

            JsonObject data = this.getPaginatedResponse(searchBO,jsonArray);
            this.setDataJsonObjectPayload(apiResponse, data);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

}

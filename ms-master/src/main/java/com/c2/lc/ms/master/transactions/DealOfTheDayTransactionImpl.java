package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DataFormatException;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.bos.DealOfTheDayBO;
import com.c2.lc.ms.master.bos.DealSearchBo;
import com.c2.lc.ms.master.bos.ItemPLPResponseBO;
import com.c2.lc.ms.master.entities.mysql.DealOfTheDayEntity;
import com.c2.lc.ms.master.services.interfaces.DealOfTheDayService;
import com.c2.lc.ms.master.services.interfaces.ItemService;
import com.c2.lc.ms.master.transactions.interfaces.DealOfTheDayTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DealOfTheDayTransactionImpl extends BaseTransactionImpl implements DealOfTheDayTransaction {
    @Autowired
    private DealOfTheDayService dealOfTheDayService;

    @Autowired
    private ItemService itemService;

    @Override
    public DealOfTheDayEntity save(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws DuplicateRecordException {
        return dealOfTheDayService.save(header, dealOfTheDayBO);
    }

    @Override
    public void updateDealStatus(JsonObject jsonObject, LcHeaderBO header) throws RecordNotFoundException {
        dealOfTheDayService.updateDealStatus(jsonObject, header);
    }

    @Override
    public void editDeal(LcHeaderBO header, DealOfTheDayBO dealOfTheDayBO) throws RecordNotFoundException {
        dealOfTheDayService.editDeal(header, dealOfTheDayBO);
    }

    @Override
    public DealOfTheDayBO singleDeal(JsonObject jsonDeal) throws RecordNotFoundException {
        return dealOfTheDayService.singleDeal(jsonDeal);

    }

    @Override
    public List<DealOfTheDayBO> fetchDeals(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException {
        return dealOfTheDayService.fetchDeals(c2Code, dealSearchBo);
    }

    @Override
    public List<ItemPLPResponseBO> DealOfTheDayProducts(SearchBO searchBO, LcHeaderBO header, JsonObject request) throws RecordNotFoundException, DataFormatException {
        List<Object[]> items = dealOfTheDayService.DealOfTheDayProducts(searchBO, header, request);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }

        return getFilteredItemPLPResponseBOS(searchBO, request, items);
    }

    private List<ItemPLPResponseBO> getFilteredItemPLPResponseBOS(SearchBO searchBO, JsonObject request, List<Object[]> items) throws DataFormatException {
        List<ItemPLPResponseBO> list = dealOfTheDayService.getTSDealPLP(items, searchBO, request);
        if (request.has("j_min_max_price")) {
            JsonArray array = request.get("j_min_max_price").getAsJsonArray();
            if (array.size() > 0) {
                list = list.stream().filter(x -> (x.getOfferRate().compareTo(array.get(0).getAsBigDecimal()) > 0 &&
                        x.getOfferRate().compareTo(array.get(1).getAsBigDecimal()) < 0)).collect(Collectors.toList());
            }
        }

        List<String> prdForms = new ArrayList<>();
        if (request.has("j_product_forms")) {
            JsonArray array = request.get("j_product_forms").getAsJsonArray();
            if (array.size() > 0) {
                for (JsonElement arr : array) {
                    prdForms.add(arr.getAsString());
                }
                list = list.stream().filter(x -> prdForms.contains(x.getPackTypeName())).collect(Collectors.toList());
            }
        }

        if (request.has("j_discount")) {
            JsonArray array = request.get("j_discount").getAsJsonArray();
            if (array.size() > 0) {
                for (JsonElement el : array) {
                    String discount = el.getAsString();
                    if (discount.contains("<")) {
                        String[] arr = discount.split("<");
                        BigDecimal disc = helper.getBigDecimal(helper.getIntegerValue(arr[1].trim()).intValue());
                        list = list.stream().filter(x -> x.getDiscPercentage().compareTo(disc) < 0)
                                .collect(Collectors.toList());
                    } else if (discount.contains(">")) {
                        String[] arr = discount.split(">");
                        BigDecimal disc = helper.getBigDecimal(helper.getIntegerValue(arr[1].trim()).intValue());
                        list = list.stream().filter(x -> x.getDiscPercentage().compareTo(disc) > 0)
                                .collect(Collectors.toList());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public long DealOfTheDayCount(SearchBO searchBO, LcHeaderBO header, JsonObject request) throws RecordNotFoundException {
        List<Object[]> items = dealOfTheDayService.DealOfTheDayProductsCount(searchBO, header, request);
        if (items.size() == 0) {
            throw new RecordNotFoundException("No u_code found!");
        }
        return itemService.tsProductsCount(items, searchBO);
    }

    @Override
    public int DealOfTheDayListCount(String c2Code, DealSearchBo dealSearchBo) throws RecordNotFoundException {
        return dealOfTheDayService.DealOfTheDayListCount(c2Code,dealSearchBo);
    }

}

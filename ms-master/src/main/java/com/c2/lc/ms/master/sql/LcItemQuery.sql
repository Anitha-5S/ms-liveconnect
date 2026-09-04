SELECT
uim.c_code AS _id,uim.c_name AS c_item_name,uim.c_item_mfac_code AS c_mfg_code,
uimm.c_name AS c_mfg_name, uicm.c_name AS c_contains, uipm.c_name AS c_pack_name,
uipm.n_qty_per_box as n_pack_size,
uim.n_max_mrp as n_mrp,uim.c_hsn_code as c_hsn_code, uim.c_gst_code as n_gst,
uim.d_adate as d_adate,list.c_molecule_code AS c_molecule_code, molecile.c_name as c_molecure_name, moleunit.c_name as c_molecule_unit,
umi.c_drug_name, umi.c_therapeutic_class, umi.c_available_doses, umi.c_indications, umi.c_ontraindications, umi.c_schedule,
umi.c_dosage_forms, umi.c_antidote, umi.c_pregnancy_category, umi.c_references
FROM u_item_mst uim
LEFT JOIN u_item_mfac_mst uimm ON uim.c_item_mfac_code = uimm.c_code
LEFT JOIN u_item_cont_mst uicm ON uim.c_item_cont_code = uicm.c_code
LEFT JOIN u_item_pack_mst uipm ON uim.c_item_pack_code = uipm.c_code
left join u_item_molecule_list list on uim.c_code=list.c_item_code
left join u_item_molecule_mst molecile  on  list.c_molecule_code=molecile.c_code
left join u_item_molecule_unit_mst moleunit on list.c_item_molecule_unit_code=moleunit.c_code
left join u_molecule_info umi on molecile.c_code = umi.c_molecule_code
order by uim.c_code
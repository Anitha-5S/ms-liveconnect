SELECT
uimm.c_code AS _id, uimm.c_name AS c_manufacture_name,
uimm.d_adate AS d_created_date, uimm.c_email AS c_email,
uimm.c_website AS c_website_url, uimm.c_phone_1 AS c_phoneNumber,
uimm.c_add_1 AS c_address1, uimm.c_add_2 AS c_address2,
uimm.c_pin AS c_pin, uimm.c_geo_area_code AS c_area_code,
uimm.c_trade_licence_no_1 AS c_trade_licence_no, uimm.c_contact_person AS c_contact_name
FROM u_item_mfac_mst uimm
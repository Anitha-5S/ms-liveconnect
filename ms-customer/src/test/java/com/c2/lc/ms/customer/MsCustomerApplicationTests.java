package com.c2.lc.ms.customer;

import com.c2.lc.lib.utils.SystemHelper;
import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.entities.customer.DocumentEntity;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.LegalIdentitiesEntity;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

//@SpringBootTest
class MsCustomerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void firm(){
		SystemHelper helper = new SystemHelper();
		FirmEntity firm = new FirmEntity();
		firm.setNFirmId(100L);
		firm.setCName("C-Square Firm");
		firm.setCGstNo("GST1234567890");
		firm.setCGstType("A");
		firm.setContactDetail(getContactDetail());
		firm.setCImageUrl("");
		firm.setCStatus("N");
		firm.setCType("B");
		firm.setCPin("600091");
		firm.setCMobileNo("9999999999");
		firm.setDocumentDetail(getDocumentDetail());
		firm.setLegalIdentities(getLegalIdentities());
		System.out.println(helper.toJson(firm));
	}

	private LegalIdentitiesEntity getLegalIdentities() {
		LegalIdentitiesEntity legal = new LegalIdentitiesEntity();
		LocalDate date = LocalDate.parse("2021-08-26");
		legal.setNLegalId(1L);
		legal.setCDrugLicenseNo1("1");
		legal.setCDrugLicenseNo1Img("");
		legal.setDDrugLicenseNo1ExpiryDate(date);
		legal.setCDrugLicenseNo2("1");
		legal.setCDrugLicenseNo2Img("");
		legal.setDDrugLicenseNo2ExpiryDate(date);
		legal.setCNarcoticNo("10");
		legal.setCNarcoticNoImg("");
		return legal;
	}

	private DocumentEntity getDocumentDetail() {
		DocumentEntity document = new DocumentEntity();
		document.setCAuthorityLetter("1");
		document.setCAuthorityLetterImg("");
		document.setCBankStatement("1");
		document.setCBankStatementImg("");
		document.setNDocumentsId(1L);
		document.setCElectricityBill("1");
		document.setCElectricityBillImg("");
		document.setCItPanNo("100");
		document.setCItPanNoImg("");
		document.setCPanNo("101");
		document.setCPanNoImg("");
		document.setCPartnershipDeed("102");
		document.setCPartnershipDeedImg("");
		document.setCRentAgreement("103");
		document.setCRentAgreementImg("");
		document.setCTanNo("104");
		document.setCTanNoImg("");
		return document;
	}

	private ContactDetailEntity getContactDetail() {
		ContactDetailEntity contact = new ContactDetailEntity();
		contact.setNContactId(1L);
		contact.setCContactName("Contact Person");
		contact.setCAddress1("1st Street");
		contact.setCAddress2("Main Road");
		contact.setCAlternateEmailId("sample@gmail.com");
		contact.setCAlternateMobileNo("9999999990");
		contact.setCAreaCode("A0001");
		contact.setCAreaName("Area1");
		contact.setCCityCode("B0001");
		contact.setCCityName("City1");
		contact.setCStateCode("C0001");
		contact.setCStateName("State1");
		contact.setCAlternatePhoneNo("7539514562");
		contact.setCCountryCode("91");
		contact.setCEmailId("email@gmail.com");
		contact.setCImageUrl("");
		contact.setCCountryName("India");
		contact.setCLandmark("Landmark");
		contact.setCMobileNo("9999999999");
		contact.setCNote("Notes");
		contact.setCPhoneNo("0421456890");
		contact.setCPin("600091");
		return contact;
	}

	@Test
	public void url() {
		String utl = "f:/folder1/f4/sub/test.png";
		System.out.println(utl.substring(utl.lastIndexOf("/")));
	}

}

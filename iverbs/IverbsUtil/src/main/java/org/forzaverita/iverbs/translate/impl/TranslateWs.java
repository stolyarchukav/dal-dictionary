//package org.forzaverita.iverbs.translate.impl;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.xml.namespace.QName;
//
//import net.webservicex.generated.TranslateService;
//import net.webservicex.generated.TranslateServiceSoap;
//
//import org.forzaverita.iverbs.translate.Translator;
//
//public class TranslateWs implements Translator {
//
//	private TranslateServiceSoap service;
//
//	public TranslateWs() {
//		try {
//			service = new TranslateService(new URL("http://www.webservicex.net/TranslateService.asmx?WSDL"),
//					new QName("http://www.webservicex.net", "TranslateService")).
//					getTranslateServiceSoap();
//		}
//		catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public String translate(String text, String lang) throws Exception {
//		String langMode = null;
//		if (lang.equals("es")) {
//			langMode = "EnglishTOGerman";
//		}
//		return service.translate(langMode, text);
//	}
//
//}

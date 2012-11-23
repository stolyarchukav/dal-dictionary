package org.forzaverita.iverbs.lang;

import org.forzaverita.iverbs.ConnectionFactory;


public class Ukrainian {

	public static void main(String[] args) throws Exception {
		ConnectionFactory.createConnection().createStatement().
			execute("alter table verb add " + "ua" + " text");
		LangImporter.importLang("ua", 0, 3, "ukrainian");
	}
	
}

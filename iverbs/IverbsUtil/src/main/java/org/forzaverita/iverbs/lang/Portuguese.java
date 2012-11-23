package org.forzaverita.iverbs.lang;

import org.forzaverita.iverbs.ConnectionFactory;


public class Portuguese {

	public static void main(String[] args) throws Exception {
		ConnectionFactory.createConnection().createStatement().
			execute("alter table verb add " + "pt" + " text");
		LangImporter.importLang("pt", 0, 3, "portuguese");
	}
	
}

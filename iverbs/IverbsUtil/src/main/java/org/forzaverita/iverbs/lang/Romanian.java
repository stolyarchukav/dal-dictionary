package org.forzaverita.iverbs.lang;

import org.forzaverita.iverbs.ConnectionFactory;


public class Romanian {

	public static void main(String[] args) throws Exception {
		ConnectionFactory.createConnection().createStatement().
			execute("alter table verb add " + "ro" + " text");
		LangImporter.importLang("ro", 0, 1, "romanian", "	a ");
	}
	
}

package org.forzaverita.daldic.widget;

public interface WidgetRefreshTask {

	void next();

	void previous();
	
	void pauseTask();

	void resumeTask();
	
}

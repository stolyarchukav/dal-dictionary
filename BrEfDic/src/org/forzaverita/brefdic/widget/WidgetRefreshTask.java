package org.forzaverita.brefdic.widget;

public interface WidgetRefreshTask {

	void next();

	void previous();
	
	void pauseTask();

	void resumeTask();
	
}

package org.forzaverita.iverbs.table;

import java.util.Comparator;

public interface SortableTable<T> {

    void clearHeaders();

    void setComparator(Comparator<T> comparator);

    void loadTable(Runnable... preLoadTasks);

}

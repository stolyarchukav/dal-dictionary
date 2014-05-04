package org.forzaverita.iverbs.table;

import android.widget.TextView;

import java.util.Comparator;

public class OrderingHandler<T> {

    private static final String ARROW_DOWN = "▼";
    private static final String ARROW_UP = "▲";

    private volatile boolean orderAsc;
    private final TextView header;
    private final CharSequence text;
    private final Comparator<T> comparatorAsc;
    private final Comparator<T> comparatorDesc;
    private final SortableTable table;

    public OrderingHandler(TextView header, Comparator<T> comparatorAsc,
                           Comparator<T> comparatorDesc, SortableTable table) {
        this.header = header;
        this.comparatorAsc = comparatorAsc;
        this.comparatorDesc = comparatorDesc;
        this.table = table;
        this.text = header.getText();
    }

    public void setOrder(boolean asc) {
        orderAsc = ! asc;
        changeOrder();
    }

    public void changeOrder() {
        table.clearHeaders();
        if (orderAsc) {
            table.setComparator(comparatorDesc);
            table.loadTable();
            header.setText(header.getText() + ARROW_DOWN);
        } else {
            table.setComparator(comparatorAsc);
            table.loadTable();
            header.setText(header.getText() + ARROW_UP);
        }
        orderAsc ^= true;
    }

    public void clear() {
        header.setText(text);
    }

}

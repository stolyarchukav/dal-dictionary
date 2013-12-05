package org.forzaverita.iverbs.table;

import android.widget.TextView;

import org.forzaverita.iverbs.data.StatItem;

import java.util.Comparator;

public class OrderingHandler {

    private static final String ARROW_DOWN = "▼";
    private static final String ARROW_UP = "▲";

    private volatile boolean orderAsc;
    private final TextView header;
    private final CharSequence text;
    private final Comparator<StatItem> comparatorAsc;
    private final Comparator<StatItem> comparatorDesc;
    private final SortableTable table;

    public OrderingHandler(TextView header, Comparator<StatItem> comparatorAsc,
                           Comparator<StatItem> comparatorDesc, SortableTable table) {
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

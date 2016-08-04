package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Michael Spitsin
 * @since 2016-08-04
 */
public class DateItem implements ChatItem<DateItem.Holder> {
    private final static DateFormat DATE_ITEM_FORMAT_MONTH = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
    private final static DateFormat DATE_ITEM_FORMAT_YEAR = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

    private final String dateFormatted;

    public DateItem(long date) {
        dateFormatted = format(date * 1000);
    }

    @Override
    public void onBindHolder(Holder holder, int position) {
        holder.dateView.setText(dateFormatted);
    }

    @Override
    public int getType() {
        return 2;
    }

    private static String format(long date) {
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        rightNow.setTimeInMillis(date);
        int dateYear = rightNow.get(Calendar.YEAR);

        if (year != dateYear) {
            return DATE_ITEM_FORMAT_YEAR.format(date);
        } else {
            return DATE_ITEM_FORMAT_MONTH.format(date);
        }
    }

    public static final class Holder extends BindViewHolder {
        final TextView dateView = bind(R.id.date);

        public Holder(View itemView) {
            super(itemView);
        }
    }
}

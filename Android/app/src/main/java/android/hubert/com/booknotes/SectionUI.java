package android.hubert.com.booknotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hubert.dal.entity.BookEntity;
import com.hubert.dal.entity.SectionEntity;

/**
 * Created by hubert.fu on 6/7/2017.
 */

public class SectionUI extends LinearLayout {
    private SectionEntity mEntity;
    ListView mBlockView;

    public SectionUI(Context context) {
        super(context);
        setUp(context);
    }

    public SectionUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public void setEntity(SectionEntity entity) {
        mEntity = findLowestSection(entity);
        if (mEntity == null) {
            return;
        }

        TextView titleView = (TextView) findViewById(R.id.textViewTitle);
        titleView.setText(mEntity.name);

        BlockAdapter adapter = new BlockAdapter(getContext(), mEntity.blocks);
        mBlockView.setAdapter(adapter);
    }

    private void setUp(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.section, this);

        mBlockView = (ListView) findViewById(R.id.listViewBlock);

        mBlockView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount < totalItemCount || totalItemCount == 0) {
                    return;
                }

                SectionEntity next = findNextSection();
                setEntity(next);
            }
        });
    }

    private static SectionEntity findLowestSection(SectionEntity entity) {
        if (entity == null){
            return null;
        }

        if (!entity.blocks.isEmpty()) {
            return entity;
        }

        for (SectionEntity child : entity.childSections) {
            SectionEntity temp = findLowestSection(child);
            if (temp != null) {
                return temp;
            }
        }
        return null;
    }

    private SectionEntity findNextSection() {
        if (mEntity.parent != null) {
            for (SectionEntity temp : mEntity.parent.childSections) {
                if (temp.order > mEntity.order) {
                    return temp;
                }
            }
        }

        BookEntity book = mEntity.book;
        SectionEntity parent = mEntity;
        while(book == null || book.sections == null || book.sections.isEmpty()){
            parent = parent.parent;
            if (parent == null){
                return null;
            }
            book = parent.book;
        }

        for (SectionEntity temp : book.sections) {
            if (temp.parent != null) {
                continue;
            }
            if (temp.order > mEntity.order) {
                return temp;
            }
        }

        return null;
    }

}

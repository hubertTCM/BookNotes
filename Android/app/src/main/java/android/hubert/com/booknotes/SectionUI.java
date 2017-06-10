package android.hubert.com.booknotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hubert.dal.entity.BlockEntity;
import com.hubert.dal.entity.BookEntity;
import com.hubert.dal.entity.SectionEntity;

/**
 * Created by hubert.fu on 6/7/2017.
 */

public class SectionUI extends LinearLayout {
    private SectionEntity mSectionEntity;
    private TextView mTitleView;
    private ListView mBlockView;
    private BlockAdapter mBlockAdapter;

    public SectionUI(Context context) {
        super(context);
        setUp(context);
    }

    public SectionUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context);
    }

    public void setEntity(SectionEntity entity) {
        mSectionEntity = findLowestSection(entity);
        if (mSectionEntity == null) {
            return;
        }


        mTitleView.setText(mSectionEntity.name);

        if (mBlockAdapter == null) {
            mBlockAdapter = new BlockAdapter(getContext(), mSectionEntity.blocks);
            mBlockView.setAdapter(mBlockAdapter);
        }
        else{
            mBlockAdapter.addAll(mSectionEntity.blocks);
        }
    }

    private void setUp(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.section, this);

        mBlockView = (ListView) findViewById(R.id.listViewBlock);
        mTitleView = (TextView) findViewById(R.id.textViewTitle);

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

        mBlockView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BlockEntity blockEntity  = mBlockAdapter.getItem(position);
                mSectionEntity = blockEntity.section;
                mTitleView.setText(mSectionEntity.name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private static SectionEntity findLowestSection(SectionEntity entity) {
        if (entity == null) {
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
        if (mSectionEntity == null){
            return null;
        }
        if (mSectionEntity.parent != null) {
            for (SectionEntity temp : mSectionEntity.parent.childSections) {
                if (temp.order > mSectionEntity.order) {
                    return temp;
                }
            }
        }

        BookEntity book = mSectionEntity.book;
        SectionEntity parent = mSectionEntity;
        while (book == null || book.sections == null || book.sections.isEmpty()) {
            parent = parent.parent;
            if (parent == null) {
                return null;
            }
            book = parent.book;
        }

        for (SectionEntity temp : book.sections) {
            if (temp.parent != null) {
                continue;
            }
            if (temp.order > mSectionEntity.order) {
                return temp;
            }
        }

        return null;
    }

}

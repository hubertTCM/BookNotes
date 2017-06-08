package android.hubert.com.booknotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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

        TextView titleView = (TextView) findViewById(R.id.textViewTitle);
        titleView.setText(mEntity.name);

        ListView blockView = (ListView)findViewById(R.id.listViewBlock);
        BlockAdapter adapter = new BlockAdapter(getContext(), mEntity.blocks);
        blockView.setAdapter(adapter);
    }

    private void setUp(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.section, this);
    }

    private SectionEntity findLowestSection(SectionEntity entity) {
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

}

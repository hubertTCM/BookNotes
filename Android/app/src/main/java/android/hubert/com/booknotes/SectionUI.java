package android.hubert.com.booknotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public void setEntity(SectionEntity entity){
        mEntity = entity;

        TextView titleView = (TextView)findViewById(R.id.textViewTitle);
        titleView.setText(mEntity.name);
    }

    private void setUp(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.section, this);
    }
}

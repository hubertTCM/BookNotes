package android.hubert.com.booknotes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.hubert.dal.entity.BlockEntity;
import com.hubert.dal.entity.SectionEntity;

import java.util.Collection;

/**
 * Created by hubert.fu on 5/29/2017.
 */

public class BlockAdapter extends ArrayAdapter<BlockEntity> {

    private LayoutInflater mInflater;
    private Context mContext;

    public BlockAdapter(@NonNull Context context, Collection<BlockEntity> blocks) {
        super(context, R.layout.block);
        addAll(blocks);

        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BlockEntity item = this.getItem(position);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.section, parent, false);
        }

        TextView titleView = (TextView)convertView.findViewById(R.id.textViewContent);
        titleView.setText(item.content);

        return convertView;
    }
}

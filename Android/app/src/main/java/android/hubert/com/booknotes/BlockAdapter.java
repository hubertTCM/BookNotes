package android.hubert.com.booknotes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
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
    public BlockAdapter(@NonNull Context context, Collection<BlockEntity> blocks) {
        super(context, R.layout.block);
        addAll(blocks);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BlockEntity item = this.getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.block, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.textViewContent);
        titleView.setText(item.content);

        return convertView;
    }
}

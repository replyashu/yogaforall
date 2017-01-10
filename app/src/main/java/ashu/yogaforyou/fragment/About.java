package ashu.yogaforyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookSdk;
import com.facebook.share.widget.LikeView;

import ashu.yogaforyou.R;

/**
 * Created by apple on 11/01/17.
 */

public class About extends Fragment {

    public About(){

    }

    private View view;
    private LikeView likeView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        FacebookSdk.sdkInitialize(getActivity());
        likeView = (LikeView) view.findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.BOTTOM);
        likeView.setForegroundColor(-256);

        likeView.setObjectIdAndType(
                "https://www.facebook.com/yogakaroaaj",
                LikeView.ObjectType.PAGE);
        return view;
    }
}

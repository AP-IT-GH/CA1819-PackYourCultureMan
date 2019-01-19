package com.ap.pacyourcultureman.Menus;

import android.app.Activity;
import android.support.v4.widget.SlidingPaneLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ap.pacyourcultureman.Helpers.ApiHelper;
import com.ap.pacyourcultureman.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

public class BottomSlideMenu {
    SlidingUpPanelLayout bottomPanel;
    Activity activity;
    TextView txtName, txtShortDesc, txtLongDesc;
    ImageView imgSight;
    public BottomSlideMenu(Activity activity) {
        this.activity = activity;
        bottomPanel = activity.findViewById(R.id.sliding_layout);
        txtName = activity.findViewById(R.id.txtName);
        imgSight = activity.findViewById(R.id.imgSight);
        txtShortDesc = activity.findViewById(R.id.txtShortDesc);
        txtLongDesc = activity.findViewById(R.id.txtLongDesc);
        bottomPanel.setPanelHeight(0);
    }
    public void setPanel(final int i) {
        bottomPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        bottomPanel.setPanelHeight(400);
        txtName.setText(ApiHelper.assignments.get(i).getName());
        txtShortDesc.setText(ApiHelper.assignments.get(i).getShortDescr());
        txtLongDesc.setText(ApiHelper.assignments.get(i).getLongDescr());
        Picasso.get().load(ApiHelper.assignments.get(i).getImgUrl()).into(imgSight);
    }

    public SlidingUpPanelLayout getBottomPanel() {
        return bottomPanel;
    }
}

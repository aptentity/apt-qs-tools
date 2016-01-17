package com.aptentity.aptqstools.view.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.utils.UrgentUtils;

/**
 * Created by Gulliver(feilong) on 15/12/15.
 */
public class UrgentSelectDlg {
    AlertDialog dlg;
    onIndexSelectListener listener;
    public UrgentSelectDlg(Context context){
        dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window mWindow = dlg.getWindow();
        mWindow.setContentView(R.layout.urgent_select_dialog);
        mWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(lp);
        mWindow.setGravity(Gravity.BOTTOM);

        mWindow.findViewById(R.id.btn_high).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.select(UrgentUtils.HIGH);
                }
                hide();
            }
        });

        mWindow.findViewById(R.id.btn_middle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.select(UrgentUtils.MIDDLE);
                }
                hide();
            }
        });
        mWindow.findViewById(R.id.btn_low).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.select(UrgentUtils.LOW);
                }
                hide();
            }
        });
        mWindow.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.select(UrgentUtils.UNSELECT);
                }
                hide();
            }
        });
    }

    public void setGenderSelectListener(onIndexSelectListener listener){
        this.listener = listener;
    }

    public void show(){

    }

    public void hide(){
        dlg.dismiss();
    }

    public interface onIndexSelectListener{
        /**
         * -1-取消,1-男孩,0-女孩
         * @param i
         */
        public void select(int i);
    }
}

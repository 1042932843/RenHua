/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.example.administrator.renhua.entity;



import com.example.administrator.renhua.ui.view.Expandable;

import java.util.List;

/**
 * 消费记录订单
 */
public class FindListPayResponse implements Expandable<FindListPayResponse.Info> {
    public float amounts;
    public String order;
    public String typename;
    public String info; // json
    public List<Info> _info;

    @Override
    public int getChildrenCount() {
        if (_info == null) {
            return 0;
        }
        return _info.size();
    }

    @Override
    public Info getChild(int childPosition) {
        if (_info == null) {
            return null;
        }
        return _info.get(childPosition);
    }

    /**
     * 消费记录订单明细/账单
     */
    public static class Info {
        public String name;
        public String orderno;
        public String code;
        public float amount;
        public String sj;
        public String month;

    }
}

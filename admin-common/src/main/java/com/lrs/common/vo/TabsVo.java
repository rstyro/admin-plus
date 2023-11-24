package com.lrs.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class TabsVo {
    private String id;
    private String text;
    private boolean close;
    private String url;
    /**
     * relative、
     */
    private String urlType;
    private String icon;
    private String targetType="iframe-tab";

    private List<TabsVo> children;
}

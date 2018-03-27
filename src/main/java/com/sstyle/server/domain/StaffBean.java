package com.sstyle.server.domain;

import lombok.Data;

/**
 * Created by ss on 2018/3/24.
 */
@Data
public class StaffBean {
    private String staffId;
    private String staffCode;
    private String staffName;
    private String passwd;
    private String linkPhone;
    private String email;
    private String changeTag;

}

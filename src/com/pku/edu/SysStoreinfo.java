package com.database.project.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * sys_storeinfo:
 */
@Entity
@Table(name = "sys_storeinfo")
public class SysStoreinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * storecode:
     */
    private String storecode;

    /**
     * groupcode:
     */
    private String groupcode;

    /**
     * storename:
     */
    private String storename;

    public SysStoreinfo() {
        super();
    }

    public SysStoreinfo(String storecode, String groupcode, String storename) {
        super();
        this.storecode = storecode;
        this.groupcode = groupcode;
        this.storename = storename;
    }

    public void setStorecode(String storecode) {
        this.storecode = storecode;
    }

    @Id
    @Column(name = "storecode", length = 3, nullable = false)
    public String getStorecode() {
        return storecode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    @Id
    @Column(name = "groupcode", length = 3, nullable = false)
    public String getGroupcode() {
        return groupcode;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    @Column(name = "storename", length = 64, nullable = false)
    public String getStorename() {
        return storename;
    }

    public String toString() {
        return "SysStoreinfo [storecode=" + storecode + ",groupcode="
                + groupcode + ",storename=" + storename + "]";
    }

}

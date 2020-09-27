package com.geovis.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zhouxian
 * @since 2019-06-14
 */
@TableName("sys_log")
public class SysLog extends Model<SysLog> {
    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    private Integer id;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 操作时间
     */
    private Long startTime;
    /**
     * 消耗时间
     */
    private Integer spendTime;
    /**
     * 根路径
     */
    private String basePath;
    /**
     * uri
     */
    private String uri;
    /**
     * url
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 用户标识
     */
    private String userAgent;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 权限值
     */
    private String permissions;
    /**
     * 请求参数
     */
    private String parameter;
    /**
     * 响应结果
     */
    private String result;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Integer spendTime) {
        this.spendTime = spendTime;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static final String ID = "id";

    public static final String DESCRIPTION = "description";

    public static final String USERNAME = "username";

    public static final String STARTTIME = "startTime";

    public static final String SPENDTIME = "spendTime";

    public static final String BASEPATH = "basePath";

    public static final String URI = "uri";

    public static final String URL = "url";

    public static final String METHOD = "method";

    public static final String USERAGENT = "userAgent";

    public static final String IP = "ip";

    public static final String PERMISSIONS = "permissions";

    public static final String PARAMETER = "parameter";

    public static final String RESULT = "result";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", description=" + description +
                ", username=" + username +
                ", startTime=" + startTime +
                ", spendTime=" + spendTime +
                ", basePath=" + basePath +
                ", uri=" + uri +
                ", url=" + url +
                ", method=" + method +
                ", userAgent=" + userAgent +
                ", ip=" + ip +
                ", permissions=" + permissions +
                ", parameter=" + parameter +
                ", result=" + result +
                "}";
    }
}

package com.hsrxxx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hsrxxx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统访问记录
 * </p>
 *
 * @author 松仁
 * @since 2021-03-21
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class SysLogininfor {

    private static final long serialVersionUID=1L;

      /**
     * 访问ID
     */
        @TableId(value = "info_id", type = IdType.AUTO)
      private Long infoId;

      /**
     * 用户账号
     */
      private String userName;

      /**
     * 登录IP地址
     */
      private String ipaddr;

      /**
     * 登录地点
     */
      private String loginLocation;

      /**
     * 浏览器类型
     */
      private String browser;

      /**
     * 操作系统
     */
      private String os;

      /**
     * 登录状态（0成功 1失败）
     */
      private String status;

      /**
     * 提示消息
     */
      private String msg;

      /**
     * 访问时间
     */
      @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
      private LocalDateTime loginTime;

}

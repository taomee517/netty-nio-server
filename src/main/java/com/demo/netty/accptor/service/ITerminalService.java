package com.demo.netty.accptor.service;


import com.fzk.terminal.api.dto.TerminalDataDTO;
import com.fzk.terminal.api.dto.TerminalLoginDTO;
import com.fzk.terminal.api.dto.TerminalRebootDTO;
import com.fzk.terminal.api.vo.TerminalVO;
import com.mysirui.springcloud.api.IBaseService;
import com.mysirui.springcloud.api.vo.RespModel;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author DELL
 * @Title: ITerminalService
 * @ProjectName blackTea-business-otu
 * @Description: TODO 设备登录，登出，重启
 * @date 2019/4/410:26
 */
public interface ITerminalService extends IBaseService {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /terminal/login")
    public RespModel<TerminalVO> login(TerminalLoginDTO dto);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /terminal/logout")
    public RespModel<Boolean> loginOut(TerminalLoginDTO dto);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /terminal/logout?imei={imei}")
    public RespModel<Boolean> find(@Param("imei") String imei);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /terminal/insertRebootLog")
    RespModel<Boolean> insertRebootLog(TerminalRebootDTO dto);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /terminal/info/{id}")
    RespModel<TerminalVO> getInfo(@Param("id") Integer id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("GET /terminal/holdHeartBeat/{imei}")
    RespModel<Boolean> updateTerminalLastMsg(@Param("imei") String imei);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /terminal/update")
    RespModel<Boolean> updateData(TerminalDataDTO dataDTO);
}

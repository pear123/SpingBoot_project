package com.example.demo.controller;

import com.example.demo.entity.Information;
import com.example.demo.entity.Score;
import com.example.demo.service.InformationService;
import com.example.demo.service.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/myInformation")
public class InformationController {
    @Resource
    private InformationService informationService;
    @Resource
    private ScoreService scoreService;

    //用户主界面
    @RequestMapping("userMain")
    public String user_main(Model model, Integer score, Integer u_id) throws Exception {
        System.out.println("第二"+score);
        List<Information> inforList = informationService.queryInformationByUid(u_id);
        model.addAttribute("u_id", u_id);
        model.addAttribute("inforList", inforList);
        model.addAttribute("score",score);
        return "userMain";
    }

    //跳转到增加消息的页面
    @RequestMapping("preAddInformation")
    public String pre_addInformation(Model model, Integer u_id) throws Exception {
        Score score=scoreService.queryScore(u_id);
        model.addAttribute("u_id", u_id);
        model.addAttribute("score",score.getSc());
        return "addInformation";
    }

    //增加消息
    @RequestMapping("addInformation")
    public String addInformation(Model model, Information information) throws Exception {
        Date date = new Date();
        information.setCreatetime(date);
        information.setTitle(information.getTitle());
        information.setContent(information.getContent());
        boolean b = informationService.addInformation(information);
        if (b) {
            Score score=scoreService.queryScore(information.getU_id());
           /* model.addAttribute("score",score.getSc());
            model.addAttribute("u_id",information.getU_id());*/
            return "redirect:userMain?score="+score.getSc()+"&u_id="+information.getU_id();
        }
        model.addAttribute("u_id", information.getU_id());
        model.addAttribute("missMessage", "添加失败");
        return "addInformation";
        /* return "redirect:pre_addInformation.action";*/

    }

    //删除消息
    @RequestMapping("deleteInformation")
    public @ResponseBody
    String deleteInformation(Model model, Integer infor_id) throws Exception {
        boolean b = informationService.deleteInformation(infor_id);
        if (b) {
            return "success";
        }
        return "fail";
    }
    //跳转到编辑页面
    @RequestMapping("preEditInformation")
    public String pre_editInformation(Model model, Integer infor_id) throws Exception {
        Information information=informationService.queryInformationById(infor_id);
        Score score=scoreService.queryScore(information.getU_id());
        model.addAttribute("information",information);
        model.addAttribute("score",score.getSc());
        return "edit";
    }
    //修改
    @RequestMapping("editInformation")
    public @ResponseBody
    String editInformation(Model model, Information information) throws Exception {
        System.out.println("输出时间"+information.getCreatetime());
      /*  Information information1=informationService.queryInformationById(information.getInfor_id());
        information.setCreatetime(information1.getCreatetime());*/
        boolean b = informationService.updateInformation(information);
        if (b) {
            return "success";
        }
        return "fail";
    }
}
